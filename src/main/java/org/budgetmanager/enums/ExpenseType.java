package org.budgetmanager.enums;

public enum ExpenseType {

    MARKET("Магазины"),
    FUEL("Бензин"),
    OTHER("Прочее"),
    FOOTBALL("Футбол");

    private String name;

    ExpenseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ExpenseType convert(String type) {

        for (ExpenseType expenseType : ExpenseType.values()) {
            if (expenseType.getName().equals(type)) {
                return expenseType;
            }
        }

        return null;
    }
}
