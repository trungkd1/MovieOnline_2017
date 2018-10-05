package com.workhard.movieonline.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.SubtitleAdapter;
import com.workhard.movieonline.model.Subtitle;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by TrungKD on 2/26/2017.
 */
public class SubtitleDialog extends ImmersiveDiaLog {
    public interface Callback {
        void onSubtitleClick(Subtitle subtitle);
    }

    private StickyListHeadersListView listView;
    private SubtitleAdapter           adapter;
    private Callback                  callback;

    public SubtitleDialog(Context context, Callback callback, SubtitleAdapter adapter) {
        super(context);
        this.callback = callback;
        initView(context, adapter);
    }

    private void initView(Context context, SubtitleAdapter adapter) {
        activity = (Activity) context;
        setContentView(R.layout.dialog_sticky_header_list_view);

        listView = (StickyListHeadersListView) findViewById(R.id.lv_container);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    callback.onSubtitleClick(SubtitleDialog.this.adapter.getItem(position));
                }
                dismiss();
            }
        });

        ImageView ivClose = (ImageView) findViewById(R.id.btn_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubtitleDialog.this.dismiss();
            }
        });

        this.adapter = adapter;
        listView.setAdapter(adapter);
        this.adapter.notifyDataSetChanged();
    }
}
