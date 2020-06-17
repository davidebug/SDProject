package com.node;

import PM10.Measurement;
import PM10.PM10Simulator;
import PM10.Simulator;
import com.gateway.Nodes;
import com.gateway.Stats;
import com.grpc.TokenMessageOuterClass;
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
import grpc.MessageHandlerGrpcServer;

import java.io.IOException;
import java.util.*;

public class NodeHandler {


    Thread tokenThread;
    Thread inputThread;
    Simulator simulator;
    Server server;
    String gatewayIP;


    public String getGatewayIP() {
        return gatewayIP;
    }

    public void setGatewayIP(String gatewayIP) {
        this.gatewayIP = gatewayIP;
    }



    public void setMyNode(Node node){
        NodesRing.getInstance().setMyNode(node);
    }

    public NodeHandler() {

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
                removeNode();
            }
        });



        tokenThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Init listener on Local Stat...");
                while (NodesRing.getInstance().getStatus().equals("online") || NodesRing.getInstance().getStatus().equals("elaborating")) {

                    synchronized (LocalToken.getInstance()) {
                        while (!haveToken() && NodesRing.getInstance().getNodes().size() > 1
                        && !NodesRing.getInstance().getStatus().equals("exiting")) {
                            try {
                                System.out.println("Waiting for Token...");
                                LocalToken.getInstance().wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(NodesRing.getInstance().getStatus().equals("exiting")){
                        break;
                    }else if(NodesRing.getInstance().getStatus().equals("online"))
                        NodesRing.getInstance().setStatus("elaborating");

                    if (StatsBuffer.getInstance().getLocalStat() != null) {

                        if(NodesRing.getInstance().getNodes().size() < 2) {
                           insertStat();
                            System.out.println("Local Stat Inserted");
                           sendGlobalStat();
                            System.out.println("Resetting and Recalculating Local stat ...");
                           LocalToken.getInstance().reset();
                           StatsBuffer.getInstance().reset();
                        }
                        else{

                            insertStat();
                            System.out.println("Local Stat Inserted");
                            if (globalStatsCondition())
                                sendGlobalStat();
                            MessageHandlerGrpcClient.sendToken(NodesRing.getInstance().getMyNode().getId());
                            System.out.println("Resetting Local stat ...");
                            LocalToken.getInstance().reset();
                            StatsBuffer.getInstance().reset();
                        }

                    }
                    else if(NodesRing.getInstance().getNodes().size() > 1){
                        System.out.println("Local Stat NOT ready - Sending token..");
                        MessageHandlerGrpcClient.sendToken(NodesRing.getInstance().getMyNode().getId());
                        LocalToken.getInstance().reset();
                    }
                }
                System.out.println("Token Thread terminated");
            }

        });



    }


    public void removeNode(){
        System.out.println("Terminating Simulator...");
        simulator.stopMeGently();
        deleteNode(NodesRing.getInstance().getMyNode().getId());
    }

    boolean globalStatsCondition(){
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        Map<String, Measurement> tokenMeasurements = LocalToken.getInstance().getMeasurements();
        for(Node n : nodesCopy){
            if(tokenMeasurements.containsKey(n.getId())){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    public void runTokenThread() {
        tokenThread.start();
    }


    synchronized void insertStat() {
        Map<String, Measurement> tokenCopy = LocalToken.getInstance().getMeasurements();
        tokenCopy.put(NodesRing.getInstance().getMyNode().getId(), StatsBuffer.getInstance().getLocalStat());
        LocalToken.getInstance().setMeasurements(tokenCopy);
        System.out.println("Local stat inserted in Token");

    }

    public boolean haveToken(){
       return LocalToken.getInstance().getCurrentId().equals(NodesRing.getInstance().getMyNode().getId());
    }

    public void runInputThread() {
        inputThread.start();
    }

    public synchronized void registerNode() throws InterruptedException, IOException {
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP + "/nodes");


        String input = "{\"id\":\"" + NodesRing.getInstance().getMyNode().getId()
                + "\",\"IP\":\"" + NodesRing.getInstance().getMyNode().getIP() + "\",\"port\":\"" + NodesRing.getInstance().getMyNode().getPort() + "\"}";

        System.out.println("New node, registering on Gateway .. ");
        System.out.println(input);
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
        Collections.sort(nodesList);
        NodesRing.getInstance().setNodes(nodesList);


        server = ServerBuilder.forPort(NodesRing.getInstance().getMyNode().getPort()).addService(new MessageHandlerGrpcServer()).build();
        server.start();
        MessageHandlerGrpcClient.addNode(NodesRing.getInstance().getMyNode().getId(), NodesRing.getInstance().getMyNode().getIP(),NodesRing.getInstance().getMyNode().getPort());


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
        if(NodesRing.getInstance().getNodes().size() > 1 && haveToken()) {
            MessageHandlerGrpcClient.sendToken(NodesRing.getInstance().getMyNode().getId());
        }
        MessageHandlerGrpcClient.removeNode(id, server);
    }



    Measurement getAvg() {
        Map<String, Measurement> statsCopy = LocalToken.getInstance().getMeasurements();
        long lastTimestamp = statsCopy.get(NodesRing.getInstance().getMyNode().getId()).getTimestamp();
        String lastType = statsCopy.get(NodesRing.getInstance().getMyNode().getId()).getType();
        String lastId = statsCopy.get(NodesRing.getInstance().getMyNode().getId()).getId();
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


        String input = "{\"requestType\":\"globalStat\",\"from\":\"" + NodesRing.getInstance().getMyNode().getId() + "\",\"value\":\""
                + globalMeasurement.getValue() + "\", \"timestamp\":\"" + globalMeasurement.getTimestamp() + "\", \"id\":\""
                + globalMeasurement.getId() + "\",\"type\":\"" + globalMeasurement.getType() + "\" }";

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
        // Clear token
        LocalToken.getInstance().getMeasurements().clear();


    }
}
