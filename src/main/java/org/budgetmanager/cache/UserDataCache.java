package org.budgetmanager.cache;

import java.util.HashMap;
import java.util.Map;
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
    private Map<Integer, String> usersProfileData = new HashMap<>();
    //private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

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
    public String getUserProfileData(int userId) {
        return usersProfileData.get(userId);
    }

    @Override
    public void saveUserProfileData(int userId, String userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }

/*    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }*/
}
