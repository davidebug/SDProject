package com.node;

import PM10.Buffer;
import PM10.PM10Simulator;
import com.models.Node;

import java.io.IOException;
import java.util.Scanner;

public class NodeProcess {

    public static void main(String[] args) throws IOException {

            NodeHandler nodeHandler = new NodeHandler();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter an id: ");
            String id = scanner.next();
            System.out.print("Enter your port: ");
            int port = scanner.nextInt();
            // System.out.print("Enter your ip: ");
            // String myIP = scanner.next();
            String myIP = "localhost";
            //System.out.print("Enter gateway's IP: ");
            //gatewayIP = scanner.next();
            nodeHandler.setGatewayIP("http://localhost:8080/restserver/gateway");
            nodeHandler.setMyNode(new Node(id, myIP, port));
            try {
                nodeHandler.registerNode(id, port, myIP);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            nodeHandler.runInputThread();
            nodeHandler.runTokenThread();


    }

}
