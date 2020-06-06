package com.gateway;

import PM10.Measurement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Stats {

    @XmlElement(name = "node")
    private List<Measurement> stats;

    private static Stats instance;

    private Stats() {
        stats = new ArrayList<Measurement>();
    }

    //singleton
    public synchronized static Stats getInstance() {
        if (instance == null)
            instance = new Stats();
        return instance;
    }

    public synchronized List<Measurement> getStatsList() {

        return new ArrayList<Measurement>(stats);

    }

    public synchronized void setStatsList(List<Measurement> stats) {
        this.stats = stats;
    }

    public synchronized boolean add(Measurement toAdd) {

        List<Measurement> statsCopy = getStatsList();
        statsCopy.add(toAdd);
        setStatsList(statsCopy);
        return true;

    }



    public synchronized List<Measurement> lastStats(int n) {

        List<Measurement> statsCopy = getStatsList();
        List<Measurement> lastStats = new ArrayList<Measurement>();
        for (int i = statsCopy.size(); i > statsCopy.size() - n; i--) {
            lastStats.add(statsCopy.get(i));
        }
        return lastStats;
    }

 // TODO: public synchronized int average(){}
 // TODO: public synchronized int stdDev(){

}

