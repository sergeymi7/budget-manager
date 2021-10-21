package org.budgetmanager.cache;

import java.util.HashMap;
import java.util.Map;
import org.budgetmanager.dto.BudgetDto;
import org.budgetmanager.enums.BotState;
import org.springframework.stereotype.Component;

/**
 * In-memory cache.
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and user's profile data.
 */

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, BudgetDto> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.SELECT_PROFIT_COST;
        }

        return botState;
    }

    @Override
    public BudgetDto getUserProfileData(int userId) {
        return usersProfileData.get(userId);
    }

    @Override
    public void saveUserProfileData(int userId, BudgetDto userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }
}
