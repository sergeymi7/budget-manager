package org.budgetmanager.handlers;

import static org.budgetmanager.enums.BotState.ASK_COST;
import static org.budgetmanager.enums.BotState.ASK_PROFIT;
import static org.budgetmanager.enums.BotState.FILL_EXPENSE;
import static org.budgetmanager.enums.BotState.SELECT_PROFIT_COST;

import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FillingProfitHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    public FillingProfitHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(SELECT_PROFIT_COST)) {

            if (message.getText().equals("Доход")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), ASK_PROFIT);
            }

            if (message.getText().equals("Расход")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), ASK_COST);
            }

        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return SELECT_PROFIT_COST;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BudgetDto budgetDto = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        BudgetDto newBudgetDto = new BudgetDto();

        if (botState.equals(ASK_PROFIT)) {
            replyToUser = new SendMessage(chatId, "Введите тип доход:");
            userDataCache.setUsersCurrentBotState(userId, FILL_EXPENSE);
            newBudgetDto.setName("Доход");
            userDataCache.saveUserProfileData(userId, newBudgetDto);
        }

        if (botState.equals(ASK_COST)) {
            replyToUser = new SendMessage(chatId, "Введите тип расход:");
            userDataCache.setUsersCurrentBotState(userId, FILL_EXPENSE);
            newBudgetDto.setName("Расход");
            userDataCache.saveUserProfileData(userId, newBudgetDto);
        }

        return replyToUser;
    }
}
