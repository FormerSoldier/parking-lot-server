package com.oocl.ita.ivy.parkinglot.service;

import com.itmuch.lightsecurity.jwt.JwtOperator;
import com.itmuch.lightsecurity.jwt.UserOperator;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.exception.UsernameOrPasswordIncorrectException;
import com.oocl.ita.ivy.parkinglot.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private JwtOperator jwtOperator;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOperator userOperator;


    public String login(User user) {
        return userRepository.findByUsername(user.getUsername())
                .filter(savedUser -> DigestUtils.sha256Hex(user.getPassword()).equals(savedUser.getPassword()))
                .map(savedUser -> jwtOperator.generateToken(savedUser))
                .orElseThrow(UsernameOrPasswordIncorrectException::new);
    }

    public User register(User user) {
        user.setRoles(Role.ADMIN.getRole());
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return userRepository.save(user);
    }

    public User register(User user, Role role){
        user.setRoles(role.getRole());
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return userRepository.save(user);
    }

    public com.itmuch.lightsecurity.jwt.User getUserPrincipal() {
        return userOperator.getUser();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(userOperator.getUser().getUsername()).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }
}
