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

    @Override
    public synchronized void addMeasurement(Measurement m) {
        List<Measurement> statsCopy = stats;
        if(statsCopy.size() == slidingMaxSize) {
            System.out.println("ARRIVATO A 12, calcolo AVG");
            localStat = getAvg();
            this.getInstance().notifyAll();
        }
        statsCopy.add(m);
        stats = statsCopy;
        //System.out.println(m.toString());
    }

    public synchronized int getBufferSize(){
        return stats.size();
    }

    public synchronized Measurement getLocalStat(){
        return localStat;
    }

    public synchronized  void reset(){
        List<Measurement> statsCopy = stats;
        for(int i = 0; i < slidingMaxSize/2; i++)
            statsCopy.remove(i);
        stats = statsCopy;
        localStat = null;
    }

    public synchronized Measurement getAvg(){
        List<Measurement> statsCopy = stats;
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
