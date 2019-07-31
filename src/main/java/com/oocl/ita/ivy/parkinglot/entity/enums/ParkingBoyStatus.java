package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum ParkingBoyStatus {
    OPEN("OPEN"), // 开启枪弹
    STOP("STOP"); // 不抢单

    private String status;
    ParkingBoyStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
