package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.DateUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class RecycleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Map> list;

    public RecycleAdapter(Context context, List<Map> list) {
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
            convertView = inflater.inflate(R.layout.list_item_recycle, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.code.setText((String) list.get(position).get("apply_code"));
        viewHolder.position.setText((String) list.get(position).get("transfer_position"));
        viewHolder.time.setText(DateUtils.getDate2(new Double((double) list.get(position).get("apply_date")).longValue()));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.position)
        TextView position;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
