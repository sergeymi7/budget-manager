package org.budgetmanager.service;

import org.budgetmanager.domain.Budget;
import org.budgetmanager.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private final BudgetRepository repository;

    public BudgetService(BudgetRepository repository) {
        this.repository = repository;
    }

    public Budget getTreatment(int id) {
        return repository.getOne(id);

    }

    public void create(Budget budget) {
        repository.save(budget);
    }

}