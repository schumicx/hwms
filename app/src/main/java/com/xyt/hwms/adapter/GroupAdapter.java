package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class GroupAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Map> list;
    private List<Map> listData;

    public GroupAdapter(Context context, List<Map> list, List<Map> listData) {
        this.list = list;
        this.listData = listData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_group, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText((String) list.get(position).get("waste_detail_id"));
        viewHolder.code.setText((String) list.get(position).get("label_code"));
        if (Constants.WASTE_PASS.equals(list.get(position).get("status").toString())) {
            viewHolder.name.setBackgroundColor(0xff00ff00);
        } else if (Constants.WASTE_BACK.equals((String) list.get(position).get("status").toString())) {
            viewHolder.name.setBackgroundColor(0xffff0000);
        } else {
            viewHolder.name.setBackgroundColor(0xffffffff);
        }
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Map) listData.get(position)).put("container_label_code", null);
                PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.remove)
        Button remove;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
