package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query(value = "SELECT c.* FROM customer c,user_master u" +
            "    WHERE c.user_id = u.id" +
            "        AND username = :username",nativeQuery = true)
    Customer findByUsername(@Param("username") String username);
}
