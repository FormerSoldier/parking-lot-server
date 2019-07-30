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

    @PostMapping(path = "/{customerUsername}/car-no/{carNo}")
    public ParkingOrder save(@PathVariable String customerUsername, @PathVariable String carNo) {
        return orderService.customerPark(customerUsername, carNo);
    }

    @PostMapping("/fetch/{id}")
    public ParkingOrder fetch(@PathVariable("id") String fetchId) throws Exception {
        System.out.println(fetchId);
        return orderService.customerFetch(fetchId);
    }
    @GetMapping("/{id}")
    public ParkingOrder findById(@PathVariable String id){
        return orderService.findById(id);
    }
}
