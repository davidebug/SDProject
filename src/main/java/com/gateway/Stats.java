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

        List<Measurement> statsCopy = new ArrayList<>(getStatsList());
        statsCopy.add(toAdd);
        setStatsList(statsCopy);
        return true;

    }



    public synchronized List<Measurement> lastStats(int n) {

        List<Measurement> statsCopy = getStatsList();
        List<Measurement> lastStats = new ArrayList<Measurement>();
            for (int i = statsCopy.size()-1; i > (statsCopy.size() - n)-1; i--) {
                lastStats.add(statsCopy.get(i));
            }
        return lastStats;
    }

    public synchronized double average(int n){
        List<Measurement> lastStats = lastStats(n);
        if(lastStats.size() == 0)
            return 0;
        double sum = 0;
        for (int i = 0; i < lastStats.size(); i++) {
            sum += lastStats.get(i).getValue();
        }

        return sum/lastStats.size();
    }

    public synchronized double stdDev(int n) {
        List<Measurement> lastStats = lastStats(n);

        if(lastStats.size() == 0)
            return 0;
        double sum = 0;
        double newSum = 0;

        for (int i = 0; i < lastStats.size(); i++) {
            sum += lastStats.get(i).getValue();
        }
        double mean = sum/ lastStats.size();

        for (int j = 0; j < lastStats.size(); j++) {
            // put the calculation right in there
            newSum += ((lastStats.get(j).getValue() - mean) * (lastStats.get(j).getValue()));
        }
        double squaredDiffMean = newSum / lastStats.size();
        double standardDev = Math.sqrt(squaredDiffMean);

        return standardDev;
    }
}

