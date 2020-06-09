package com.node;

import PM10.Measurement;
import PM10.PM10Simulator;
import PM10.Simulator;
import com.gateway.Stats;
import com.models.Node;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NodeHandler {

    Token currentToken;
    Node myNode;
    String gatewayIP;
    Thread tokenThread;
    Thread inputThread;
    Simulator simulator;

    NodeHandler() {
        this.currentToken = new Token();
        myNode = null;

        tokenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO: Setto listener per il token
                //TODO: inizializzo il token con i valori arrivati
                //TODO: dentro al listener verifico che il token arrivato ce l'ho io
                // se ce l'ho io --
                System.out.println("Init token thread");
                while(true) {
                        try {
                            synchronized (StatsBuffer.getInstance()) {
                                while (StatsBuffer.getInstance().getLocalStat() == null)
                                    StatsBuffer.getInstance().wait();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    System.out.println("inserisco");
                    insertStat(StatsBuffer.getInstance().getLocalStat());
                    StatsBuffer.getInstance().reset(); //resetto la stat locale a null
                    if (currentToken.getMeasurements().size() == NodesRing.getInstance().getNodes().size())
                        sendGlobalStat();
                    nextToken();
                }// lo passo agli altri
            }
        });

        inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter an id: ");
                String id = scanner.next();
               // System.out.print("Enter your port: ");
               // String port = scanner.next();
                String port = "8080";
               // System.out.print("Enter your ip: ");
                // String myIP = scanner.next();
                String myIP = "localhost";
                //System.out.print("Enter gateway's IP: ");
                //gatewayIP = scanner.next();
                gatewayIP = "http://localhost:8080/restserver/gateway";
                myNode = new Node(id, myIP, port);
                registerNode(id, port, myIP);

                System.out.println("Enter 'exit' to exit..");
                String exit = scanner.next();
                while (!exit.equals("exit")) {
                    if (exit.equals("nodes")) {
                        System.out.println(NodesRing.getInstance().getNodes().toString());
                    }
                    exit = scanner.next();
                }
                tokenThread.interrupt();
                deleteNode(id);
            }
        });

    }

    void runTokenThread() {
        tokenThread.start();
    }

    synchronized void insertStat(Measurement localStat) {
        Map<String, Measurement> tokenCopy = currentToken.getMeasurements();
        tokenCopy.put(myNode.getId(), localStat);
        currentToken.setMeasurements(tokenCopy);
        System.out.println("INSERITA stat nel TOKEN--> " + currentToken.toString());
    }


    void runInputThread() {
        inputThread.start();
    }

    synchronized void registerNode(String id, String port, String myIP) {
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP+"/nodes");


        String input = "{\"id\":\"" + id + "\",\"IP\":\"" + myIP + "\",\"port\":\"" + port + "\"}";

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
        System.out.println(output);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(output).getAsJsonObject()
                .get("Nodes").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            Node toAdd = new Gson().fromJson(array.get(i), Node.class);
            nodesList.add(toAdd);
        }
        NodesRing.getInstance().setNodes(nodesList);
        //TODO: comunicare agli altri della mia entrata e aggiornarli

        System.out.println("Ring created -> " + NodesRing.getInstance().getNodes().toString());

        runTokenThread();
        simulator = new PM10Simulator(StatsBuffer.getInstance());
        simulator.start();


    }

    synchronized void deleteNode(String id) {
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP+"/nodes");


        ClientResponse response = webResource.header("id", id)
                .delete(ClientResponse.class);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        } else if (response.getStatus() == 412) {
            throw new RuntimeException("Node already in list - "
                    + response.getStatus());
        }

        NodesRing.getInstance().removeNode(id);
        //TODO: comunicare agli altri della mia uscita

        String output = response.getEntity(String.class);
        System.out.println(output);

    }

    synchronized void nextToken() {
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        Node nextNode = new Node("", "", "");
        for (int i = 0; i < nodesCopy.size(); i++) {
            if (myNode.getId() == nodesCopy.get(i).getId()) {
                if (i + 1 < nodesCopy.size())
                    nextNode = nodesCopy.get(i + 1);
                else {
                    nextNode = nodesCopy.get(0);
                }
            }
        }
        currentToken.setNextIp(nextNode.getIP());
        currentToken.setNextPort(nextNode.getPort());

        //TODO: Mando il token a tutti
    }

    synchronized Measurement getAvg() {
        Map<String, Measurement> statsCopy = currentToken.getMeasurements();
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

        System.out.println("All nodes stats found, sending global stat to gateway...");

        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP+"/stats");


        String input = "{\"requestType\":\"globalStat\",\"from\":\"" + myNode.getId() + "\",\"value\":\""
                + globalMeasurement.getValue() + "\", \"timestamp\":\"" + globalMeasurement.getTimestamp() + "\", \"id\":\""
                + globalMeasurement.getId() + "\",\"type\":\"" + globalMeasurement.getType() + "\" }";

        System.out.println(input);
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
        System.out.println(output);
        // Clear token stats list
        currentToken.getMeasurements().clear();




    }
}
