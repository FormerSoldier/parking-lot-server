package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum ParkingBoyStatus {
    OPEN("OPEN"), // 开启枪弹
    BUSY("BUSY"), // 正在停车取车
    STOP("STOP"); // 不抢单

    private String status;
    ParkingBoyStatus(String status){
        this.status = status;
    }
}
