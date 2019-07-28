package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> getAllParkingLot() {
        return parkingLotRepository.findAll();
    }

    public ParkingLot addParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    public ParkingLot modifyParkingLot(ParkingLot parkingLot) {
        List<ParkingLot> list = new ArrayList<>();
        String id = parkingLot.getId();
        int capacity = parkingLot.getCapacity();
        String name = parkingLot.getName();
        ParkingLot getParkingLot = getParkingLotById(id);
        getParkingLot.setCapacity(capacity);
        getParkingLot.setAddress(name);
        return parkingLotRepository.save(parkingLot);
    }

    public ParkingLot getParkingLotById(String id) {
        Optional<ParkingLot> optional = parkingLotRepository.findById(id);
        ParkingLot parkingLot = null;
        if (optional.isPresent()) {
            parkingLot = optional.get();
        }
        return parkingLot;
    }

    public void deleteParkingLotById(String id) {
        parkingLotRepository.deleteById(id);
    }


}
