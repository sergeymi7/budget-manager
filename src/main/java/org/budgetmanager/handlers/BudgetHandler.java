package org.budgetmanager.handlers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.domain.Budget;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.budgetmanager.repository.BudgetRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class BudgetHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private BudgetRepository repository;

    public BudgetHandler(UserDataCache userDataCache, BudgetRepository repository) {
        this.userDataCache = userDataCache;
        this.repository = repository;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLED;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        String usersAnswer = inputMsg.getText();
        BudgetDto budgetDto = userDataCache.getUserProfileData(userId);

        Budget budget = new Budget();
        budget.setName(budgetDto.getName());
        budget.setPrice(new BigDecimal(usersAnswer));
        budget.setCreated(LocalDateTime.now());
        budget.setType(budgetDto.getType());
        budget.setServiceDate(LocalDateTime.now()); //заменить на выбор в календаре может быть
        budget.setCreatedBy(userId);
        repository.save(budget);

        SendMessage replyToUser = new SendMessage(chatId, "Готово");

        userDataCache.setUsersCurrentBotState(userId, BotState.BEGIN);

        return replyToUser;
    }


}



