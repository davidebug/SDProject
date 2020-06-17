package com.node;

import PM10.Measurement;
import com.google.gson.Gson;

import com.grpc.TokenMessageOuterClass;

import java.util.HashMap;
import java.util.Map;

public class LocalToken {
    String nextIp;
    int nextPort;
    String currentId;
    String nextId;
    private static LocalToken instance;
    Map<String, Measurement> measurements;

    public synchronized String getNextId() {
        return nextId;
    }

    public synchronized void setNextId(String nextId) {
        this.nextId = nextId;
    }


    public synchronized static LocalToken getInstance() {
        if (instance == null)
            instance = new LocalToken();
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

    public synchronized void reset() {
        measurements.clear();
        currentId = "";
        nextId = "";
        nextIp = "";
        nextPort = 0;
    }

    public synchronized String getCurrentId() {
        return currentId;
    }

    public synchronized void setNextIp(String nextIp) {
        this.nextIp = nextIp;
    }

    public synchronized void setCurrentId(String currentId) {
        this.currentId = currentId;
        this.getInstance().notifyAll();
    }

    public synchronized int getNextPort() {
        return nextPort;
    }

    public synchronized void setNextPort(int nextPort) {
        this.nextPort = nextPort;
    }

    public synchronized Map<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> getMeasurementMessage() {
        Map<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> messageMap = new HashMap<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage>();
        for (Map.Entry<String, Measurement> entry : getMeasurements().entrySet()) {
            Measurement m = entry.getValue();
            if (m != null) {
                TokenMessageOuterClass.TokenMessage.MeasurementMessage measurement =
                        TokenMessageOuterClass.TokenMessage.MeasurementMessage.newBuilder().setValue(m.getValue()).setTimestamp(m.getTimestamp()).build();
                messageMap.put(entry.getKey(), measurement);
            }
        }
        return messageMap;
    }

    public synchronized void setMeasurementsFromMessage(Map<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> messageMap) {
        Map<String, Measurement> map = new HashMap<String, Measurement>();
        for (Map.Entry<String, TokenMessageOuterClass.TokenMessage.MeasurementMessage> entry : messageMap.entrySet()) {
            TokenMessageOuterClass.TokenMessage.MeasurementMessage m = entry.getValue();
            if (m != null) {
                Measurement measurement = new Measurement("", "", m.getValue(), m.getTimestamp());
                map.put(entry.getKey(), measurement);
            }
        }
        setMeasurements(map);
    }

    LocalToken() {
        measurements = new HashMap<String, Measurement>();
        nextIp = "";
        nextPort = 0;
        currentId = "";
    }
}
