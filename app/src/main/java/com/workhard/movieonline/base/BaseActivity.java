package com.workhard.movieonline.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.R;
import com.workhard.movieonline.handler.BackButtonHandlerInterface;
import com.workhard.movieonline.handler.OnBackClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by TrungKD on 2/14/2017.
 */
public abstract class BaseActivity extends AppCompatActivity implements BackButtonHandlerInterface {
    protected ArrayList<WeakReference<OnBackClickListener>> backClickListenersList = new ArrayList<>();
    private   Logger                                        logger                 = LoggerManager.getLogger(BaseActivity.class);
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        fragmentList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        if (backClickListenersList != null && backClickListenersList.size() > 1) {
            WeakReference<OnBackClickListener> weakRef             = backClickListenersList.get(0);
            OnBackClickListener                onBackClickListener = weakRef.get();
            if (onBackClickListener != null) {
                onBackClickListener.onBackClick();
            } else {
                popBackStack();
            }
        } else {
            super.onBackPressed();
            finish();
        }
    }

    /**
     * The function is used when user press back button.
     */
    public void popBackStack() {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            supportFragmentManager.popBackStack();
            if (fragmentList != null && fragmentList.size() > 0) {
                fragmentList.remove(0);
            }
        }
    }

    /**
     * The function is used when user chooses left menu or finish application.
     */
    public void removeAllFragment() {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        // Remove current all back press listener
        removeAllBackClickListener();
        fragmentList.clear();
    }

    public void addFragment(Fragment fragment, String tag) {
        addFragment(R.id.frame, fragment, tag, true);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    private void addFragment(int containerViewId, Fragment fragment, String tag, boolean isAddToBackStack) {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return;
        }

        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (fragmentTransaction == null) {
            return;
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (fragmentList != null && fragmentList.size() > 0) {
            fragmentTransaction.hide(fragmentList.get(0));
        }
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentList.add(0, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void addBackClickListener(OnBackClickListener onBackClickListener) {
        backClickListenersList.add(0, new WeakReference<>(onBackClickListener));
    }

    @Override
    public void removeBackClickListener(OnBackClickListener onBackClickListener) {
        for (Iterator<WeakReference<OnBackClickListener>> iterator = backClickListenersList.iterator();
             iterator.hasNext(); ) {
            WeakReference<OnBackClickListener> weakRef = iterator.next();
            if (weakRef.get() == onBackClickListener) {
                iterator.remove();
            }
        }
    }

    @Override
    public void removeAllBackClickListener() {
        for (Iterator<WeakReference<OnBackClickListener>> iterator = backClickListenersList.iterator();
             iterator.hasNext(); ) {
            WeakReference<OnBackClickListener> weakRef = iterator.next();
            iterator.remove();
        }
    }

    protected abstract int getLayoutID();
}