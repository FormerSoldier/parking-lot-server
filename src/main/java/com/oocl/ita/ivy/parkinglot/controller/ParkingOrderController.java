package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/fetch/{id}")
    public ParkingOrder fetch(@RequestBody ParkingOrder parkingOrder) throws Exception {
        return orderService.CustomerFetch(parkingOrder);
    }

    @GetMapping(params = {"page"})
    public Page<ParkingOrder> parkingOrders(@PageableDefault(size = 3) Pageable pageable){
        return orderService.findAll(pageable);
    }
}
