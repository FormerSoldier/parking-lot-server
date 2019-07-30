package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService implements BaseService<ParkingLot, String> {
    @Autowired
    private ParkingLotRepository parkingLotRepository;


    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public List<ParkingLot> findAll() {
        return parkingLotRepository.findAll();
    }

    @Override
    public void deleteById(String s) {
        parkingLotRepository.deleteById(s);
    }

    @Override
    public ParkingLot findById(String s) {
        return parkingLotRepository.findById(s).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    @Override
    public Page<ParkingLot> findAll(Pageable pageable) {
        return parkingLotRepository.findAll(pageable);
    }


    public void addUsedCapacity(String id){
        parkingLotRepository.addUsedCapacity(id);
    }
}
