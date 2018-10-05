package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.model.Subtitle;
import com.workhard.movieonline.util.SubtitleComparable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class SubtitleAdapter extends DiaLogAdapter<Subtitle> implements StickyListHeadersAdapter {
    private LayoutInflater inflater;
    private List<Subtitle> subtitleList;

    public SubtitleAdapter(Activity activity, List<Subtitle> subtitleList) {
        this.subtitleList = subtitleList;
        Collections.sort(this.subtitleList, new SubtitleComparable());
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (subtitleList != null) {
            return subtitleList.size();
        }
        return 0;
    }

    @Override
    public Subtitle getItem(int position) {
        if (subtitleList != null && subtitleList.size() > position) {
            return subtitleList.get(position);
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
        Subtitle   subtitle = subtitleList.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_subtitle, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvOrder.setText(String.format(Locale.US, "%d", position));
        viewHolder.tvTitle.setText(subtitle.getTitle());
        viewHolder.tvRating.setText(String.format(Locale.US, "Rating: %d", subtitle.getRating()));
        return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View             view     = convertView;
        HeaderViewHolder holder;
        Subtitle         subtitle = subtitleList.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_subtitle_header, null);
            holder = new HeaderViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        // set tvSubtitleHeader text as first char in name
        holder.tvSubtitleHeader.setText(subtitle.getLanguage());
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        return subtitleList.get(position).getLanguage().charAt(0);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvOrder;
        TextView tvTitle;
        TextView tvRating;

        public ViewHolder(View view) {
            tvOrder = (TextView) view.findViewById(R.id.tv_order);
            tvTitle = (TextView) view.findViewById(R.id.tv_subtitle_title);
            tvRating = (TextView) view.findViewById(R.id.tv_rating);
        }
    }

    private class HeaderViewHolder {
        private TextView tvSubtitleHeader;

        public HeaderViewHolder(View v) {
            tvSubtitleHeader = (TextView) v.findViewById(R.id.tv_subtitle_header);
        }
    }
}
