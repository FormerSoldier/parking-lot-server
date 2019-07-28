package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum Gender {

    FEMALE("female"),
    MALE("male");

    private String status;

    Gender(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
