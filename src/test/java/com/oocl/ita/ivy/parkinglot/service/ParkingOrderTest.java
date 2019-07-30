package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingOrderTest {
    @Autowired
    ParkingOrderService parkingOrderService;

    @Test
    public void s(){
        //ParkingOrderService parkingOrderService = new ParkingOrderService();
        ParkingOrder parkingOrder = parkingOrderService.save("1","12345");
        ParkingOrder parkingOrder1 = parkingOrderService.CustomerPark(parkingOrder);

        System.out.println(parkingOrder1);

    }
}
