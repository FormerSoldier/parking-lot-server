package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.service.ParkingBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingBoyController {

    @Autowired
    private ParkingBoyService parkingBoyService;

    @GetMapping(path = "/parkingboys")
    public List<ParkingBoy> findAllParkingBoys() {
        return parkingBoyService.findAllParkingBoys();
    }

    @GetMapping(path = "/parkingboys/{id}")
    public ParkingBoy find(@PathVariable String id) throws Exception {
        return parkingBoyService.find(id);
    }

    @PostMapping(path = "/parkingboys")
    public ParkingBoy add(@RequestBody ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }

    @PutMapping(path = "/parkingboys")
    public ParkingBoy update(@RequestBody ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }

    @DeleteMapping(path = "/parkingboys/{id}")
    public void delete(@PathVariable String id) {
        parkingBoyService.delete(id);
    }


}
