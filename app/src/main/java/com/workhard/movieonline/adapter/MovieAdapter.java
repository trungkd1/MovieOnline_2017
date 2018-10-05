package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.workhard.movieonline.R;
import com.workhard.movieonline.database.Database;
import com.workhard.movieonline.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by TrungKD on 2/21/2017.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Logger logger = LoggerManager.getLogger(MovieAdapter.class);

    private List<Movie>          movieList;
    private Activity             activity;
    private OnMovieClickListener listener;
    private final int IMAGE_WIDTH  = 440;
    private final int IMAGE_HEIGHT = 640;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);

        void onBookmarkClick(Movie movie);
    }

    public MovieAdapter(Activity activity, List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder viewHolder, final int i) {
        final Movie movie = movieList.get(i);

        if (movie == null) {
            return;
        }

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            }
        });

        // Check db to set bookmark status
        Movie dbMovie = Database.favouriteDao.fetchFavouriteByAlias(movie.getAlias());
        if (dbMovie != null) {
            viewHolder.iv_movie_bookmark.setSelected(dbMovie.isBookmark());
            movie.setBookmark(dbMovie.isBookmark());
        }

        viewHolder.iv_movie_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = viewHolder.iv_movie_bookmark.isSelected();
                boolean dbResult;

                movie.setBookmark(!selected);
                if (selected) {
                    // Remove on database
                    dbResult = Database.favouriteDao.deleteFavouriteById(movie.getAlias());
                    if (listener != null) {
                        listener.onBookmarkClick(movie);
                    }
                } else {
                    // Add to database
                    dbResult = Database.favouriteDao.addFavourite(movie);
                }

                // If database processing is success, update UI
                if (dbResult) {
                    // Update bookmark UI
                    viewHolder.iv_movie_bookmark.setSelected(!selected);
                }
            }
        });

        // Use float division
        // Get screen width
        int screenWidth = activity.getWindow().getDecorView().getWidth();
        // Movie list is divided into 3 columns.
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.iv_movie_thumb.getLayoutParams();
        layoutParams.height = screenWidth / 3 * IMAGE_HEIGHT / IMAGE_WIDTH;
        viewHolder.iv_movie_thumb.setLayoutParams(layoutParams);

        loadMovieThumbByPicasso(movie, viewHolder.iv_movie_thumb);

        setEpisodeNums(viewHolder, movie.getEpisodesCount());

        viewHolder.tv_movie_title.setText(movie.getName());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void clear() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public void remove(Movie movie) {
        movieList.remove(movie);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View rootView;

        private ImageView iv_movie_bookmark;

        private ImageView iv_movie_thumb;

        private LinearLayout ll_BLOCK_episode;
        private TextView     tv_episode_number;

        private TextView tv_movie_title;

        public ViewHolder(View view) {
            super(view);
            rootView = view;

            iv_movie_bookmark = (ImageView) view.findViewById(R.id.iv_bookmark);

            iv_movie_thumb = (ImageView) view.findViewById(R.id.iv_movie_thumb);

            ll_BLOCK_episode = (LinearLayout) view.findViewById(R.id.BLOCK_episode);
            tv_episode_number = (TextView) view.findViewById(R.id.tv_episode_number);

            tv_movie_title = (TextView) view.findViewById(R.id.tv_movie_title);
        }
    }

    private void loadMovieThumbByPicasso(Movie movie, ImageView imageView) {
        Picasso.with(activity).load(movie.getCover())
                .fit()
                .error(R.mipmap.ic_close)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // logger.d("loading image url success");
                    }

                    @Override
                    public void onError() {
                        // logger.d("loading image url fail");
                    }
                });
    }

    private void setEpisodeNums(ViewHolder viewHolder, int episodeNum) {
        if (episodeNum <= 1) {
            viewHolder.ll_BLOCK_episode.setVisibility(View.GONE);
        } else {
            viewHolder.ll_BLOCK_episode.setVisibility(View.VISIBLE);
            viewHolder.tv_episode_number.setText(String.format(Locale.US, "%d", episodeNum));
        }
    }

    public void addMovieList(List<Movie> movieList) {
        if (movieList == null) {
            return;
        }

        if (this.movieList == null) {
            this.movieList = new ArrayList<>();
        }

        this.movieList.addAll(this.movieList.size(), movieList);
    }
}
