package com.oocl.ita.ivy.parkinglot.entity.enums;

public enum Role {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    PARKINGBOY("PARKINGBOY");
    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
