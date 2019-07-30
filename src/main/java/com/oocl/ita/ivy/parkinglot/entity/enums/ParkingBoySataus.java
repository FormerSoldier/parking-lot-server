package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum ParkingBoySataus {
    OPEN("OPEN"),
    BUSY("BUSY"),
    STOP("STOP");

    private String status;
    ParkingBoySataus(String status){
        this.status = status;
    }
}
