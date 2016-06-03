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
import com.xyt.hwms.bean.TransferDetail;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class GroupAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<TransferDetail> list;
    private List<TransferDetail> listData;

    public GroupAdapter(Context context, List<TransferDetail> list, List<TransferDetail> listData) {
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

        viewHolder.name.setText(list.get(position).getWaste_name());
        viewHolder.code.setText(list.get(position).getLabel_code());
        if (Constants.WASTE_PASS.equals(list.get(position).getStatus())) {
            viewHolder.status.setBackgroundColor(0xff5ea640);
        } else if (Constants.WASTE_BACK.equals(list.get(position).getStatus())) {
            viewHolder.status.setBackgroundColor(0xffff8e00);
        } else {
            viewHolder.status.setBackgroundColor(0xffccc8c3);
        }

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.get(position).setContainer_label_code(null);
                PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.status)
        TextView status;
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
