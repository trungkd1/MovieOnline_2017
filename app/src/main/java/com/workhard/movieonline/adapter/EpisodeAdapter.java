package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.model.Episode;

import java.util.List;
import java.util.Locale;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class EpisodeAdapter extends DiaLogAdapter<Episode> {
    private LayoutInflater inflater;

    private List<Episode> episodeList;
    private int curSelected;

    public EpisodeAdapter(Activity activity, List<Episode> episodeList) {
        this.episodeList = episodeList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (episodeList != null) {
            return episodeList.size();
        }
        return 0;
    }

    @Override
    public Episode getItem(int position) {
        if (episodeList != null) {
            episodeList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        Episode episode = episodeList.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_episode, null);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_episode_title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(episode.getTitle());
        viewHolder.tvTitle.setSelected(curSelected == position);
        return view;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView  tvTitle;
    }

    public int getCurSelected() {
        return curSelected;
    }

    public void setCurSelected(int curSelected) {
        this.curSelected = curSelected;
    }
}