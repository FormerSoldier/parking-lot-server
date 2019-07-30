package com.oocl.ita.ivy.parkinglot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class ParkingLot {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @Column
    @NotNull
    private String name;
    @Column
    @NotNull
    private int capacity;
    @Column
    private String address;

    private int usedCapacity=0;
    @JsonIgnore
    public int getFreeCapacity(){
        return this.capacity-this.usedCapacity;
    }
}
