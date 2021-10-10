package org.budgetmanager.handlers;

import static org.budgetmanager.enums.BotState.FILLED;
import static org.budgetmanager.enums.BotState.FILL_EXPENSE;
import static org.budgetmanager.enums.BotState.MARKET;
import static org.budgetmanager.enums.BotState.OTHER;

import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.budgetmanager.enums.ExpenseType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CostTypeHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    public CostTypeHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(FILL_EXPENSE)) {

            if (message.getText().equals("Магазины")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), MARKET);
            }
            if (message.getText().equals("Прочее")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), OTHER);
            }

        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return FILL_EXPENSE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BudgetDto budgetDto = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(MARKET)) {
            replyToUser = new SendMessage(chatId, "Введите доход:");
            userDataCache.setUsersCurrentBotState(userId, FILLED);
            budgetDto.setExpenseType(ExpenseType.MARKET);
            userDataCache.saveUserProfileData(userId, budgetDto);
        }

        if (botState.equals(OTHER)) {
            replyToUser = new SendMessage(chatId, "Введите расход:");
            userDataCache.setUsersCurrentBotState(userId, FILLED);
            budgetDto.setExpenseType(ExpenseType.OTHER);
            userDataCache.saveUserProfileData(userId, budgetDto);
        }

/*        if (botState.equals(FILLED)) {
            budgetDto.setValue(new BigDecimal(usersAnswer));
            userDataCache.saveUserProfileData(userId, budgetDto);
            userDataCache.setUsersCurrentBotState(userId, BotState.BEGIN);

            //replyToUser = new SendMessage(chatId, "Готово");
        }*/

        return replyToUser;
    }
}
