package com.doggo.molly.breadboard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.activity.HomeActivity;

/**
 * @author Abhishek Vadnerkar
 */

public class SplashScreenFragment extends BaseFragment {

    private Handler handler;
    private Runnable runnable;
    private boolean activityVisible;
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        activityVisible = true;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (activityVisible) {
                    Intent startShopActivity = new Intent(getActivity(), HomeActivity.class);
                    startActivity(startShopActivity);
                    getActivity().finish();
                }
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onPause() {
        activityVisible = false;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
        super.onPause();
    }
}
