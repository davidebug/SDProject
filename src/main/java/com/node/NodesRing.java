package com.node;

import com.models.Node;

import java.util.ArrayList;
import java.util.List;

public class NodesRing {
    private List<Node> nodes = new ArrayList<Node>();

    private static NodesRing instance;

    private NodesRing() {
        this.nodes = new ArrayList<Node>();
    }

    //singleton
    public synchronized static NodesRing getInstance(){
        if(instance==null)
            instance = new NodesRing();
        return instance;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public synchronized void addNode(Node node){
        List<Node> nodesCopy = nodes;
        nodesCopy.add(node);
        // TODO: sorting nodesCopy.sort();
        nodes = nodesCopy;
    }

    public synchronized void removeNode(String id){
        List<Node> nodesCopy = nodes;
        for(Node n : nodesCopy)
            if(n.getId().equals(id))
                nodesCopy.remove(n);
        // TODO: sorting nodesCopy.sort();
            nodes = nodesCopy;
    }


}
