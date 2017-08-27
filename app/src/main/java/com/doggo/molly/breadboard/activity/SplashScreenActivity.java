package com.doggo.molly.breadboard.activity;

import android.os.Bundle;
import android.view.Window;

import com.doggo.molly.breadboard.R;

/**
 * @author Abhishek Vadnerkar
 */

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
