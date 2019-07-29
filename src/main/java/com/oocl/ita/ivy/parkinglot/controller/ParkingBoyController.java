package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.service.ParkingBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyController {

    @Autowired
    private ParkingBoyService parkingBoyService;

    @GetMapping
    public List<ParkingBoy> findAllParkingBoys() {
        return parkingBoyService.findAllParkingBoys();
    }

    @GetMapping(path = "/{id}")
    public ParkingBoy find(@PathVariable String id) throws Exception {
        return parkingBoyService.find(id);
    }

    @PostMapping
    public ParkingBoy add(@RequestBody ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }

    @PutMapping
    public ParkingBoy update(@RequestBody ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable String id) {
        parkingBoyService.delete(id);
    }


}
