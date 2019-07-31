package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ExpenseRate;
import com.oocl.ita.ivy.parkinglot.repository.ExpenseRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseRateService {
    @Autowired
    private ExpenseRateRepository expenseRateRepository;

    public ExpenseRate getExpenseRate() {
        List<ExpenseRate> list = expenseRateRepository.findAll(Sort.by(Sort.Order.desc("id")));
        return list.size()>0?list.get(0):null;
    }

    public ExpenseRate addExpenseRate(ExpenseRate expenseRate){
        return expenseRateRepository.save(expenseRate);
    }
}
