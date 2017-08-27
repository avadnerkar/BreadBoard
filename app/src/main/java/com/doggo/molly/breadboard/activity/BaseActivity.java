package com.doggo.molly.breadboard.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.doggo.molly.breadboard.R;
import com.doggo.molly.breadboard.fragment.BaseFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Abhishek Vadnerkar
 */

public abstract class BaseActivity extends AppCompatActivity {

    private View loadingView;
    private Set<BaseFragment> loadingFragmentSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        This set keeps track of all the fragment which have requested loading. Whenever the contents
        of the set are changed, we make a call to updateLoadingView():
         */
        loadingFragmentSet = new HashSet<BaseFragment>() {
            @Override
            public boolean add(BaseFragment o) {
                boolean added = super.add(o);
                updateLoadingView();
                return added;
            }

            @Override
            public boolean remove(Object o) {
                boolean removed = super.remove(o);
                updateLoadingView();
                return removed;
            }

            @Override
            public void clear() {
                super.clear();
                updateLoadingView();
            }
        };
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(this);
        super.setContentView(addContentView(inflater.inflate(layoutResID, getApplicationRootView(), false)));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(addContentView(view));
    }

    protected ViewGroup getApplicationRootView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup activityRootView = findViewById(android.R.id.content);
        return (ViewGroup) inflater.inflate(R.layout.layout_activity_root, activityRootView, false);
    }

    /*
    Before calling the super for setContentView, we add the desired content (as passed from the BaseActivity
    subclass) to a root view containing the loader
     */
    private View addContentView(View contentView) {
        ViewGroup applicationRootView = getApplicationRootView();
        loadingView = applicationRootView.findViewById(R.id.loading_progress_bar);
        ProgressBar progressBar = applicationRootView.findViewById(R.id.loading_bar_loader_view);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        applicationRootView.addView(contentView, 0);
        updateLoadingView();
        return applicationRootView;
    }

    /*
    Public method for an instance of BaseFragment to call to start/stop loading.
     */
    public void setLoading(BaseFragment fragment, boolean loading) {
        if (loading) {
            loadingFragmentSet.add(fragment);
        } else {
            loadingFragmentSet.remove(fragment);
        }
    }

    /*
    The loader will only be hidden if there is no BaseFragment inside loadingFragmentSet
     */
    private void updateLoadingView() {
        loadingView.setVisibility(loadingFragmentSet.size() > 0 ? View.VISIBLE : View.GONE);
    }

}
