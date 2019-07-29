package com.oocl.ita.ivy.parkinglot.entity;

import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @NotNull
    @OneToOne
    private Customer customer;

    @NotNull
    private String carNo;

    private Date startTime;

    private Date endTime;

    @OneToOne
    private ParkingBoy parkingBoy;

    @OneToOne
    private ParkingLot parkingLot;

    private OrderStatus orderStatus;

    private double price;

    private Date submitTime;
}
