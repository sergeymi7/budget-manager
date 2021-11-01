package org.budgetmanager.handlers;

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
public class CommentFillHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private SaveBudgetService saveBudgetService;

    public CommentFillHandler(UserDataCache userDataCache, SaveBudgetService saveBudgetService) {
        this.userDataCache = userDataCache;
        this.saveBudgetService = saveBudgetService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILL_COMMENT;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        String usersAnswer = inputMsg.getText();
        BudgetDto budgetDto = userDataCache.getUserProfileData(userId);
        budgetDto.setComment(usersAnswer);
        userDataCache.saveUserProfileData(userId, budgetDto);

        saveBudgetService.save(userId, chatId);

        SendMessage replyToUser = new SendMessage(chatId, "Введите тип:");
        replyToUser.setReplyMarkup(getInlineKeyboardMarkup());

        return replyToUser;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonProfit = new InlineKeyboardButton().setText("Доход");
        buttonProfit.setCallbackData("Доход");
        InlineKeyboardButton buttonCost = new InlineKeyboardButton().setText("Расход");
        buttonCost.setCallbackData("Расход");

        List<InlineKeyboardButton> rowButton = Arrays.asList(buttonCost, buttonProfit);
        List<List<InlineKeyboardButton>> listRowButton = Arrays.asList(rowButton);
        markup.setKeyboard(listRowButton);

        return markup;
    }

}