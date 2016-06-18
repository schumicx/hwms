package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xyt.hwms.R;
import com.xyt.hwms.bean.OutboundDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class OutboundDialogAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OutboundDetail> list;

    public OutboundDialogAdapter(Context context, List<OutboundDetail> list) {
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
        viewHolder.status.setVisibility(View.GONE);
        viewHolder.name.setText(list.get(position).getWaste_name());
        viewHolder.code.setText(list.get(position).getLabel_code());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.code)
        TextView code;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
