package com.node;

import PM10.Buffer;
import PM10.Measurement;

import java.util.ArrayList;
import java.util.List;

public class StatsBuffer implements Buffer {

    List<Measurement> stats;
    Measurement localStat;
    int slidingMaxSize = 12;
    private static StatsBuffer instance;

    private StatsBuffer() {
        this.stats = new ArrayList<Measurement>();
    }

    public synchronized static StatsBuffer getInstance(){
        if(instance==null)
            instance = new StatsBuffer();
        return instance;
    }

    public synchronized  void setLocalStat(Measurement localStat) {
        this.localStat = localStat;
    }

    @Override
    public synchronized void addMeasurement(Measurement m) {
        List<Measurement> statsCopy = stats;
        statsCopy.add(m);
        if(statsCopy.size() == slidingMaxSize) {
            localStat = getAvg();
            System.out.println("Local Stat ready --> " + localStat.toString());
            slide();
        }else {
            stats = statsCopy;
        }
        //System.out.println(m.toString());
    }

    public synchronized int getBufferSize(){
        return stats.size();
    }

    public synchronized Measurement getLocalStat(){
        return localStat;
    }

    public synchronized List<Measurement> getStats() {
        return stats;
    }

    public void setStats(List<Measurement> stats) {
        this.stats = stats;
    }

    public synchronized  void reset(){
        localStat = null;
    }

    public synchronized  void slide(){
        List<Measurement> statsCopy = stats;
        for(int i = 0; i < slidingMaxSize/2; i++)
            statsCopy.remove(i);
        stats = statsCopy;
    }

    public synchronized Measurement getAvg(){
        List<Measurement> statsCopy = getStats();
        long lastTimestamp = statsCopy.get(statsCopy.size()-1).getTimestamp();
        String lastType = statsCopy.get(statsCopy.size()-1).getType();
        String lastId = statsCopy.get(statsCopy.size()-1).getId();
        double sum = 0;
        double avg = 0;
        for(Measurement m : statsCopy){
            sum += m.getValue();
        }
        avg = sum / statsCopy.size();
        if(localStat != null)
            avg = (avg + localStat.getValue()) / 2;
        return new Measurement(lastId,lastType,avg,lastTimestamp);
    }
}
