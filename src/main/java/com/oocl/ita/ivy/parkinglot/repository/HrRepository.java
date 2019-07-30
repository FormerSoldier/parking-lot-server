package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrRepository extends JpaRepository<User, Integer> {

    @Override
    @Query(value = "SELECT * FROM user_master WHERE delete_flag = 0 and roles = 'HR'",nativeQuery = true)
    Page<User> findAll(Pageable pageable);

}
