package com.doggo.molly.breadboard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.activity.HomeActivity;
import com.doggo.molly.breadboard.model.AppAccessToken;
import com.doggo.molly.breadboard.model.AuthData;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Abhishek Vadnerkar
 */

public class SplashScreenFragment extends BaseFragment {

    private Handler handler;
    private Runnable runnable;
    private boolean activityVisible;
    private static final int SPLASH_TIME_OUT = 2000;
    private static final int REQUEST_CODE_AUTHORIZE_APP = 100;
    private boolean apiReady;
    private boolean timerReady;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public Call buildAPICall(int requestCode, Bundle params) {
        Call<AppAccessToken> call = getRestClient().getAuthService().getAuthToken(new AuthData(getActivity()));
        call.enqueue(new BaseRestCallback<AppAccessToken>() {
            @Override
            public boolean handleResponse(Call<AppAccessToken> call, Response<AppAccessToken> response) {
                if (response.isSuccessful()) {
                    getUserManager().setAppAuthorization(getActivity(), response.body());
                    apiReady = true;
                    proceedIfReady();
                }
                return false;
            }
        });
        return call;
    }

    @Override
    public void onResume() {
        super.onResume();
        activityVisible = true;
        doAPICall(REQUEST_CODE_AUTHORIZE_APP, null);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (activityVisible) {
                    timerReady = true;
                    proceedIfReady();
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

    private void proceedIfReady() {
        if (timerReady && apiReady) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
