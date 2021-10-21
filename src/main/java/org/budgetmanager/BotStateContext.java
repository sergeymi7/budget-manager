package org.budgetmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.budgetmanager.enums.BotState;
import org.budgetmanager.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Здесь собирается мапа хэндлеров и потом по BotState выбирается необходимый
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = messageHandlers.get(currentState);
        return currentMessageHandler.handle(message);
    }

}





