package com.doggo.molly.breadboard.api;

import android.content.Context;
import android.support.annotation.StringRes;

import com.doggo.molly.breadboard.BuildConfig;
import com.doggo.molly.breadboard.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Abhishek Vadnerkar
 */

public class RestClient {

    private Gson gson;
    private ApiService apiService;
    private AuthService authService;

    public RestClient(Context context) {
        /*
          Gson Builder is used to convert the JSON data to Java objects and vice-versa.
          Date format converts date from UTC to local time.
         */
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();
        apiService = (ApiService) createAPIService(context, ServiceType.API);
        authService = (AuthService) createAPIService(context, ServiceType.AUTH);
    }

    /**
     * The below API service is created when making call to backend.
     * HttpLoggingInterceptor is used to log the JSON Request and Responses only in debug mode.
     * GSON converter is added to convert the JSON to objects.
     *
     */

    private Object createAPIService(Context context, ServiceType type) {
        String baseUrl = context.getString(type.baseUrlId);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Headers.Builder builder = new Headers.Builder();
                Headers headers = builder.build();
                Request original = chain.request();
                Request request = original.newBuilder()
                        .headers(headers)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofitAPI = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofitAPI.create(type.apiServiceClass);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    private enum ServiceType {
        API(ApiService.class, R.string.base_url),
        AUTH(AuthService.class, R.string.base_auth_url);

        @StringRes private int baseUrlId;
        private Class apiServiceClass;

        ServiceType(Class apiServiceClass, int baseUrlId) {
            this.baseUrlId = baseUrlId;
            this.apiServiceClass = apiServiceClass;
        }
    }
}
