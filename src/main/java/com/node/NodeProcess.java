package com.node;

import PM10.Buffer;
import PM10.PM10Simulator;

import java.io.IOException;

public class NodeProcess {

    public static void main(String[] args) throws IOException {

        try {
          //  NodeHandler nodeHandler = new NodeHandler();
          //  nodeHandler.runInputThread();
            PM10Simulator simulator = new PM10Simulator(StatsBuffer.getInstance());
            simulator.run();



        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
