package com.doggo.molly.breadboard.model;

import android.content.Context;

import com.doggo.molly.breadboard.R;
import com.google.gson.annotations.SerializedName;

/**
 * @author Abhishek Vadnerkar
 */

public class AuthData {
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_secret")
    private String clientSecret;
    @SerializedName("grant_type")
    private String grantType;
    private String audience;

    public AuthData(Context context) {
        clientId = context.getString(R.string.client_id);
        clientSecret = context.getString(R.string.client_secret);
        grantType = context.getString(R.string.grant_type);
        audience = context.getString(R.string.audience);
    }
}
