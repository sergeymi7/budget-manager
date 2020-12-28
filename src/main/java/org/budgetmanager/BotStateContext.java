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
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
/*        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.SELECT_PROFIT_COST);
        }*/

        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_COST:
            case ASK_PROFIT:
            case SELECT_PROFIT_COST:
            case FILLED:
                return true;
            default:
                return false;
        }
    }


}





