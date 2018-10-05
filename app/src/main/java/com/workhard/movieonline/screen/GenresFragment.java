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
import com.workhard.movieonline.adapter.GenreAdapter;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.model.Genre;
import com.workhard.movieonline.rest.model.ResponseGenreList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TrungKD on 2/20/2017.
 */
public class GenresFragment extends BaseFragment implements GenreAdapter.OnGenreClickListener {
    private Logger logger = LoggerManager.getLogger(GenresFragment.class);
    private GenreAdapter genreAdapter;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_genre;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), null);
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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_genre_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        genreAdapter   = new GenreAdapter(activity, new ArrayList<Genre>(), GenresFragment.this);
        recyclerView.setAdapter(genreAdapter);

        getGenres();
    }

    @Override
    public boolean onBackClick() {
        return false;
    }

    @Override
    public void onGenreClick(Genre genre) {
        // Go to genre detail.
        openGenreDetail(genre);
    }

    private void openGenreDetail(Genre genre) {
        GenreDetailFragment fragment = new GenreDetailFragment();
        fragment.setGenre(genre);
        activity.addFragment(fragment, fragment.getTag());
    }

    @Override
    public void setupActionBar() {
        activity.enableNavIcon();
    }

    private void getGenres() {
        Call<ResponseGenreList> call = baseApi.getGenres("deviceToken", "deviceId", "android", "en", "Nexus 5");
        call.enqueue(new Callback<ResponseGenreList>() {
            @Override
            public void onResponse(Call<ResponseGenreList> call, Response<ResponseGenreList> response) {
                List<Genre> genreList = response.body().getData();
                genreAdapter.addGenreList(genreList);
                genreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseGenreList> call, Throwable t) {
                logger.e(t.toString());
            }
        });
    }
}