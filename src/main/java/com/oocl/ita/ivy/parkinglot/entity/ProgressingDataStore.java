package com.oocl.ita.ivy.parkinglot.entity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProgressingDataStore {
    public static Queue<String> queue;
    static {
        queue = new ConcurrentLinkedQueue<>();
    }
}
