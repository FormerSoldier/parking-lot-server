package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lot")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping
    public List<ParkingLot> getAllParkingLot() {
        return parkingLotService.getAllParkingLot();
    }

    @PostMapping
    public ParkingLot addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.addParkingLot(parkingLot);
    }
    @PutMapping
    public ParkingLot modifyParkingLot(@RequestBody ParkingLot parkingLot){
        return parkingLotService.modifyParkingLot(parkingLot);
    }
    @DeleteMapping("/{id}")
    public void deleteParkingLotById(@PathVariable String id){
        parkingLotService.deleteParkingLotById(id);
    }
}
