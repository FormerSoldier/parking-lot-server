package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.HrRepository;
import com.oocl.ita.ivy.parkinglot.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HrService {

    @Autowired
    private HrRepository hrRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public User save(User hr) {
        if (userRepository.findByUsername(hr.getUsername()).isPresent()) {
            throw new BusinessException(BusinessExceptionType.USERNAME_EXISTS);
        }

        return userService.register(hr, Role.HR);
    }

    public User update(User hr) {
        User oldHr = hrRepository.findById(hr.getId()).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        hr.setPassword(oldHr.getPassword());
        hr.setUsername(oldHr.getUsername());
        hr.setRoles(oldHr.getRoles());
        return hrRepository.save(hr);
    }

    public void deleteById(Integer id) {
        User hr = hrRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
        hr.setDeleteFlag(true);
        hrRepository.save(hr);
    }

    public User findById(Integer id) {
        return hrRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    public Page<User> findAll(Pageable pageable) {
        return hrRepository.findAll(pageable);
    }

}
