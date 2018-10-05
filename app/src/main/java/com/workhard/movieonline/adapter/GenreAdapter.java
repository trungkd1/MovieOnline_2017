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
import com.workhard.movieonline.model.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungKD on 2/21/2017.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private Logger logger = LoggerManager.getLogger(GenreAdapter.class);

    private List<Genre>          genreList;
    private Activity             activity;
    private OnGenreClickListener listener;
    private final int IMAGE_WIDTH  = 680;
    private final int IMAGE_HEIGHT = 478;

    public interface OnGenreClickListener {
        void onGenreClick(Genre version);
    }

    public GenreAdapter(Activity activity, List<Genre> genreList, OnGenreClickListener listener) {
        this.genreList = genreList;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_genre, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onGenreClick(genreList.get(i));
                }
            }
        });

        viewHolder.tvGenreTitle.setText(genreList.get(i).getName());

        // Use float division
        // Get screen width
        int screenWidth = activity.getWindow().getDecorView().getWidth();
        // Movie list is divided into 3 columns.
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.ivGenreThumb.getLayoutParams();
        layoutParams.height = screenWidth / 2 * IMAGE_HEIGHT / IMAGE_WIDTH;
        viewHolder.ivGenreThumb.setLayoutParams(layoutParams);

        Picasso.with(activity).load(genreList.get(i).getImage())
                .fit()
                .error(R.drawable.test_480_320)
                .into(viewHolder.ivGenreThumb, new Callback() {
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

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View rootView;

        private TextView  tvGenreTitle;
        private ImageView ivGenreThumb;

        public ViewHolder(View view) {
            super(view);

            rootView = view;

            tvGenreTitle = (TextView) view.findViewById(R.id.tv_genre_title);
            ivGenreThumb = (ImageView) view.findViewById(R.id.iv_genre_thumb);
        }
    }

    public void addGenreList(List<Genre> genreList) {
        if (genreList == null) {
            return;
        }

        if (this.genreList == null) {
            this.genreList = new ArrayList<>();
        }

        this.genreList.addAll(this.genreList.size(), genreList);
    }
}
