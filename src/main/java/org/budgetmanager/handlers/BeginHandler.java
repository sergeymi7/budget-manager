package org.budgetmanager.handlers;

import lombok.extern.slf4j.Slf4j;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Slf4j
@Component
public class BeginHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public BeginHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.BEGIN;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = new SendMessage(chatId, "Введите тип:");
        userDataCache.setUsersCurrentBotState(userId, BotState.SELECT_PROFIT_COST);

        return replyToUser;
    }


}



