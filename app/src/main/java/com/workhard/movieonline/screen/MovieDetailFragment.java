package com.workhard.movieonline.screen;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.EpisodeAdapter;
import com.workhard.movieonline.adapter.MovieAdapter;
import com.workhard.movieonline.adapter.MovieDetailPagerAdapter;
import com.workhard.movieonline.adapter.MovieInfoAdapter;
import com.workhard.movieonline.adapter.ServerAdapter;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.database.Database;
import com.workhard.movieonline.model.Episode;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.model.MovieInfo;
import com.workhard.movieonline.model.Server;
import com.workhard.movieonline.rest.ApiInitialization;
import com.workhard.movieonline.rest.model.MovieDetailData;
import com.workhard.movieonline.rest.model.ResponseEpisodeList;
import com.workhard.movieonline.rest.model.ResponseMovieDetail;
import com.workhard.movieonline.rest.model.ResponseMovieList;
import com.workhard.movieonline.util.ImageUtil;
import com.workhard.movieonline.rest.MovieDetailInitialization;
import com.workhard.movieonline.util.Util;
import com.workhard.movieonline.widget.LoadingDialog;
import com.workhard.movieonline.widget.ServerDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by TrungKD on 2/25/2017.
 */
public class MovieDetailFragment extends BaseFragment implements MovieAdapter.OnMovieClickListener, ServerDialog.Callback, MovieDetailInitialization.Callback {

    private Logger logger = LoggerManager.getLogger(MovieDetailFragment.class);
    private int curPosition = 0;
    private int prevPosition = 0;
    private MovieDetailPagerAdapter adapter;
    private Movie movie;
    private final int minOffset = -936;
    private final int maxOffset = 0;
    private boolean isDisableScroll = true;

    private boolean isMovieInfoShown = false;

