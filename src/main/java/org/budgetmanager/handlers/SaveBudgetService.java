package org.budgetmanager.handlers;

import java.time.LocalDateTime;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.domain.Budget;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.budgetmanager.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class SaveBudgetService {
    private UserDataCache userDataCache;
    private BudgetRepository repository;

    public SaveBudgetService(UserDataCache userDataCache, BudgetRepository repository) {
        this.userDataCache = userDataCache;
        this.repository = repository;
    }

    public void save(int userId, long chatId) {
        BudgetDto budgetDto = userDataCache.getUserProfileData(userId);

        Budget budget = new Budget();
        budget.setName(budgetDto.getName());
        budget.setPrice(budgetDto.getValue());
        budget.setCreated(LocalDateTime.now());
        budget.setType(budgetDto.getType());
        budget.setServiceDate(LocalDateTime.now()); //заменить на выбор в календаре может быть
        budget.setCreatedBy(userId);
        budget.setComment(budgetDto.getComment());
        repository.save(budget);

        userDataCache.setUsersCurrentBotState(userId, BotState.BEGIN);
    }
}
