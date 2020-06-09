package com.node;

import com.models.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodesRing {
    private List<Node> nodes = new ArrayList<Node>();

    private static NodesRing instance;

    private NodesRing() {
        this.nodes = new ArrayList<Node>();
    }

    public synchronized static NodesRing getInstance() {
        if (instance == null)
            instance = new NodesRing();
        return instance;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public synchronized void setNodes(List<Node> nodes) {
        Collections.sort(nodes);
        this.nodes = nodes;
    }

    public synchronized void addNode(Node node) {
        List<Node> nodesCopy = nodes;
        nodesCopy.add(node);
        Collections.sort(nodesCopy);
        nodes = nodesCopy;
    }

    public synchronized void removeNode(String id) {
        List<Node> nodesCopy = nodes;
        for (Node n : nodesCopy)
            if (n.getId().equals(id))
                nodesCopy.remove(n);
        Collections.sort(nodesCopy);
        nodes = nodesCopy;
    }


}
