package com.doggo.molly.breadboard.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Abhishek Vadnerkar
 */

public class AppAccessToken {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("expires_in")
    long expiresIn;
    String scope;
    @SerializedName("token_type")
    String tokenType;
}
