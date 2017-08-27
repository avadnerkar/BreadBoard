package com.doggo.molly.breadboard.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.doggo.molly.breadboard.BreadboardApplication;
import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.model.AppAccessToken;
import com.doggo.molly.breadboard.model.AuthData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


}
