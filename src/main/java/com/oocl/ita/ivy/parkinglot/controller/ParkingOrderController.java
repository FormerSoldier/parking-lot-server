package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyVo;
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
        return orderService.customerPark(customerUsername, carNo);
    }

    @PostMapping("/fetch")
    public ParkingOrder fetch(@RequestBody String fetchId) throws Exception {
        System.out.println(fetchId);
        return orderService.customerFetch(fetchId);
    }

    @GetMapping("/parkingboys/parkorders")
    public List<ParkingBoyVo> getMySelfParkOrders(){
        return orderService.getMySelfParkOrders();
    }

    @GetMapping("/parkingboys/fetchorders")
    public List<ParkingBoyVo> getMySelfFetchOrder(){
        return orderService.getMySelfFetchOrder();
    }


    @GetMapping("/parking-boys/orders")
    public List<ParkingBoyVo> getAllParkingBoyVo(){
        return orderService.getMySelfAllOrders();
    }

    @GetMapping(params = {"page"})
    public Page<ParkingOrder> parkingOrders(@PageableDefault(size = 15) Pageable pageable){
        return orderService.findAll(pageable);
    }
}
