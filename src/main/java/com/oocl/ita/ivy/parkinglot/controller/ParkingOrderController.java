package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class ParkingOrderController {

    @Autowired
    private ParkingOrderService orderService;

    @PostMapping(path = "/{customerUsername}/car-no/{carNo}")
    public ParkingOrder save(@PathVariable String customerUsername, @PathVariable String carNo) {
        return orderService.save(customerUsername, carNo);
    }


    @PostMapping("/park")
    public ParkingOrder park(@RequestBody ParkingOrder parkingOrder){
        return orderService.CustomerPark(parkingOrder);
    }

}
