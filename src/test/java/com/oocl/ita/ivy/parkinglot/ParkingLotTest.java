package com.oocl.ita.ivy.parkinglot;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.service.ParkingLotService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertSame;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParkingLotTest {
    @Autowired
    private ParkingLotService parkingLotService;
    @Test
    public void should_add_park_lot_successfully_when_call_add_parking_lot(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("1号停车场");
        parkingLot.setCapacity(5);
        parkingLot.setAddress("东岸村");
        ParkingLot getParkingLot = parkingLotService.addParkingLot(parkingLot);
        assertSame(parkingLot,getParkingLot);
    }
    @Test
    public void should_return_different_parkinglot_when_call_modify_parking_lot(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId("1");
        parkingLot.setAddress("东岸村");
        parkingLot.setCapacity(10);
        parkingLot.setName("5号停车场");
        ParkingLot getSaveParkingLot=parkingLotService.addParkingLot(parkingLot);
        //change parkingLot name
        getSaveParkingLot.setName("6号停车场");
        ParkingLot changedParkingLot = parkingLotService.modifyParkingLot(getSaveParkingLot);
        assertNotSame(getSaveParkingLot,changedParkingLot);
    }
    @Test
    public void should_find_null_when_call_delete_parking_lot_by_id(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setAddress("东岸村");
        parkingLot.setCapacity(10);
        parkingLot.setName("5号停车场");
        parkingLotService.addParkingLot(parkingLot);
        parkingLotService.deleteParkingLotById(parkingLot.getId());
        assertSame(null,parkingLotService.getParkingLotById(parkingLot.getId()));
    }

}
