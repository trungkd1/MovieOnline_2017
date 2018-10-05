package com.workhard.movieonline.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.EpisodeAdapter;
import com.workhard.movieonline.model.Episode;

/**
 * Created by TrungKD on 2/26/2017.
 */
public class EpisodeDialog extends ImmersiveDiaLog {
    public interface Callback {
        void onEpisodeClick(Episode episode);
    }

    private ListView       listView;
    private EpisodeAdapter adapter;
    private Callback       callback;

    public EpisodeDialog(Context context, Callback callback, EpisodeAdapter adapter) {
        super(context);
        this.callback = callback;
        initView(context, adapter);
    }

    private void initView(Context context, EpisodeAdapter adapter) {
        activity = (Activity) context;
        setContentView(R.layout.dialog_list_view);

        listView = (ListView) findViewById(R.id.lv_container);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    callback.onEpisodeClick(EpisodeDialog.this.adapter.getItem(position));
                }
            }
        });

        ImageView ivClose = (ImageView) findViewById(R.id.btn_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpisodeDialog.this.dismiss();
            }
        });

        this.adapter = adapter;
        listView.setAdapter(adapter);
        this.adapter.notifyDataSetChanged();
    }
}
