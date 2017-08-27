package com.doggo.molly.breadboard;

import android.app.Application;

import com.doggo.molly.breadboard.api.RestClient;
import com.doggo.molly.breadboard.manager.UserManager;

/**
 * @author Abhishek Vadnerkar
 */

public class BreadboardApplication extends Application {

    private RestClient restClient;
    private UserManager userManager;

    @Override
    public void onCreate() {
        super.onCreate();
        userManager = UserManager.getInstance();
        restClient = new RestClient(this);
    }

    public RestClient getRestClient() {
        return restClient;
    }


}
