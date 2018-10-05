package com.workhard.movieonline;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.base.BaseActivity;
import com.workhard.movieonline.listener.DeleteListener;
import com.workhard.movieonline.rest.ApiInitialization;
import com.workhard.movieonline.rest.BaseApi;
import com.workhard.movieonline.rest.IBaseApi;
import com.workhard.movieonline.rest.ILocationApi;
import com.workhard.movieonline.rest.LocationApi;
import com.workhard.movieonline.rest.model.BaseListResponse;
import com.workhard.movieonline.rest.model.ResponseUserLocation;
import com.workhard.movieonline.screen.FavouriteFragment;
import com.workhard.movieonline.screen.GenresFragment;
import com.workhard.movieonline.screen.HistoryFragment;
import com.workhard.movieonline.screen.HomeFragment;
import com.workhard.movieonline.screen.SettingFragment;
import com.workhard.movieonline.util.Util;
import com.workhard.movieonline.widget.LoadingDialog;

import java.security.MessageDigest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    // Logger to show log
    private Logger logger = LoggerManager.getLogger();

    private NavigationView navigationView;
    private DrawerLayout   drawer;
    private Toolbar        toolbar;
    private View           decorView;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME      = "home";
    private static final String TAG_GENRES    = "genres";
    private static final String TAG_HISTORY   = "history";
    private static final String TAG_FAVOURITE = "favourite";
    private static final String TAG_SETTINGS  = "settings";
    private static final String TAG_TWITTER   = "twitter";
    private static final String TAG_FACEBOOK  = "facebook";

    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = false;
    private Handler mHandler;

    private Resources resources;

    private InterstitialAd mInterstitialAd;
    private AdView         avBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resources = getResources();
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        decorView = getWindow().getDecorView();

        // load toolbar titles from string resources
        activityTitles = resources.getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        enableNavIcon();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadMenuFragment();
        }

        loadBannerAd();
        loadFullAd();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // showing dot next to notifications label
        // navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadMenuFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // Remove all fragment
                removeAllFragment();

                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                addFragment(fragment, CURRENT_TAG);
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                return new HomeFragment();
            case 1:
                // genres
                return new GenresFragment();
            case 2:
                // history
                return new HistoryFragment();
            case 3:
                // favourite
                return new FavouriteFragment();
            case 4:
                // setting
                return new SettingFragment();
            case 5:
                // twitter
                // open web browser

            case 6:
                // facebook
                // open web browser

            default:
                return new HomeFragment();
        }
    }

    public void setToolbarTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            View     customView = supportActionBar.getCustomView();
            TextView tvTitle    = (TextView) customView.findViewById(R.id.screen_title);
            tvTitle.setText(title);
            tvTitle.setSelected(true);
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_genres:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_GENRES;
                        break;
                    case R.id.nav_history:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_favourite:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_FAVOURITE;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_twitter:
                        drawer.closeDrawers();
                        Util.openWebBrowser(MainActivity.this, "https://twitter.com");
                        return false;

                    case R.id.nav_facebook:
                        drawer.closeDrawers();
                        Util.openWebBrowser(MainActivity.this, "https://facebook.com");
                        return false;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                menuItem.setChecked(true);

                loadMenuFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadMenuFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void finish() {
        removeAllFragment();
        super.finish();
    }

    public void enableLandscapeMode() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void enablePortraitMode() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setImmersiveMode(boolean isEnable) {
        if (isEnable) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    /**
     * Refer http://stackoverflow.com/posts/36242114/revisions
     */
    private void hideSystemUI() {
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                drawer.setFitsSystemWindows(false);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (Build.VERSION.SDK_INT >= 19) {
                decorView.setSystemUiVisibility(
                        // View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else if (Build.VERSION.SDK_INT >= 16) {
                decorView.setSystemUiVisibility(
                        // View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
            toolbar.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (NullPointerException e) {
            logger.e("NullPointerException: " + e);
        }
    }

    /**
     * Refer http://stackoverflow.com/posts/36242114/revisions
     */
    private void showSystemUI() {
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (Build.VERSION.SDK_INT >= 14) {
                drawer.setFitsSystemWindows(true);
            }

            if (Build.VERSION.SDK_INT >= 16) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            toolbar.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } catch (NullPointerException e) {
            logger.e("NullPointerException: " + e);
        }
    }

    public void enableBackIcon() {
        setupToolbar(R.layout.toolbar_back);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        View customView = actionBar.getCustomView();
        View backBtn    = customView.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void enableNavIcon() {
        setupToolbar(R.layout.toolbar_normal);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        View customView = actionBar.getCustomView();
        View backBtn    = customView.findViewById(R.id.btn_nav);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public void enableDeleteIcon(final DeleteListener listener) {
        setupToolbar(R.layout.toolbar_delete);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        View customView = actionBar.getCustomView();
        View backBtn    = customView.findViewById(R.id.btn_nav);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        View btnDelete = customView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteAll();
                }
            }
        });
    }

    private void setupToolbar(int resource) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        // disable default toolbar icon
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Inflate custom view.
        View         view   = getLayoutInflater().inflate(resource, null);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(view, layout);

        // Enable show custom view.
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // set toolbar title
        String title = activityTitles[navItemIndex];
        setToolbarTitle(title);
    }

    private void loadBannerAd() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_application_id));

        avBanner = (AdView) findViewById(R.id.av_banner);
        // AdRequest adRequest = new AdRequest.Builder().addTestDevice("A2E28D7487A7FF497332D6548F8E322C").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        //load ads
        avBanner.loadAd(adRequest);
    }

    public void showBannerAd() {
        if (avBanner != null) {
            avBanner.setVisibility(View.VISIBLE);
        }
    }

    public void hideBannerAd() {
        if (avBanner != null) {
            avBanner.setVisibility(View.GONE);
        }
    }

    private void loadFullAd() {

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.ad_full));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call api get location
        getLocation();
    }

    private void getLocation() {
        LoadingDialog.getInstance().show(MainActivity.this);

        Call<ResponseUserLocation> callGetLocation = LocationApi.getClient().create(ILocationApi.class).getLocation();
        callGetLocation.enqueue(new Callback<ResponseUserLocation>() {
            @Override
            public void onResponse(Call<ResponseUserLocation> call, Response<ResponseUserLocation> response) {
                ResponseUserLocation body        = response.body();
                String               ip          = "";
                String               countryName = "";
                String               countryCode = "";
                String               regionCode  = "";
                String               ips         = "";
                String               city        = "";

                if (body != null && body.getStatus()) {
                    ip = body.getQuery();
                    countryName = body.getCountry();
                    countryCode = body.getCountryCode();
                    regionCode = body.getRegion();
                    ips = body.getIsp();
                    city = body.getCity();
                }
                postAppInfo(ip, countryName, countryCode, regionCode, ips, city);
            }

            @Override
            public void onFailure(Call<ResponseUserLocation> call, Throwable t) {
                String ip          = "";
                String countryName = "";
                String countryCode = "";
                String regionCode  = "";
                String ips         = "";
                String city        = "";
                postAppInfo(ip, countryName, countryCode, regionCode, ips, city);
            }
        });
    }

    private void postAppInfo(String ip, String countryName, String countryCode, String regionCode, String ips, String city) {
        String            time              = String.valueOf(System.currentTimeMillis());
        String            token             = md5(time);
        ApiInitialization apiInitialization = new ApiInitialization(MainActivity.this);
        Call<ResponseBody> call = BaseApi.getClient().create(IBaseApi.class).postAppInfo(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                apiInitialization.getDeviceName(),
                apiInitialization.getAppVersion(MainActivity.this),
                apiInitialization.getAppName(MainActivity.this),
                apiInitialization.getOsName(),
                apiInitialization.getOsVersion(),

                ip,
                countryName,
                countryCode,
                regionCode,
                ips,
                city,

                token,
                time
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LoadingDialog.getInstance().hide();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.getInstance().hide();
            }
        });
    }

    public String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[]        bytes = digest.digest();
            final StringBuilder sb    = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }
}