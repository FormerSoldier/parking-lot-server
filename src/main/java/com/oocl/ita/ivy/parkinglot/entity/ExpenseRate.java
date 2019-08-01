package com.oocl.ita.ivy.parkinglot.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ExpenseRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private double expenseRate;

    @Column
    @NotNull
    private boolean status;
}
