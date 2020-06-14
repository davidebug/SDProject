package com.node;

import PM10.Measurement;
import com.google.gson.Gson;

import com.grpc.TokenMessageOuterClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Token {
    String nextIp;
    int nextPort;
    String currentId;
    String nextId;

    public synchronized String getNextId() {
        return nextId;
    }

    public synchronized void setNextId(String nextId) {
        this.nextId = nextId;
    }

    private static Token instance;
    Map<String, Measurement> measurements;

    public synchronized static Token getInstance() {
        if (instance == null)
            instance = new Token();
        return instance;
    }


    public synchronized Map<String, Measurement> getMeasurements() {
        return measurements;
    }

    public synchronized void setMeasurements(Map<String, Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public synchronized String getNextIp() {
        return nextIp;
    }

    public synchronized String getCurrentId() {
        return currentId;
    }

    public synchronized void setNextIp(String nextIp) {
        this.nextIp = nextIp;
    }
    public synchronized void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public synchronized int getNextPort() {
        return nextPort;
    }

    public synchronized void setNextPort(int nextPort) {
        this.nextPort = nextPort;
    }

    public synchronized Map<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> getMeasurementMessage(){
        Map<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> messageMap = new HashMap<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage>();
        for(Map.Entry<String,Measurement> entry: getMeasurements().entrySet()){
            Measurement m = entry.getValue();
            if(m != null) {
                TokenMessageOuterClass.TokenMessage.MeasurementMessage measurement =
                        TokenMessageOuterClass.TokenMessage.MeasurementMessage.newBuilder().setValue(m.getValue()).setTimestamp(m.getTimestamp()).build();
                messageMap.put(entry.getKey(),measurement);
            }
        }
        return messageMap;
    }

    public synchronized  void setMeasurementsFromMessage(Map<String,TokenMessageOuterClass.TokenMessage.MeasurementMessage> messageMap){
        Map<String,Measurement> map = new HashMap<String,Measurement>();
        for(Map.Entry<String,TokenMessageOuterClass.TokenMessage.MeasurementMessage> entry: messageMap.entrySet()){
            TokenMessageOuterClass.TokenMessage.MeasurementMessage m = entry.getValue();
            if(m != null) {
                Measurement measurement = new Measurement("","",m.getValue(), m.getTimestamp());
                map.put(entry.getKey(),measurement);
            }
        }
        setMeasurements(map);
    }

    Token(){
        measurements = new HashMap<String, Measurement>();
        nextIp = "";
        nextPort = 0;
        currentId = "";
    }
}
