package org.budgetmanager.dto;

import java.math.BigDecimal;
import org.budgetmanager.enums.ExpenseType;

public class BudgetDto {

    private String name;
    private ExpenseType expenseType;
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }
}
