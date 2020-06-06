package com.gateway;

import com.models.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Nodes {

    @XmlElement(name="node")
    private List<Node> nodesList;

    private static Nodes instance;

    private Nodes() {
        nodesList = new ArrayList<Node>();
    }

    //singleton
    public synchronized static Nodes getInstance(){
        if(instance==null)
            instance = new Nodes();
        return instance;
    }

    public synchronized List<Node> getNodesList() {

        return new ArrayList<Node>(nodesList);

    }

    public synchronized void setNodesList(List<Node> nodesList) {
        this.nodesList = nodesList;
    }

    public synchronized boolean add(Node toAdd){

        List<Node> nodesCopy = getNodesList();
        for(Node n: nodesCopy){
            if(n.getId().toLowerCase().equals(toAdd.getId().toLowerCase())){
                return false;
            }
        }
        nodesCopy.add(toAdd);
        // TODO: nodesCopy.sort(Comparator.naturalOrder());
        setNodesList(nodesCopy);
        return true;

    }

    public synchronized boolean remove(String id){

        List<Node> nodesCopy = getNodesList();
        for(Node n: nodesCopy) {
            if (n.getId().toLowerCase().equals(id.toLowerCase())) {
                nodesCopy.remove(n);
                //TODO: sorting nodesCopy.sort();
                setNodesList(nodesCopy);
                return true;
            }
        }

        return false;

    }



    public Node getById(String id){

        List<Node> nodesCopy = getNodesList();

        for(Node n: nodesCopy)
            if(n.getId().toLowerCase().equals(id.toLowerCase()))
                return n;
        return null;
    }



}
