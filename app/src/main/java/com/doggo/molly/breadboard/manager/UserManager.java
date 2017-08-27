package com.doggo.molly.breadboard.manager;

import android.content.Context;

import com.doggo.molly.breadboard.model.AppAccessToken;

/**
 * @author Abhishek Vadnerkar
 */

public class UserManager {

    private static UserManager instance;
    private static StorageManager storageManager;
    private static final String APP_AUTHORIZATION = "com-doggo-molly-breadboard-APP_AUTH";

    private UserManager() {
        storageManager = new StorageManager();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setAppAuthorization(Context context, AppAccessToken appAccessToken) {
        storageManager.create(context, APP_AUTHORIZATION, appAccessToken);
    }

    public AppAccessToken getAppAuthorization(Context context) {
        return (AppAccessToken) storageManager.read(context, APP_AUTHORIZATION);
    }
}
