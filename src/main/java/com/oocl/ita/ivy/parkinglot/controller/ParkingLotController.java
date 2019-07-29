package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController implements BaseController<ParkingLot, String >{
    @Autowired
    private ParkingLotService parkingLotService;


    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        return parkingLotService.save(parkingLot);
    }

    @Override
    public List<ParkingLot> findAll() {
        return parkingLotService.findAll();
    }

    @Override
    public Page<ParkingLot> findAll(Pageable pageable) {
        return parkingLotService.findAll(pageable);
    }

    @Override
    public ParkingLot findById(String s) throws Exception {
        return parkingLotService.findById(s);
    }

    @Override
    public void deleteById(String s) {
        parkingLotService.deleteById(s);
    }

    @Override
    public ParkingLot update(ParkingLot parkingLot) {
        return parkingLotService.save(parkingLot);
    }
}
