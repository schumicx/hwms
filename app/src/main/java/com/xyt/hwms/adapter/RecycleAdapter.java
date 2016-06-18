package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.hwms.R;
import com.xyt.hwms.bean.Recycle;
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
    private List<Recycle> list;

    public RecycleAdapter(Context context, List<Recycle> list) {
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
        viewHolder.code.setText(list.get(position).getApply_code());
        viewHolder.org.setText(list.get(position).getParent_org_name() + "-" + list.get(position).getOrg_name());
        viewHolder.text1.setText(list.get(position).getUser_name());
        viewHolder.text2.setText(list.get(position).getPhone());
        viewHolder.text3.setText(DateUtils.getTime(list.get(position).getCreate_time()));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.org)
        TextView org;
        @BindView(R.id.text1)
        TextView text1;
        @BindView(R.id.text2)
        TextView text2;
        @BindView(R.id.text3)
        TextView text3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
