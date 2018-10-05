package com.workhard.movieonline.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.ServerAdapter;
import com.workhard.movieonline.model.Server;

import java.util.List;

/**
 * Created by TrungKD on 3/21/2017.
 */
public class ServerDialog extends Dialog {

    private Callback callback;
    private ServerAdapter adapter;
    private Server curSelected;

    public interface Callback {
        public void onServerClick(Server server);
    }

    public ServerDialog(Context context, ServerAdapter adapter, Callback callback) {
        super(context);

        this.callback = callback;
        this.adapter = adapter;

        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_list_view_center);

        final ListView serverListView = (ListView) findViewById(R.id.lv_container);
        serverListView.setAdapter(adapter);
        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getSelectedPos() == position) {
                    dismiss();
                    return;
                }

                if (callback != null) {
                    adapter.setSelectedPos(position);
                    curSelected = adapter.getItem(position);
                    callback.onServerClick(curSelected);
                }
            }
        });

        ImageView ivClose = (ImageView) findViewById(R.id.btn_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        serverListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        curSelected = adapter.getItem(adapter.getSelectedPos());
    }

    public void setServerList(List<Server> serverList) {
        adapter.setServerList(serverList);
        adapter.notifyDataSetChanged();
    }

    public List<Server> getServerList() {
        return adapter.getServerList();
    }

    public Server getCurSelected() {
        return curSelected;
    }
}