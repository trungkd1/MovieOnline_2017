package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workhard.movieonline.R;
import com.workhard.movieonline.model.Server;

import java.util.List;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class ServerAdapter extends DiaLogAdapter<Server> {
    private LayoutInflater inflater;

    private List<Server> serverList;
    private int selectedPos = 0;

    public ServerAdapter(Activity activity, List<Server> serverList) {
        this.serverList = serverList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (serverList != null) {
            return serverList.size();
        }
        return 0;
    }

    @Override
    public Server getItem(int position) {
        if (serverList != null) {
            return serverList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View       view = convertView;
        ViewHolder viewHolder;

        Server server = serverList.get(position);

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_server, null);

            viewHolder = new ViewHolder();
            viewHolder.tvServerName = (TextView) view.findViewById(R.id.tv_server_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvServerName.setText(server.getServerName());
        viewHolder.tvServerName.setSelected(selectedPos ==  position);

        return view;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvServerName;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void setSelectedPos(int pos) {
        selectedPos = pos;
    }

    public int getSelectedPos() {
        return selectedPos;
    }
}