package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.model.MovieInfo;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class MovieInfoAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private LayoutInflater  inflater;
    private List<MovieInfo> movieInfos;

    public MovieInfoAdapter(Activity activity, List<MovieInfo> movieInfos) {
        this.movieInfos = movieInfos;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (movieInfos != null) {
            return movieInfos.size();
        }
        return 0;
    }

    @Override
    public MovieInfo getItem(int position) {
        if (movieInfos != null && movieInfos.size() > position) {
            return movieInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View       view     = convertView;
        ViewHolder viewHolder;
        MovieInfo  movieInfo = movieInfos.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_movie_info, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMovieInfoName.setText(movieInfo.getContent());
        return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View             view     = convertView;
        HeaderViewHolder holder;
        MovieInfo        movieInfo = movieInfos.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_movie_info_header, null);
            holder = new HeaderViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        // set tvMovieInfoHeader text as first char in name
        holder.tvMovieInfoHeader.setText(movieInfo.getHeader());
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        return movieInfos.get(position).getHeader().charAt(0);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvMovieInfoName;

        public ViewHolder(View view) {
            tvMovieInfoName = (TextView) view.findViewById(R.id.tv_movie_info_name);
        }
    }

    private class HeaderViewHolder {
        private TextView tvMovieInfoHeader;

        public HeaderViewHolder(View v) {
            tvMovieInfoHeader = (TextView) v.findViewById(R.id.tv_movie_info_header);
        }
    }
}
