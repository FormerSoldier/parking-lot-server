package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.UserRepository;
import com.oocl.ita.ivy.parkinglot.service.ParkingBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-boys")
public class ParkingBoyController implements BaseController<ParkingBoy, String> {

    @Autowired
    private ParkingBoyService parkingBoyService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ParkingBoy save(ParkingBoy parkingBoy) {
        if(userRepository.findByUsername(parkingBoy.getUser().getUsername()).isPresent()){
            throw new BusinessException(BusinessExceptionType.USERNAME_EXISTS);
        }
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
        return parkingBoyService.update(parkingBoy);
    }

    @PutMapping("/{id}/parking-lots")
    public ParkingBoy setParkingLots(@PathVariable String id, @RequestBody  List<ParkingLot>parkingLots){
        return parkingBoyService.setParkingLotsByID(id,parkingLots);
    }

    @GetMapping("/{id}/parking-lots")
    public List<ParkingLot> getParkingLotsById(@PathVariable String id){
        return parkingBoyService.getParkingLotsByID(id);
    }


    @GetMapping("/boy")
    public ParkingBoy g(){
        ParkingBoy parkingBoy = parkingBoyService.getParkingBoyDTO();

        return parkingBoy;
    }


}
