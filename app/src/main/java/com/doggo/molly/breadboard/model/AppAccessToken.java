package com.doggo.molly.breadboard.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Abhishek Vadnerkar
 */

public class AppAccessToken implements Serializable {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("expires_in")
    long expiresIn;
    String scope;
    @SerializedName("token_type")
    String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
