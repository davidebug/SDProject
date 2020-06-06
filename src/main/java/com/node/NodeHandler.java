package com.node;

import PM10.Measurement;
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
import java.util.Scanner;

public class NodeHandler {

    String currentToken;
    Node myNode;
    Thread tokenThread;
    Thread inputThread;

    NodeHandler(){
        this.currentToken = "";
        myNode = null;

        tokenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO: Setto listener per il token
                //TODO: dentro al listener verifico che il token arrivato ce l'ho io
                // se ce l'ho io -->

                if(StatsBuffer.getInstance().getLocalStat() == null){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                insertStat(StatsBuffer.getInstance().getLocalStat());
                StatsBuffer.getInstance().reset();
                StatsBuffer.getInstance().reset(); //resetto la stat locale a null


                nextToken(); // lo passo agli altri
            }
        });

        inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter an id: ");
                String id = scanner.next();
                System.out.print("Enter your port: ");
                String port = scanner.next();
                System.out.print("Enter your ip: ");
                String myIP = scanner.next();
                System.out.print("Enter gateway's IP: ");
                String gatewayIP = scanner.next();
                myNode = new Node(id, myIP, port);
                registerNode(id, port, gatewayIP, myIP);
                runTokenThread();
                System.out.println("Enter 'exit' to exit..");
                String exit = scanner.next();
                while(!exit.equals("exit")) {
                    if(exit.equals("nodes")){
                        System.out.println(NodesRing.getInstance().getNodes().toString());
                    }
                    exit = scanner.next();
                }
                deleteNode(id,gatewayIP);
            }
        });
    }

    void runTokenThread(){
        tokenThread.start();
    }

    void insertStat(Measurement localStat){
        //TODO: Regione critica: inserisco la mia statistica nel messaggio token
        String token = localStat.toString();
    }


    void runInputThread(){
        inputThread.start();
    }

    void registerNode(String id, String port, String gatewayIP, String myIP){
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP);


        String input = "{\"id\":\"" + id + "\",\"gatewayIP\":\"" + myIP + "\",\"port\":\"" + port + "\"}";

        System.out.println(input);
        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        }
        else if( response.getStatus() == 412) {
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
        for(int i = 0; i < array.size(); i++){
            Node toAdd = new Gson().fromJson(array.get(i), Node.class);
            nodesList.add(toAdd);
        }
        NodesRing.getInstance().setNodes(nodesList);
        //TODO: comunicare agli altri della mia entrata e aggiornarli
        System.out.println("Ring created -> " + NodesRing.getInstance().getNodes().toString());


    }

    void deleteNode(String id, String gatewayIP){
        Client client = Client.create();

        WebResource webResource = client
                .resource(gatewayIP);


        ClientResponse response = webResource.header("id",id)
                .delete(ClientResponse.class);

        if (response.getStatus() != 412 && response.getStatus() != 200) {
            throw new RuntimeException("Server unreachable or invalid parameters - "
                    + response.getStatus());
        }
        else if( response.getStatus() == 412) {
            throw new RuntimeException("Node already in list - "
                    + response.getStatus());
        }

        NodesRing.getInstance().removeNode(id);
        //TODO: comunicare agli altri della mia uscita

        String output = response.getEntity(String.class);
        System.out.println(output);

    }

    synchronized void nextToken(){
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        Node nextNode = new Node("", "", "");
        for(int i = 0; i < nodesCopy.size(); i++){
            if(myNode.getId() == nodesCopy.get(i).getId()){
                if(i +1 < nodesCopy.size() )
                    nextNode = nodesCopy.get(i+1);
                else{
                    nextNode = nodesCopy.get(0);
                }
            }
        }
        currentToken = nextNode.getId(); // TODO: setto il token id
        //TODO: Mando il token a tutti
    }
}
