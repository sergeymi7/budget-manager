package org.budgetmanager;

import static org.budgetmanager.enums.BotState.FILLED;
import static org.budgetmanager.enums.BotState.FILL_EXPENSE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.budgetmanager.cache.UserDataCache;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private final List<String> expenseTypes = Arrays.asList("Магазины", "Прочее", "Футбол", "Бензин", "Развлечения",
                                                            "Одежда/обувь", "ЖКХ", "Интернет+ТВ", "Связь",
                                                            "Непредвиденные", "Годовые расходы");
    private final List<String> profitTypes = Arrays.asList("Зарплата", "Прочее");

    private static int i = 0;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            return processCallbackQuery(callbackQuery);
        }


        if (message != null && message.hasText()) {
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;

        switch (inputMsg) {
            case "Привет":
                botState = BotState.BEGIN;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        SendMessage replyToUser = new SendMessage(chatId, "Введите тип:");
        replyToUser.setReplyMarkup(getInlineKeyboardMarkup());
        botStateContext.processInputMessage(botState, message); //запускает поиск хэндлеров
       // replyMessage.setText("23123123");
       // replyMessage.setChatId((long) userId);

        return replyToUser;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> replyToUser = null;
        SendMessage sendMessage = null;

       // BotState botState = userDataCache.getUsersCurrentBotState(userId);
        BudgetDto newBudgetDto = new BudgetDto();

        String buttonData = buttonQuery.getData();

        Optional<String> findCostTypeOptional = expenseTypes.stream()
            .filter(type -> type.equals(buttonData))
            .findFirst();

        Optional<String> findProfitTypeOptional = profitTypes.stream()
            .filter(type -> type.equals(buttonData))
            .findFirst();

        if (buttonData.equals("Доход")) {
            sendMessage = new SendMessage(chatId, "Введите тип дохода:");
            sendMessage.setReplyMarkup(getProfitTypesKeyboardMarkup());

            userDataCache.setUsersCurrentBotState(userId, FILL_EXPENSE);
            newBudgetDto.setName("Доход");
            userDataCache.saveUserProfileData(userId, newBudgetDto);
        }

        if (buttonData.equals("Расход")) {
            sendMessage = new SendMessage(chatId, "Введите тип расхода:");
            sendMessage.setReplyMarkup(getCostTypesKeyboardMarkup());
            userDataCache.setUsersCurrentBotState(userId, FILL_EXPENSE);
            newBudgetDto.setName("Расход");
            userDataCache.saveUserProfileData(userId, newBudgetDto);
        }

        if (findCostTypeOptional.isPresent()) {
            String findType = findCostTypeOptional.get();

            BudgetDto budgetDto = userDataCache.getUserProfileData(userId);
            sendMessage = new SendMessage(chatId, "Введите расход:");
            userDataCache.setUsersCurrentBotState(userId, FILLED);
            budgetDto.setType(findType);
            userDataCache.saveUserProfileData(userId, budgetDto);
        }

        if (findProfitTypeOptional.isPresent()) {
            String findType = findProfitTypeOptional.get();

            BudgetDto budgetDto = userDataCache.getUserProfileData(userId);
            sendMessage = new SendMessage(chatId, "Введите доход:");
            userDataCache.setUsersCurrentBotState(userId, FILLED);
            budgetDto.setType(findType);
            userDataCache.saveUserProfileData(userId, budgetDto);
        }

        replyToUser = sendMessage;
        return replyToUser;
    }

    public InlineKeyboardMarkup getCostTypesKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonMarket = new InlineKeyboardButton().setText("Магазины");
        buttonMarket.setCallbackData("Магазины");
        InlineKeyboardButton buttonOther = new InlineKeyboardButton().setText("Прочее");
        buttonOther.setCallbackData("Прочее");
        InlineKeyboardButton buttonFootball= new InlineKeyboardButton().setText("Футбол");
        buttonFootball.setCallbackData("Футбол");
        InlineKeyboardButton buttonFuel = new InlineKeyboardButton().setText("Бензин");
        buttonFuel.setCallbackData("Бензин");


        InlineKeyboardButton buttonChill = new InlineKeyboardButton().setText("Развлечения");
        buttonChill.setCallbackData("Развлечения");
        InlineKeyboardButton buttonDress = new InlineKeyboardButton().setText("Одежда/обувь");
        buttonDress.setCallbackData("Одежда/обувь");
        InlineKeyboardButton buttonCommunal = new InlineKeyboardButton().setText("ЖКХ");
        buttonCommunal.setCallbackData("ЖКХ");
        InlineKeyboardButton buttonInternet = new InlineKeyboardButton().setText("Интернет");
        buttonInternet.setCallbackData("Интернет");

        InlineKeyboardButton buttonConnection= new InlineKeyboardButton().setText("Связь");
        buttonConnection.setCallbackData("Связь");
        InlineKeyboardButton buttonUnexpected = new InlineKeyboardButton().setText("Непредвиденные");
        buttonUnexpected.setCallbackData("Непредвиденные");
        InlineKeyboardButton buttonAnnualExpenses= new InlineKeyboardButton().setText("Годовые расходы");
        buttonAnnualExpenses.setCallbackData("Годовые расходы");

        List<InlineKeyboardButton> rowButton1 = Arrays.asList(buttonMarket, buttonOther, buttonFootball, buttonFuel);
        List<InlineKeyboardButton> rowButton2 = Arrays.asList(buttonChill, buttonDress, buttonCommunal, buttonInternet);
        List<InlineKeyboardButton> rowButton3 = Arrays.asList(buttonConnection, buttonUnexpected, buttonAnnualExpenses);

        List<List<InlineKeyboardButton>> listRowButton = Arrays.asList(rowButton1, rowButton2, rowButton3);
        markup.setKeyboard(listRowButton);

        return markup;
    }

    public InlineKeyboardMarkup getProfitTypesKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonSalary = new InlineKeyboardButton().setText("Зарплата");
        buttonSalary.setCallbackData("Зарплата");
        InlineKeyboardButton buttonOther = new InlineKeyboardButton().setText("Прочее");
        buttonOther.setCallbackData("Прочее");

        List<InlineKeyboardButton> rowButton1 = Arrays.asList(buttonSalary, buttonOther);
        List<List<InlineKeyboardButton>> listRowButton = Arrays.asList(rowButton1);
        markup.setKeyboard(listRowButton);

        return markup;
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
