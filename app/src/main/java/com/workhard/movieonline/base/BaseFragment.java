package com.workhard.movieonline.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.workhard.movieonline.MainActivity;
import com.workhard.movieonline.handler.BackButtonHandlerInterface;
import com.workhard.movieonline.handler.OnBackClickListener;
import com.workhard.movieonline.rest.BaseApi;
import com.workhard.movieonline.rest.IBaseApi;

/**
 * Created by TrungKD on 2/20/2017
 */
public abstract class BaseFragment extends Fragment implements OnBackClickListener {
    protected MainActivity               activity;
    // The fragment root view
    protected View                       rootView;
    // Handle back press
    protected BackButtonHandlerInterface backButtonHandler;

    protected IBaseApi baseApi = BaseApi.getClient().create(IBaseApi.class);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();

        backButtonHandler = (BackButtonHandlerInterface) activity;
        backButtonHandler.addBackClickListener(this);
    }

    /**
     * The function is still called in Android API 22.
     * It not even being called in Android API 23.
     *
     * @param activity
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.activity = (MainActivity) activity;

            backButtonHandler = (BackButtonHandlerInterface) this.activity;
            backButtonHandler.addBackClickListener(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backButtonHandler.removeBackClickListener(this);
        backButtonHandler = null;
    }

    protected void showActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    protected void hideActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public abstract int getLayoutID();
    public abstract void setupActionBar();
}