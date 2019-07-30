package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-orders")
public class UserOrderController {

    @Autowired
    private UserOrderService userOrderService;

    @GetMapping(path = "{customerId}/fetchable")
    List<ParkingOrder> getOrdersByCustomerIdByFetchable(@PathVariable String customerId) {
        return userOrderService.getOrdersByCustomerIdByFetchable(customerId);
    }

    @GetMapping(path = "{customerId}/all")
    List<ParkingOrder> getOrdersByCustomerIdByAll(@PathVariable String customerId) {
        return userOrderService.getOrdersByCustomerIdByAll(customerId);
    }

}
