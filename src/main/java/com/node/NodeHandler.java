package com.node;

import PM10.Measurement;
import PM10.PM10Simulator;
import PM10.Simulator;
import com.models.Node;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import grpc.MessageHandlerGrpcClient;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import grpc.MessageHandlerGrpcImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NodeHandler {

    Node myNode;
    String gatewayIP;

    public Node getMyNode() {
        return myNode;
    }

    public void setMyNode(Node myNode) {
        this.myNode = myNode;
    }

    public String getGatewayIP() {
        return gatewayIP;
    }

    public void setGatewayIP(String gatewayIP) {
        this.gatewayIP = gatewayIP;
    }

    Thread tokenThread;
    Thread inputThread;
    Simulator simulator;


    NodeHandler() {

        inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter 'exit' to exit..");
                String exit = scanner.next();
                while (!exit.equals("exit")) {
                    if (exit.equals("nodes")) {
                        System.out.println(NodesRing.getInstance().getNodes().toString());
                    }
                    exit = scanner.next();
                }
                System.out.println("Terminating process...");
                stopThreads();
                System.out.println("Terminating Simulator...");
                simulator.stopMeGently();
                deleteNode(myNode.getId());
            }
        });


        tokenThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Init listener on Local Stat...");
                while (NodesRing.getInstance().getStatus().equals("online")) {
                    if (Token.getInstance().getCurrentId().equals(myNode.getId()) || NodesRing.getInstance().getNodes().size() < 2) {
                        try {
                            synchronized (StatsBuffer.getInstance()) {
                                while (StatsBuffer.getInstance().getLocalStat() == null)
                                    StatsBuffer.getInstance().wait();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Local Stat Produced");
                        nextToken();
                        insertStat(StatsBuffer.getInstance().getLocalStat());
                        System.out.println("Sending token to --> " + Token.getInstance().getNextId());
                        MessageHandlerGrpcClient.sendToken();
                        if (Token.getInstance().getMeasurements().size() == NodesRing.getInstance().getNodes().size())
                            sendGlobalStat();
                        System.out.println("Resetting and Recalculating Local stat ...");
                        Token.getInstance().getMeasurements().remove(myNode.getId());
                        StatsBuffer.getInstance().reset();
                    }
                }
                System.out.println("Token Thread terminated");
            }

        });



    }

    void runTokenThread() {
        tokenThread.start();
    }

    synchronized void stopThreads() {
        NodesRing.getInstance().setStatus("exiting");
        System.out.println(NodesRing.getInstance().getStatus());
    }

    synchronized void insertStat(Measurement localStat) {
        Map<String, Measurement> tokenCopy = Token.getInstance().getMeasurements();
        tokenCopy.put(myNode.getId(), localStat);
        Token.getInstance().setMeasurements(tokenCopy);
        System.out.println("Local stat inserted in Token --> " + Token.getInstance().toString());

    }


    void runInputThread() {
        inputThread.start();
    }

    synchronized void registerNode(String id, int port, String myIP) throws InterruptedException, IOException {
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP + "/nodes");


        String input = "{\"id\":\"" + id + "\",\"IP\":\"" + myIP + "\",\"port\":\"" + port + "\"}";

        System.out.println("New node, registering on Gateway .. " + input);
        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        } else if (response.getStatus() == 412) {
            throw new RuntimeException("Node already in list - "
                    + response.getStatus());
        }
        // NodesRing init
        List<Node> nodesList = new ArrayList<Node>();
        String output = response.getEntity(String.class);
        System.out.println("Gateway response : "  + output);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(output).getAsJsonObject()
                .get("Nodes").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            Node toAdd = new Gson().fromJson(array.get(i), Node.class);
            nodesList.add(toAdd);
        }
        NodesRing.getInstance().setNodes(nodesList);


        Server server = ServerBuilder.forPort(port).addService(new MessageHandlerGrpcImpl()).build();
        server.start();
        MessageHandlerGrpcClient.addNode(id,myIP,port);


        System.out.println("Local Ring from Gateway created ---> " + NodesRing.getInstance().getNodes().toString());

        System.out.println("Starting Measurements Simulator...");
        simulator = new PM10Simulator(StatsBuffer.getInstance());
        simulator.start();


    }

    synchronized void deleteNode(String id) {

        System.out.println("Deleting node on Gateway..");
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP + "/nodes");


        ClientResponse response = webResource.header("id", id)
                .delete(ClientResponse.class);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        } else if (response.getStatus() == 412) {
            throw new RuntimeException("Node already in list - "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        System.out.println(output);

        System.out.println("Deleting node on Nodes..");
        MessageHandlerGrpcClient.removeNode(id);
    }

    void nextToken() {
        System.out.println("Setting next endpoint..");
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        Node nextNode = new Node("", "", 0);
        for (int i = 0; i < nodesCopy.size(); i++) {
            if (myNode.getId().equals(nodesCopy.get(i).getId())) {
                    int nextIndex = (i+1)%nodesCopy.size();
                    nextNode = nodesCopy.get(nextIndex);
            }
        }
        Token.getInstance().setNextIp(nextNode.getIP());
        Token.getInstance().setNextPort(nextNode.getPort());
        Token.getInstance().setNextId(nextNode.getId());
    }

    Measurement getAvg() {
        Map<String, Measurement> statsCopy = Token.getInstance().getMeasurements();
        long lastTimestamp = statsCopy.get(myNode.getId()).getTimestamp();
        String lastType = statsCopy.get(myNode.getId()).getType();
        String lastId = statsCopy.get(myNode.getId()).getId();
        double sum = 0;
        double avg = 0;
        for (Measurement m : statsCopy.values()) {
            sum += m.getValue();
        }
        avg = sum / statsCopy.size();
        return new Measurement(lastId, lastType, avg, lastTimestamp);
    }

    synchronized void sendGlobalStat() {

        Measurement globalMeasurement = getAvg();

        System.out.println("All Local Stats found, sending a Global Stat to Gateway...");

        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP + "/stats");


        String input = "{\"requestType\":\"globalStat\",\"from\":\"" + myNode.getId() + "\",\"value\":\""
                + globalMeasurement.getValue() + "\", \"timestamp\":\"" + globalMeasurement.getTimestamp() + "\", \"id\":\""
                + globalMeasurement.getId() + "\",\"type\":\"" + globalMeasurement.getType() + "\" }";

        System.out.println("Request to Gateway : " + input);
        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        } else if (response.getStatus() == 412) {
            throw new RuntimeException("Stats not reachable - "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        System.out.println("Response from gateway: " + output);
        // Clear token stats list
        Token.getInstance().getMeasurements().clear();


    }
}
