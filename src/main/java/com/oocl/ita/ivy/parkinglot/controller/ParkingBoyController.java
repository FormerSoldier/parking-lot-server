package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.service.ParkingBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyController implements BaseController<ParkingBoy, String> {

    @Autowired
    private ParkingBoyService parkingBoyService;


    @Override
    public ParkingBoy save(ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }

    @Override
    public List<ParkingBoy> findAll() {
        return parkingBoyService.findAll();
    }

    @Override
    public Page<ParkingBoy> findAll(Pageable pageable) {
        return parkingBoyService.findAll(pageable);
    }

    @Override
    public ParkingBoy findById(String s) throws Exception {
        return parkingBoyService.findById(s);
    }

    @Override
    public void deleteById(String s) {
        parkingBoyService.deleteById(s);
    }

    @Override
    public ParkingBoy update(ParkingBoy parkingBoy) {
        return parkingBoyService.save(parkingBoy);
    }
}
