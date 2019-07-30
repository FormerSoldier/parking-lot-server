package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrRepository extends JpaRepository<User, String> {


}
