package com.workhard.movieonline.screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.workhard.movieonline.R;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.util.Util;

/**
 * Created by TrungKD on 2/20/2017.
 */
public class SettingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), null);
        showActionBar();
        initViews();
        setupActionBar();
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupActionBar();
        }
    }

    private void initViews() {
        // Report problem
        LinearLayout llReportProblem = (LinearLayout) rootView.findViewById(R.id.ll_report_problem);
        llReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMail("text@gmail.com", "[MovieOnline] Report problem", "Test content");
            }
        });

        // Invite friends
        LinearLayout llInviteFriend = (LinearLayout) rootView.findViewById(R.id.ll_invite_friends);

        // About us
        LinearLayout llAboutUs = (LinearLayout) rootView.findViewById(R.id.ll_about_us);
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openWebBrowser(activity, "https://www.google.com");
            }
        });

        // Maybe need
        LinearLayout llMaybeNeed = (LinearLayout) rootView.findViewById(R.id.ll_maybe_need);
        llMaybeNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openWebBrowser(activity, "https://www.youtube.com/");
            }
        });


        // Join fb fan page.
        LinearLayout llJoinFanPage = (LinearLayout) rootView.findViewById(R.id.ll_join_fan_page);
        llJoinFanPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openWebBrowser(activity, "https://www.facebook.com");
            }
        });


        // Follow twitter
        LinearLayout llFollowTwitter = (LinearLayout) rootView.findViewById(R.id.ll_follow_twitter);
        llFollowTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openWebBrowser(activity, "https://twitter.com");
            }
        });

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    public boolean onBackClick() {
        return false;
    }

    private void openMail(String email, String mailSubject, String mailBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(new Uri.Builder().scheme("mailto").build());
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mailBody);
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setupActionBar() {
        activity.enableNavIcon();
    }
}
