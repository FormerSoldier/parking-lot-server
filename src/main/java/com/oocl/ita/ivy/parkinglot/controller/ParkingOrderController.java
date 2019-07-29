package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class ParkingOrderController {

    @Autowired
    private ParkingOrderService orderService;

    @PostMapping(path = "/{customerUsername}")
    public ParkingOrder save(@RequestBody ParkingOrder order, @PathVariable String customerUsername) {
        return orderService.save(order, customerUsername);
    }
}
