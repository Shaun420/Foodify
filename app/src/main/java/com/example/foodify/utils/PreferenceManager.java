// PreferenceManager.java
package com.example.foodify.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for managing app preferences and user session data
 */
public class PreferenceManager {

    private static final String PREF_NAME = "FoodifyPrefs";
    private static final String KEY_USER_LOGGED_IN = "user_logged_in";
    private static final String KEY_PROFILE_COMPLETE = "profile_complete";
    private static final String KEY_FIRST_TIME_USER = "first_time_user";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Set first time user flag to true if not already set
        if (!sharedPreferences.contains(KEY_FIRST_TIME_USER)) {
            setFirstTimeUser(true);
        }
    }

    public boolean isFirstTimeUser() {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME_USER, true);
    }

    public void setFirstTimeUser(boolean isFirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_TIME_USER, isFirstTime);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isProfileComplete() {
        return sharedPreferences.getBoolean(KEY_PROFILE_COMPLETE, false);
    }

    public void setProfileComplete(boolean isComplete) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_PROFILE_COMPLETE, isComplete);
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void clearAllPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}