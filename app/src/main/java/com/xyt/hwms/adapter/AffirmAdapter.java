package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyt.hwms.R;
import com.xyt.hwms.bean.Transfer;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class AffirmAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Transfer> list;
    private String type;

    public AffirmAdapter(Context context, List<Transfer> list, String type) {
        this.list = list;
        this.type = type;
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
            convertView = inflater.inflate(R.layout.list_item_affirm, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!type.equals(list.get(position).getTransfer_type())) {
            viewHolder.code.setVisibility(View.GONE);
            viewHolder.org.setVisibility(View.GONE);
            viewHolder.text1.setVisibility(View.GONE);
            viewHolder.text2.setVisibility(View.GONE);
            viewHolder.text3.setVisibility(View.GONE);
            viewHolder.carCode.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
        } else {
            viewHolder.code.setVisibility(View.VISIBLE);
            viewHolder.org.setVisibility(View.VISIBLE);
            viewHolder.text1.setVisibility(View.VISIBLE);
            viewHolder.text2.setVisibility(View.VISIBLE);
            viewHolder.text3.setVisibility(View.VISIBLE);
            viewHolder.carCode.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.VISIBLE);
        }

        viewHolder.code.setText("" + list.get(position).getApply_code());
        viewHolder.org.setText(list.get(position).getParent_org_name() + "-" + list.get(position).getOrg_name());
        if (Constants.TRANSFER_TYPE_OUTER.equals(list.get(position).getTransfer_type())) {
            viewHolder.text2.setVisibility(View.GONE);
            viewHolder.text1.setText(list.get(position).getCard_name());
            viewHolder.carCode.setText(list.get(position).getCar_code());
            viewHolder.text3.setText("电子锁号:" + list.get(position).getLock_code());
        } else if (Constants.TRANSFER_TYPE_INNER.equals(list.get(position).getTransfer_type())) {
            viewHolder.carCode.setVisibility(View.GONE);
            viewHolder.text1.setText(list.get(position).getDuty_person());
            viewHolder.text2.setText(list.get(position).getPhone());
            viewHolder.text3.setText(DateUtils.getTime(list.get(position).getCreate_time()));
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_layout)
        RelativeLayout itemLayout;
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.org)
        TextView org;
        @BindView(R.id.text1)
        TextView text1;
        @BindView(R.id.text2)
        TextView text2;
        @BindView(R.id.car_code)
        TextView carCode;
        @BindView(R.id.text3)
        TextView text3;
        @BindView(R.id.line)
        View line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
