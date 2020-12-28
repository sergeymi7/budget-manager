package org.budgetmanager;

import lombok.extern.slf4j.Slf4j;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    private static int i = 0;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;

        switch (inputMsg) {
            case "Привет":
                botState = BotState.BEGIN;
                break;
            case "доход":
                botState = BotState.ASK_PROFIT;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        log.info("Итерация :{}, состояние: {},  with text: {}", i, botState, message.getText());

        userDataCache.setUsersCurrentBotState(userId, botState);

        SendMessage replyMessage = botStateContext.processInputMessage(botState, message);
        
       // replyMessage.setText("23123123");
       // replyMessage.setChatId((long) userId);

        i++;
        return replyMessage;
    }


}
