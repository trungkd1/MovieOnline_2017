package com.workhard.movieonline.screen;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.MovieAdapter;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.model.Genre;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.rest.model.ResponseGenreDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TrungKD on 3/5/2017.
 */
public class GenreDetailFragment extends BaseFragment implements MovieAdapter.OnMovieClickListener {

    private Logger logger = LoggerManager.getLogger(GenreDetailFragment.class);
    private Genre genre;
    private MovieAdapter movieAdapter;

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_genre_detail_movie_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(activity, new ArrayList<Movie>(), GenreDetailFragment.this);
        recyclerView.setAdapter(movieAdapter);

        getGenreDetail(1);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_genre_detail;
    }

    @Override
    public boolean onBackClick() {
        activity.popBackStack();
        return true;
    }

    @Override
    public void onMovieClick(Movie movie) {
        openMovieDetail(movie);
    }

    private void openMovieDetail(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setMovie(movie);
        activity.addFragment(fragment, fragment.getTag());
    }

    @Override
    public void onBookmarkClick(Movie movie) {

    }

    @Override
    public void setupActionBar() {
        activity.enableBackIcon();
        activity.setToolbarTitle(genre.getName());
    }

    private void getGenreDetail(int pageNum) {
        if (genre == null) {
            return;
        }

        String category = genre.getAlias();
        String page = String.valueOf(pageNum);

        Call<ResponseGenreDetail> call = baseApi.getGenreDetail("deviceToken", "deviceId", "android", "en", "Nexus 5", category, page);
        call.enqueue(new Callback<ResponseGenreDetail>() {
            @Override
            public void onResponse(Call<ResponseGenreDetail> call, Response<ResponseGenreDetail> response) {
                List<Movie> movieList = response.body().getData();
                movieAdapter.addMovieList(movieList);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseGenreDetail> call, Throwable t) {
                logger.e(t.toString());
            }
        });
    }
}
