package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum OrderStatus {

    PROGRESSING("PROGRESSING"),     // 待分配
    ACCEPT("ACCEPT"),               //已分配
    PARK("PARK"),                   // 已停车
    FETCHING("FETCH"),              // 待取车
    ACCEPT_FETCH("ACCEPT_FETCH"),   // 已分配取车
    PAID("PAID"),                   // 已取车
    //下面待定
    UNPAID("UNPAID"),
    FINISHED("FINISHED");

    public String getStatus() {
        return status;
    }

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

}