    private ResponseMovieDetail responseMovieDetail;
    private List<Episode> episodeList = new ArrayList<>();
    private ServerDialog serverDialog;
    private EpisodeAdapter episodeAdapter;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), null);

        initWatchMovieButton(rootView);

        initMovieThumb(rootView);

        initAppBarTopView(rootView);

        setupTabLayout(activity, rootView);

        initServerButton(rootView);

        initBookmarkButton(rootView);

        initShareMovieButton(rootView);

        setupActionBar();

        MovieDetailInitialization initialization =
                new MovieDetailInitialization(baseApi, this, movie, activity);
        initialization.initMovieDetail();

        return rootView;
    }

    private void setupTabLayout(Context context, View rootView) {
        adapter = new MovieDetailPagerAdapter(context, firstPageLoadedListener);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.vpMovieDetailPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tlMovieDetailTab);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(listener);
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            curPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Finished scrolling.
            if (state == 0) {
                if (curPosition != prevPosition) {
                    // Load correspond data.
                    loadData(curPosition);
                    prevPosition = curPosition;
                }
            }
        }
    };

    private void loadData(int position) {
        List<View> viewList = adapter.getViewList();
        View tabView = viewList.get(position);
        switch (position) {
            case 0:
                if (episodeList == null || episodeList.size() <= 0) {
                    loadEpisode(tabView);
                }
                break;

            case 1:
                if (isMovieInfoShown) {
                    // Show movie info
                    loadMovieInfo(tabView);
                }
                break;
            case 2:
                // Load data for more like tab
                loadMoreLikeData(tabView);
                break;
            default:
                break;
        }
    }

    private void loadEpisode(View tabView) {
        ListView lvEpisode = (ListView) tabView.findViewById(R.id.lv_episode);
        if (lvEpisode == null) {
            return;
        }

        episodeAdapter = new EpisodeAdapter(activity, episodeList);
        lvEpisode.setAdapter(episodeAdapter);
        episodeAdapter.notifyDataSetChanged();

        lvEpisode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int episodePos, long id) {
                if (!isDisableScroll) {
                    episodeAdapter.setCurSelected(episodePos);
                    openMoviePlayFragment(movie, episodePos);
                }
            }
        });
    }

    private void loadMovieInfo(View tabView) {
        if (responseMovieDetail == null) {
            return;
        }

        MovieDetailData data = responseMovieDetail.getData();

        if (data == null) {
            return;
        }

        Movie movie = data.getInfo();

        // List info
        StickyListHeadersListView lvInfo = (StickyListHeadersListView) tabView.findViewById(R.id.lv_info);
        MovieInfoAdapter movieInfoAdapter = new MovieInfoAdapter(activity, createMovieInfoList(movie));
        lvInfo.setAdapter(movieInfoAdapter);
        movieInfoAdapter.notifyDataSetChanged();

        // Description text
        TextView tvInfo = (TextView) tabView.findViewById(R.id.tv_info);
        tvInfo.setMovementMethod(new ScrollingMovementMethod());

        String description = createMovieDescription(movie);

        tvInfo.setText(Util.fromHtml(description));

        isMovieInfoShown = true;
    }

    private List<MovieInfo> createMovieInfoList(Movie movie) {
        List<MovieInfo> movieInfos = new ArrayList<>();
        MovieInfo info;

        // GENRE
        info = new MovieInfo("GENRE", movie.getTags());
        movieInfos.add(info);

        // DIRECTOR
        String[] directorList = movie.getDirector().split(",");
        for (String directorName : directorList) {
            info = new MovieInfo("DIRECTOR", directorName);
            movieInfos.add(info);
        }

        // STAR
        String[] stars = movie.getStar().split(",");
        for (String star : stars) {
            info = new MovieInfo("STAR", star);
            movieInfos.add(info);
        }

        return movieInfos;
    }

    private String createMovieDescription(Movie movie) {
        String result = "";

        if (movie != null) {
            result += "<h1><b>" + movie.getName() + "</b></h1><br/>";
            result += "IMDB Rating: " + movie.getRating() + "<br/>";
            result += "Duration: " + movie.getDuration() + "<br/>";
            if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
                result += "Release Date: " + movie.getReleaseDate() + "<br/>";
            }
            result += movie.getDescription() + "<br/>";
        }

        return result;
    }

    private void loadMoreLikeData(final View tabView) {
        ApiInitialization apiInitialization = new ApiInitialization(activity);
        Call<ResponseMovieList> call = baseApi.getMoreLike(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                movie.getAlias()
        );

        call.enqueue(new Callback<ResponseMovieList>() {
            @Override
            public void onResponse(Call<ResponseMovieList> call, Response<ResponseMovieList> response) {
                List<Movie> moreLikeList = response.body().getData();
                initMoreLikeData(tabView, moreLikeList);
            }

            @Override
            public void onFailure(Call<ResponseMovieList> call, Throwable t) {

            }
        });
    }

    private void initMoreLikeData(View tabView, List<Movie> moreLikeList) {
        RecyclerView recyclerView = (RecyclerView) tabView.findViewById(R.id.rv_more_like_movie_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        MovieAdapter moreMovieAdapter = new MovieAdapter(activity, moreLikeList, MovieDetailFragment.this);
        recyclerView.setAdapter(moreMovieAdapter);
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();

        // Trick for disable recyler view scrolling.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return isDisableScroll;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    MovieDetailPagerAdapter.Callback firstPageLoadedListener = new MovieDetailPagerAdapter.Callback() {
        @Override
        public void onFirstViewAdded() {
            List<View> viewList = adapter.getViewList();
            View tabView = viewList.get(curPosition);
            loadEpisode(tabView);
        }
    };

    private void openMoviePlayFragment(Movie movie, int episodePos) {
        MoviePlayFragment fragment = new MoviePlayFragment();
        fragment.setMovie(movie);
        fragment.setEpisodePos(episodePos);
        if (responseMovieDetail != null) {
            MovieDetailData data = responseMovieDetail.getData();
            if (data != null) {
                fragment.setEpisodeList(data.getEpisodeList());
            }
        }
        fragment.setArguments(initArguments());
        activity.addFragment(fragment, getTag());
        hideActionBar();
    }

    @Override
    public boolean onBackClick() {
        // Call back movie info to home fragment
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            targetFragment.onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_OK, new Intent());
        }

        activity.popBackStack();
        return true;
    }

    @Override
    public void onMovieClick(Movie movie) {
        openMovieDetailFragment(movie);
    }

    private void openMovieDetailFragment(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setMovie(movie);
        activity.addFragment(fragment, fragment.getTag());
    }

    @Override
    public void onBookmarkClick(Movie movie) {
        logger.d("movie: " + movie);
    }

    @Override
    public void onServerClick(Server server) {
        logger.d("server: " + server);
        // Clear old episode list.
        if (episodeList != null) {
            episodeList.clear();
            episodeList = new ArrayList<>();
        }
        // Get new episode list.
        ApiInitialization apiInitialization = new ApiInitialization(activity);
        Call<ResponseEpisodeList> call = baseApi.getEpisodes(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                movie.getAlias(),
                server.getServerId());

        call.enqueue(new Callback<ResponseEpisodeList>() {
            @Override
            public void onResponse(Call<ResponseEpisodeList> call, Response<ResponseEpisodeList> response) {
                episodeList = response.body().getData();

                List<View> viewList = adapter.getViewList();
                View tabView = viewList.get(curPosition);
                loadEpisode(tabView);
                if (serverDialog != null) {
                    serverDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseEpisodeList> call, Throwable t) {
                logger.e(t.toString());
                if (serverDialog != null) {
                    serverDialog.dismiss();
                }
            }
        });
    }

    private void initBookmarkButton(View rootView) {
        final ImageView btnBookmark = (ImageView) rootView.findViewById(R.id.iv_bookmark);
        btnBookmark.setSelected(movie.isBookmark());
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = btnBookmark.isSelected();
                boolean dbResult;
                if (selected) {
                    // Remove from database
                    dbResult = Database.favouriteDao.deleteFavouriteById(movie.getAlias());
                } else {
                    // Add to database
                    movie.setBookmark(true);
                    dbResult = Database.favouriteDao.addFavourite(movie);
                }
                // If database processing is success, update UI
                if (dbResult) {
                    // Update bookmark UI
                    btnBookmark.setSelected(!selected);
                }
            }
        });
    }

    private void initServerButton(View rootView) {
        Button btnServer = (Button) rootView.findViewById(R.id.btn_server);
        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog server
                serverDialog.show();
            }
        });
    }

    private void initAppBarTopView(View rootView) {
        AppBarLayout topView = (AppBarLayout) rootView.findViewById(R.id.appbarTopView);
        topView.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == maxOffset) {
                    // Disable viewpager scrolling
                    isDisableScroll = true;
                } else if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
                    // Enable viewpager scrolling
                    isDisableScroll = false;
                }
            }
        });
    }

    private void initWatchMovieButton(View rootView) {
        Button btnWatch = (Button) rootView.findViewById(R.id.btn_watch_now);
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Default episode is 0
                openMoviePlayFragment(movie, 0);
            }
        });

        ImageView ivWatch = (ImageView) rootView.findViewById(R.id.iv_play);
        ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Default episode is 0
                openMoviePlayFragment(movie, 0);
            }
        });
    }

    private void initMovieThumb(View rootView) {
        if (movie != null && movie.getCover() != null) {
            ImageView ivMovieThumb = (ImageView) rootView.findViewById(R.id.iv_movie_thumb);
            ImageUtil.loadImage(activity, movie.getCover(), 240, 320, ivMovieThumb, R.drawable.test_320_480);
        }
    }

    private void initShareMovieButton(View rootView) {
        ImageView btnShareMovie = (ImageView) rootView.findViewById(R.id.iv_share_movie);
        btnShareMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*// Open share app.
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                String title       = movie.getName();
                String image       = movie.getCover();
                String description = movie.getDescription();

                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(image));
                intent.putExtra(Intent.EXTRA_TEXT, description);

                intent.setType("image/jpeg");

                startActivity(intent);*/
                onShareClick();
            }
        });
    }

    public void onShareClick() {
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, Util.fromHtml("html ne"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject ne");
        emailIntent.setType("message/rfc822");

        PackageManager pm = activity.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");


        Intent openInChooser = Intent.createChooser(emailIntent, "Share via");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, "Share Twitter");
                } else if (packageName.contains("facebook")) {
                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                    // will show the <meta content ="..."> text from that page with our link in Facebook.
                    intent.putExtra(Intent.EXTRA_TEXT, "Share facebook");
                } else if (packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, "Share sms");
                } else if (packageName.contains("android.gm")) { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
                    intent.putExtra(Intent.EXTRA_TEXT, Util.fromHtml("Share gmail"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
                    intent.setType("message/rfc822");
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupActionBar();
        }
    }

    @Override
    public void setupActionBar() {
        activity.enableBackIcon();
        activity.setToolbarTitle(movie.getName());
    }

    private Bundle initArguments() {
        Bundle bundle = new Bundle();
        bundle.putString("action", MoviePlayFragment.ACTION_VIEW);
//        String movieUrl = "http://api.mp3.zing.vn/api/mobile/source/video/LGJGTLGNQANQDLETNDGTDGLG";
         String movieUrl = "http://www.storiesinflight.com/js_videosub/jellies.mp4";
        bundle.putString("movie_url", movieUrl);
        return bundle;
    }

    @Override
    public void onStartGetMovieDetail() {
        // show loading
        LoadingDialog.getInstance().show(activity);
    }

    @Override
    public void onMovieDetailGettingSuccess(ResponseMovieDetail responseMovieDetail) {
        this.responseMovieDetail = responseMovieDetail;
        List<Server> serverList = new ArrayList<>();
        if (responseMovieDetail != null) {
            MovieDetailData data = responseMovieDetail.getData();
            if (data != null) {
                // Save episode list
                episodeList.addAll(data.getEpisodeList());
                // Save server list
                serverList.addAll(data.getServerList());
            }
        }

        // Build server list dialog
        if (serverDialog == null) {
            serverDialog = new ServerDialog(activity,
                    new ServerAdapter(activity, serverList),
                    MovieDetailFragment.this);
        }

        if (serverDialog.getServerList().size() <= 0) {
            serverDialog.setServerList(serverList);
        }

        // Show movie info
        showMovieInfo();

        // hide loading
        LoadingDialog.getInstance().hide();
    }

    @Override
    public void onMovieDetailGettingFail(Throwable throwable) {
        // hide loading
        LoadingDialog.getInstance().hide();
        // TODO: show noti
    }

    private void showMovieInfo() {
        if (responseMovieDetail == null) {
            return;
        }

        if (adapter == null) {
            return;
        }

        List<View> viewList = adapter.getViewList();
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            View tabView = viewList.get(i);
            if (tabView == null) {
                continue;
            }

            switch (i) {
                case 0:
                    if (episodeList.size() >= 0) {
                        continue;
                    }
                    // Load episode list
                    loadEpisode(tabView);
                    break;
                case 1:
                    if (isMovieInfoShown) {
                        continue;
                    }
                    // Load movie info
                    loadMovieInfo(tabView);
                    break;
            }
        }
    }
}