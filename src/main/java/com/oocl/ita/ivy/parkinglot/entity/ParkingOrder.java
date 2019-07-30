package com.oocl.ita.ivy.parkinglot.entity;

import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class ParkingOrder {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private int number;


    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    private String carNo;

    private Date startTime;

    private Date endTime;

    @OneToOne
    @JoinColumn(name = "parkingboy_id")
    private ParkingBoy parkingBoy;

    @OneToOne
    @JoinColumn(name = "parkinglot_id")
    private ParkingLot parkingLot;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private double price;

    private Date submitTime;
}
