package org.budgetmanager.cache;

import org.budgetmanager.enums.BotState;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    String getUserProfileData(int userId);

    void saveUserProfileData(int userId, String userProfileData);

/*    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);*/
}
