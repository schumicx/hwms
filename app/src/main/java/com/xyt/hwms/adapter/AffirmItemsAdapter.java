package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.hwms.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class AffirmItemsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Map> list;

    public AffirmItemsAdapter(Context context, List<Map> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.list_item_affirm_items, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText((String)list.get(position).get("waste_detail_id"));
        viewHolder.code.setText((String)list.get(position).get("label_code"));
        if((String)list.get(position).get("status") == "已转移") {
            viewHolder.name.setBackgroundColor(0xff00ff00);
        } else if((String)list.get(position).get("status") == "退回"){
            viewHolder.name.setBackgroundColor(0xffff0000);
        } else {
            viewHolder.name.setBackgroundColor(0xffffffff);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.code)
        TextView code;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
