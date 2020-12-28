package org.budgetmanager.handlers;

import java.math.BigDecimal;
import org.budgetmanager.Treatment;
import org.budgetmanager.cache.UserDataCache;
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
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SELECT_PROFIT_COST)) {

            if (message.getText().equals("Доход")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_PROFIT);
            }

            if (message.getText().equals("Расход")) {
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_COST);
            }

        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SELECT_PROFIT_COST;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();


        String name = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;


        if (botState.equals(BotState.ASK_PROFIT)) {
            replyToUser = new SendMessage(chatId, "Введите доход:");
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLED);
            userDataCache.saveUserProfileData(userId, "Доход");
        }

        if (botState.equals(BotState.ASK_COST)) {
            replyToUser = new SendMessage(chatId, "Введите расход:");
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLED);
            userDataCache.saveUserProfileData(userId, "Расход");
        }

        if (botState.equals(BotState.FILLED)) {
            Treatment treatment = new Treatment();
            treatment.setName(name);
            treatment.setPrice(new BigDecimal(usersAnswer));

            replyToUser = new SendMessage(chatId, "Готово");

            userDataCache.setUsersCurrentBotState(userId, BotState.BEGIN);
        }

        return replyToUser;
    }
}
