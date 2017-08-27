package com.doggo.molly.breadboard.api;

import com.doggo.molly.breadboard.model.AuthData;
import com.doggo.molly.breadboard.model.AppAccessToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Abhishek Vadnerkar
 */

public interface AuthService {
    @POST("oauth/token")
    Call<AppAccessToken> getAuthToken(@Body AuthData authData);
}
