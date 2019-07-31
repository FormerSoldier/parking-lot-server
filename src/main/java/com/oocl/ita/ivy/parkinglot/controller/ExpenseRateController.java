package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.ExpenseRate;
import com.oocl.ita.ivy.parkinglot.service.ExpenseRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense-rate")
public class ExpenseRateController {
    @Autowired
    private ExpenseRateService expenseRateService;

    @GetMapping
    public ExpenseRate getExpenseRate(){
        return expenseRateService.getExpenseRate();
    }
    @PostMapping
    public ExpenseRate addExpenseRate(@RequestBody ExpenseRate expenseRate){
        System.out.println(expenseRate.getExpenseRate());
        return expenseRateService.addExpenseRate(expenseRate);
    }
}
