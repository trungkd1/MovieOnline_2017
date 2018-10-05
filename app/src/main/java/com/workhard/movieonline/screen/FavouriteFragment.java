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
import com.workhard.movieonline.database.Database;
import com.workhard.movieonline.listener.DeleteListener;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.widget.ConfirmDeleteDialog;

import java.util.List;

/**
 * Created by TrungKD on 2/20/2017.
 */
public class FavouriteFragment extends BaseFragment implements MovieAdapter.OnMovieClickListener, ConfirmDeleteDialog.Callback, DeleteListener {
    private Logger logger = LoggerManager.getLogger(FavouriteFragment.class);

    private RecyclerView        rvFavouriteList;
    private MovieAdapter        adapter;
    private ConfirmDeleteDialog confirmDeleteDialog;

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
        rvFavouriteList = (RecyclerView) rootView.findViewById(R.id.rv_favourite_list);
        rvFavouriteList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        rvFavouriteList.setLayoutManager(layoutManager);

        // Get history list from db
        List<Movie> movieList = Database.favouriteDao.fetchAllFavourites();
        adapter = new MovieAdapter(activity, movieList, FavouriteFragment.this);
        rvFavouriteList.setAdapter(adapter);

        confirmDeleteDialog = new ConfirmDeleteDialog(activity, FavouriteFragment.this,
                getString(R.string.title_delete_bookmarks), getString(R.string.content_delete_bookmarks));
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_favourite;
    }

    @Override
    public boolean onBackClick() {
        return false;
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
        adapter.remove(movie);
    }

    @Override
    public void onOkButtonClick() {
        // Clear history item.
        Database.favouriteDao.deleteAllFavourites();
        // Refresh item list.
        adapter.clear();
    }

    @Override
    public void onDeleteAll() {
        if (adapter != null && adapter.getItemCount() > 0) {
            confirmDeleteDialog.show();
        }
    }

    @Override
    public void setupActionBar() {
        activity.enableDeleteIcon(FavouriteFragment.this);
    }
}
