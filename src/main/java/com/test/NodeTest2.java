package com.test;

import com.models.Node;
import com.node.NodeHandler;

import java.io.IOException;
import java.util.Random;

public class NodeTest2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Random rand = new Random();
        int randomNum = rand.nextInt((10000 - 1000) + 1) + 1000;
        NodeHandler nodeHandler1 = new NodeHandler();
        nodeHandler1.setGatewayIP("http://localhost:8080/restserver/gateway");

        nodeHandler1.setMyNode(new Node("Node"+randomNum, "localhost", randomNum));
        try {
            nodeHandler1.registerNode();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        nodeHandler1.runInputThread();
        nodeHandler1.runTokenThread();


    }
}
