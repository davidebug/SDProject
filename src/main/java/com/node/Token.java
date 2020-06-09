package com.node;

import PM10.Measurement;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Token {
    String nextIp;
    String nextPort;
    Map<String, Measurement> measurements;

    public Map<String, Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getNextIp() {
        return nextIp;
    }

    public void setNextIp(String nextIp) {
        this.nextIp = nextIp;
    }

    public String getNextPort() {
        return nextPort;
    }

    public void setNextPort(String nextPort) {
        this.nextPort = nextPort;
    }



    Token(){
        measurements = new HashMap<String, Measurement>();
        nextIp = "";
        nextPort = "";
    }
}
