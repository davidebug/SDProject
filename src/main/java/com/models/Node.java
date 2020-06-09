package com.models;



import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;

import java.io.IOException;
import java.util.Scanner;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Node implements Comparable<Node>{

    String id;
    String IP;
    String port;

    public Node(String id, String IP, String port){
        this.id = id;
        this.IP = IP;
        this.port = port;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public int compareTo(Node o) {
        return this.getId().compareTo(o.getId());
    }
}
