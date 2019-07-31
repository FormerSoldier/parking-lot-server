package com.oocl.ita.ivy.parkinglot.entity;

import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ParkingBoyVo {
    private String orderId;
    private String username;
    private String phone;
    private String carNo;
    private String number;
    private double price;
    private Date submitTime;
    private String parkingLotName;
    private Date fetchTime;
    private String parkParkingBoyName;
    private String fetchParkingBoyName;
    private OrderStatus orderStatus;
}
