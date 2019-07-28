package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.repository.ParkingBoyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingBoyService {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    public List<ParkingBoy> findAllParkingBoys() {
        return parkingBoyRepository.findAll();
    }

    public ParkingBoy find(String id) throws Exception {
        ParkingBoy parkingBoy = parkingBoyRepository.findById(id).orElse(null);
        if (parkingBoy == null) {
            throw new Exception("No such parking boy");
        }
        return parkingBoy;
    }

    public ParkingBoy save(ParkingBoy parkingBoy) {
        return parkingBoyRepository.save(parkingBoy);
    }

    public void delete(String id) {
        parkingBoyRepository.deleteById(id);
    }
}
