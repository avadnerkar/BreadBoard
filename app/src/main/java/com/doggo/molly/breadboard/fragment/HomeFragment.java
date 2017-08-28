package com.doggo.molly.breadboard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Abhishek Vadnerkar
 */

public class HomeFragment extends BaseFragment implements OnClickListener {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button button = view.findViewById(R.id.button_make_request);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public Call buildAPICall(int requestCode, Bundle params) {
        Call<List<Movie>> call = getRestClient().getApiService().getMovies();
        call.enqueue(new BaseRestCallback<List<Movie>>() {
            @Override
            public boolean handleResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    return true;
                }
                return false;
            }
        });
        return super.buildAPICall(requestCode, params);
    }

    @Override
    public void onClick(View view) {
        doAPICall(101, null);
    }
}
