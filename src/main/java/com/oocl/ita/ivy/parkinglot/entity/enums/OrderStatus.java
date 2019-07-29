package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum OrderStatus {

    PROGRESSING("PROGRESSING"),
    UNPAID("UNPAID"),
    PAID("PAID"),
    FINISHED("FINISHED");

    public String getStatus() {
        return status;
    }

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

}
