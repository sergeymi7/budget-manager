package org.budgetmanager.cache;

import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;

public interface DataCache {

    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    BudgetDto getUserProfileData(int userId);

    void saveUserProfileData(int userId, BudgetDto userProfileData);
}
