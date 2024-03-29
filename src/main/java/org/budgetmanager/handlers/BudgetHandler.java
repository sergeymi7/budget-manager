package org.budgetmanager.handlers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Slf4j
@Component
public class BudgetHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    public BudgetHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
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
        budgetDto.setValue(new BigDecimal(usersAnswer));
        userDataCache.saveUserProfileData(userId, budgetDto);

        SendMessage replyToUser = new SendMessage(chatId, "Комментарий?");
        replyToUser.setReplyMarkup(getCommentInlineKeyboardMarkup());
        return replyToUser;
    }

    public InlineKeyboardMarkup getCommentInlineKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonYesComment = new InlineKeyboardButton().setText("Да");
        buttonYesComment.setCallbackData("ДаКомментарий");
        InlineKeyboardButton buttonNoComment = new InlineKeyboardButton().setText("Нет");
        buttonNoComment.setCallbackData("НетКомментарий");

        List<InlineKeyboardButton> rowButton = Arrays.asList(buttonYesComment, buttonNoComment);
        List<List<InlineKeyboardButton>> listRowButton = Arrays.asList(rowButton);
        markup.setKeyboard(listRowButton);

        return markup;
    }
}