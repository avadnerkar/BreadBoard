package com.doggo.molly.breadboard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.model.AppAccessToken;

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
    public void onClick(View view) {
        AppAccessToken appAccessToken = getUserManager().getAppAuthorization(getActivity());
    }
}
