package com.doggo.molly.breadboard.manager;

/**
 * @author Abhishek Vadnerkar
 */

public class UserManager {

    private static UserManager instance;
    private static StorageManager storageManager;

    private UserManager() {
        storageManager = new StorageManager();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
}
