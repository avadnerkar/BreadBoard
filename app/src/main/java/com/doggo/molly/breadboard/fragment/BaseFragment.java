package com.doggo.molly.breadboard.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.doggo.molly.breadboard.BreadboardApplication;
import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.activity.BaseActivity;
import com.doggo.molly.breadboard.api.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Abhishek Vadnerkar
 */

public abstract class BaseFragment extends Fragment {

    private Map<Integer, Call> activeCalls;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        The map that manages all active API calls (including custom requests made by a specific
        instance of BaseFragment). A call to setLoading() is made any time the size of the map changes.
         */
        activeCalls = new HashMap<Integer, Call>() {
            @Override
            public Call put(Integer key, Call value) {
                Call returnCall = super.put(key, value);
                setLoading(true);
                return returnCall;
            }

            @Override
            public Call remove(Object key) {
                Call call = super.remove(key);
                setLoading(false);
                return call;
            }

            @Override
            public void clear() {
                super.clear();
                setLoading(false);
            }
        };
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the base layout
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        View contentView = onCreateContentView(inflater, (ViewGroup) rootView, savedInstanceState);
        if (contentView != null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup viewGroup = rootView.findViewById(R.id.fe_content_view);
            viewGroup.addView(contentView, 0, params);
        }
        return rootView;
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /*
    Cancel all current calls and clear the activeCalls set (thus telling the activity that loading
    is no longer required for this fragment) when destroying the fragment
     */
    @Override
    public void onDestroyView() {
        for (Entry<Integer, Call> entry : activeCalls.entrySet()) {
            Call call = entry.getValue();
            if (call != null) {
                call.cancel();
            }
        }
        activeCalls.clear();
        super.onDestroyView();
    }

    /*
    Call this to make a network request
     */
    public final void doAPICall(int requestCode, Bundle params) {
        Call call = buildAPICall(requestCode, params);
        if (call != null) {
            activeCalls.put(call.hashCode(), call);
        }
    }

    /*
    Builds the network request to made
     */
    public Call buildAPICall(int requestCode, Bundle params) {
        return null;
    }

    /*
    Callback class that should be used for any API call made. This callback includes some base handling
    for specific error codes
     */
    abstract class BaseRestCallback<T> implements Callback<T> {
        @Override
        public final void onResponse(Call<T> call, Response<T> response) {
            boolean responseHandled = handleResponse(call, response);
            if (!responseHandled) {
                baseHandleResponse(call, response);
            }

            /*
            Remove call from activeCalls map after receiving response
             */
            activeCalls.remove(call.hashCode());
        }

        @Override
        public final void onFailure(Call<T> call, Throwable t) {
            handleFailure(call, t);

            /*
            Remove call from activeCalls map after receiving response
             */
            activeCalls.remove(call.hashCode());
        }

        /*
        Return true if response has been consumed entirely, false if base handling is required
         */
        public abstract boolean handleResponse(Call<T> call, Response<T> response);

        /*
        Base handling for specific error types
         */
        private void baseHandleResponse(Call<T> call, Response<T> response) {
            int code = response.code();
            if (code == HttpURLConnection.HTTP_UNAUTHORIZED || code == HttpURLConnection.HTTP_FORBIDDEN) {
                //User is either not logged in on server (expired auth token) or is not email verified
                handleUnauthorized(code);
            } else if (code >= HttpURLConnection.HTTP_BAD_REQUEST) {
                //Server error, show message sent from server
                showErrorMessage(extractErrorMessage(response));
            }
        }

        /*
        Get the error message from the server response. Calls getApiErrorMessage by default, but can
        be overridden to call getPaysafeApiErrorMessage instead if making a paysafe network call
         */
        protected String extractErrorMessage(Response<T> response) {
            return getApiErrorMessage(response);
        }

        private String getApiErrorMessage(Response<T> response) {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                return jObjError.getString("message");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                return getString(R.string.generic_error_message);
            }
        }

        /*
        Base handling for a call failure
         */
        void handleFailure(Call<T> call, Throwable t) {
            if (t.getCause() instanceof ConnectException) {
                showErrorMessage(getString(R.string.no_internet_message));
            }
        }

        /*
        By default, log out the current user and launch the login activity. Can be overridden in
        specific cases for different behaviour
         */
        protected void handleUnauthorized(int code) {
        }
    }

    /*
    This is called whenever a call is added or removed from the map of active calls. This method
    checks to see if the fragment needs to ask the activity to added or removed from the activity's set of
    loading fragments, i.e. if the map is cleared, or if it transitions from 0 calls to 1 call.
     */
    private void setLoading(boolean callAdded) {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            int numCalls = activeCalls.size();
            if (numCalls == 0) {
                baseActivity.setLoading(this, false);
            } else if (callAdded && numCalls == 1) {
                baseActivity.setLoading(this, true);
            }
        }
    }

    /*
        Show a snackbar containing an error message
         */
    protected void showErrorMessage(String message) {
        if (message != null) {
            Activity activity = getActivity();
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            Snackbar snackbar;
            snackbar = Snackbar.make(getSnackbarCoordinatorView(getActivity()), message, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);
            snackbarTextView.setMaxLines(4);
            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAction(R.string.close, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.White))
                    .show();
        }
    }

    private View getSnackbarCoordinatorView(Activity activity) {
        View coordinatorView = activity.findViewById(R.id.snackbar_coordinator);
        if (coordinatorView == null) {
            ViewGroup contentView = (activity.findViewById(android.R.id.content));
            coordinatorView = activity.getLayoutInflater().inflate(R.layout.snackbar_coordinator_view, contentView, true);
        }
        return coordinatorView;
    }

    protected RestClient getRestClient() {
        return getApplication().getRestClient();
    }

    protected BreadboardApplication getApplication() {
        return ((BreadboardApplication) getActivity().getApplication());
    }
}
