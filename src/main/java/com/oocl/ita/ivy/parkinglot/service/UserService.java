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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements BaseService<User, String>{

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

    public User register(User user,String roleType) {
        user.setRoles(roleType);
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

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public User findById(String s) {
        return null;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    public void updateUserDeleteFlagById(Integer id){
        System.out.println(id);
        //userRepository.updateUserDeleteFlagById(id);
    }
}
