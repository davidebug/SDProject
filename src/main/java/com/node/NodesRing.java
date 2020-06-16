package com.node;

import com.models.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodesRing {

    private Node myNode;
    private String status;

    public synchronized Node getMyNode() {
        return myNode;
    }

    public synchronized void setMyNode(Node myNode) {
        this.myNode = myNode;
    }

    private List<Node> nodes = new ArrayList<Node>();

    public synchronized String getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }

    private static NodesRing instance;

    private NodesRing() {
        this.nodes = new ArrayList<Node>();
        status = "online";
        myNode = new Node("", "", 0);
    }

    public synchronized static NodesRing getInstance() {
        if (instance == null)
            instance = new NodesRing();
        return instance;
    }
    public synchronized List<Node> getNodes() {
        return nodes;
    }

    public synchronized void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public synchronized boolean addNode(Node toAdd) {
        List<Node> nodesCopy = new ArrayList<>(getNodes());
        for(Node n: nodesCopy){
            if(n.getId().toLowerCase().equals(toAdd.getId().toLowerCase())){
                return false;
            }
        }
        nodesCopy.add(toAdd);
        Collections.sort(nodesCopy);
        setNodes(nodesCopy);
        return true;
    }

    public synchronized boolean removeNode(String id) {
        List<Node> nodesCopy = new ArrayList<>(getNodes());
        for(Node n: nodesCopy) {
            if (n.getId().toLowerCase().equals(id.toLowerCase())) {
                nodesCopy.remove(n);
                Collections.sort(nodesCopy);
                setNodes(nodesCopy);
                return true;
            }
        }

        return false;
    }

    public synchronized Node getNextNode(String currentId){
        List<Node> nodesCopy = NodesRing.getInstance().getNodes();
        Node nextNode = new Node("", "", 0);
        for (int i = 0; i < nodesCopy.size(); i++) {
            if (currentId.equals(nodesCopy.get(i).getId())) {
                int nextIndex = (i + 1) % nodesCopy.size();
                nextNode = nodesCopy.get(nextIndex);
            }
        }
        return nextNode;
    }


}
