package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query(value = "SELECT c.* FROM customer c,user_master u" +
            "    WHERE c.user_id = u.id" +
            "        AND username = :username",nativeQuery = true)
    Customer findByUsername(@Param("username") String username);

    @Override
    @Query(value="SELECT * FROM customer INNER JOIN user_master ON user_master.id = :id AND user_master.id = customer.user_id",nativeQuery = true)
    Optional<Customer> findById(@Param("id") String id);
}
