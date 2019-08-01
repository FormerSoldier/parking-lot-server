package com.oocl.ita.ivy.parkinglot.service;

import com.itmuch.lightsecurity.jwt.UserOperator;
import com.oocl.ita.ivy.parkinglot.entity.*;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.ParkingBoyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionException;
import org.springframework.hateoas.alps.Doc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingBoyService implements BaseService<ParkingBoy, String> {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    UserOperator userOperator;

    @Override
    public ParkingBoy save(ParkingBoy parkingBoy) {
        User user=userService.register(parkingBoy.getUser(), Role.PARKINGBOY);
        parkingBoy.setUser(user);
        return parkingBoyRepository.save(parkingBoy);
    }

    @Override
    public List<ParkingBoy> findAll() {
        return parkingBoyRepository.findAll();
    }

    @Override
    public void deleteById(String s) {
        ParkingBoy parkingBoy=parkingBoyRepository.findById(s).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        parkingBoy.getUser().setDeleteFlag(true);
        parkingBoyRepository.save(parkingBoy);
    }

    @Override
    public ParkingBoy findById(String s) {
        return parkingBoyRepository.findById(s).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    @Override
    public Page<ParkingBoy> findAll(Pageable pageable) {
        return parkingBoyRepository.findAll(pageable);
    }


    public ParkingBoy setParkingLotsByID(String id, List<ParkingLot> parkingLots) {
        ParkingBoy parkingBoy=parkingBoyRepository.findById(id).orElseThrow(() ->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        parkingBoy.setParkingLotList(parkingLots);
        return parkingBoyRepository.saveAndFlush(parkingBoy);
    }

    public List<ParkingLot> getParkingLotsByID(String id) {
        return parkingBoyRepository.findById(id).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT)).getParkingLotList();
    }

    public ParkingBoy update(ParkingBoy parkingBoy) {
        ParkingBoy oldParkingBoy=parkingBoyRepository.findById(parkingBoy.getId()).orElseThrow(() ->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        parkingBoy.setUser(oldParkingBoy.getUser());
        return parkingBoyRepository.save(parkingBoy);
    }


    public ParkingBoy getCurrentParkingBoy() {
        Integer id = userOperator.getUser().getId();
        return findByUserId(id);
    }

    private ParkingBoy findByUserId(Integer id) {
        return parkingBoyRepository.findParkingBoyByUserId(id);
    }


    public ParkingBoy getParkingBoyInSomeStatus(String status){
        return parkingBoyRepository.getParkingBoyInSomeStatus(status);
    }

    public List<ParkingBoy> getParkingBoyByParkingLot(String id, String status) {
        return parkingBoyRepository.getParkingBoyByParkingLot(id, status);
    }

    public ParkingBoy changeParkingBoyStatus(String id) {
        ParkingBoy parkingBoy=parkingBoyRepository.findById(id).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        parkingBoy.setStatus(parkingBoy.getStatus()==ParkingBoyStatus.OPEN?ParkingBoyStatus.STOP:ParkingBoyStatus.OPEN);
        return parkingBoyRepository.save(parkingBoy);
    }

    public ParkingBoy upgradeToManager(String id) {
        ParkingBoy manager = parkingBoyRepository.findById(id).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        manager.setManager(true);
        List<String> roles = manager.getUser().getRoles();
        roles.add(String.valueOf(Role.MANAGER));
        manager.getUser().setRoles(roles);
        ParkingBoy oldHeader = parkingBoyRepository.findManagerBySubordinate(manager.getId());
        if (oldHeader != null) {
            oldHeader.getParkingBoys().remove(manager);
            parkingBoyRepository.save(oldHeader);
        }
        return parkingBoyRepository.save(manager);
    }

    public ParkingBoy addParkingBoyForManager(String id, List<ParkingBoy> parkingBoys) {
        ParkingBoy manager = parkingBoyRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        if (parkingBoys.size() == 0) {
            manager.setParkingBoys(null);
            return parkingBoyRepository.saveAndFlush(manager);
        } else {
            List<ParkingBoy> chooseBoys = new ArrayList<>();
            for (ParkingBoy item : parkingBoys) {
                chooseBoys.add(parkingBoyRepository.findById(item.getId()).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT)));
            }
            manager.setParkingBoys(chooseBoys);
            return parkingBoyRepository.saveAndFlush(manager);
        }
    }

    public ParkingBoy degradeToParkingBoy(String id) {
        ParkingBoy manager = parkingBoyRepository.findById(id).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        manager.setManager(false);
        manager.getUser().setRoles(String.valueOf(Role.PARKINGBOY));
        manager.setParkingBoys(null);
        return parkingBoyRepository.save(manager);
    }

    public List<ParkingBoy> getSubordinatesByManagerId(String managerId) {
        ParkingBoy manager = parkingBoyRepository.findById(managerId).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        if (manager.getParkingBoys() == null)
            return null;
        return manager.getParkingBoys();
    }

    public List<ParkingBoy> findLowerParkingBoy() {
        List<ParkingBoy> chooseParkingBoysLists = parkingBoyRepository.findNotInManagedParkingBoy();
        if (chooseParkingBoysLists.size() != 0) {
            chooseParkingBoysLists = chooseParkingBoysLists.stream().filter(item -> !item.isManager()).collect(Collectors.toList());
        }
        return chooseParkingBoysLists;
    }

    public List<ParkingBoy> getSubordinatesByUserId(Integer userId) {
        ParkingBoy parkingBoy = Optional.of(parkingBoyRepository.findByUserId(userId)).orElseThrow(()->new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        return getSubordinatesByManagerId(parkingBoy.getId());
    }

    public void raiseSalary(double sum) {
        List<ParkingBoy> parkingBoys = parkingBoyRepository.findAllByDeleteFlag();
        double points = 0;
        for (ParkingBoy p : parkingBoys) {
            points += p.getOrderNumInClose() + p.getOrderNumInOpen()*5;
        }
        for (ParkingBoy parkingBoy : parkingBoys) {
            double pbPonit = parkingBoy.getOrderNumInClose() + parkingBoy.getOrderNumInOpen()*5;
            double newSalary = parkingBoy.getSalary() + (pbPonit/points) * sum;
            newSalary = Math.floor(newSalary*100)/100;
            parkingBoy.setSalary(newSalary);
            parkingBoyRepository.save(parkingBoy);
        }
    }
}
