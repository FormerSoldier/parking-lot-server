package com.oocl.ita.ivy.parkinglot.entity;

import java.util.concurrent.ConcurrentHashMap;

public class ProgressingDataStore {
    public static ConcurrentHashMap<String, ParkingOrder> dataStore;
    static {
        dataStore = new ConcurrentHashMap<>();
    }
}
