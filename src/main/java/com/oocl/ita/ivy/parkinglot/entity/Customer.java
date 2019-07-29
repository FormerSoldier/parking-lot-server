package com.oocl.ita.ivy.parkinglot.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @NotNull
    private String phone;

    private int point;

    private boolean isVIP;

    private int times;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
