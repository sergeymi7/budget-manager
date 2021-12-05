package org.budgetmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BudgetManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetManagerApplication.class, args);
    }
}
