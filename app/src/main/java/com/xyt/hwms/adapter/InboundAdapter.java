package com.xyt.hwms.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xyt.hwms.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class InboundAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Map> list;
    private EditText weight;


    public InboundAdapter(Context context, List<Map> list, EditText weight) {
        this.list = list;
        this.weight = weight;
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_inbound, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText((String) list.get(position).get("waste_detail_id"));
        viewHolder.code.setText((String) list.get(position).get("label_code"));
        viewHolder.itemWeight.setText(String.valueOf(list.get(position).get("itemWeight")).equals("null") ? "" : String.valueOf(list.get(position).get("itemWeight")));

        viewHolder.itemWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewHolder.itemWeight.isFocused()) {
                    list.get(position).put("itemWeight",s.toString());
                    jisuan();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        if (Constants.WASTE_PASS.equals(list.get(position).get("status").toString())) {
//            viewHolder.name.setBackgroundColor(0xff00ff00);
//        } else if (Constants.WASTE_BACK.equals((String) list.get(position).get("status").toString())) {
//            viewHolder.name.setBackgroundColor(0xffff0000);
//        } else {
//            viewHolder.name.setBackgroundColor(0xffffffff);
//        }
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                jisuan();
            }
        });

        return convertView;
    }

    public void jisuan() {
        double x = 0;
        for (int i=0;i<list.size();i++){
            x+=String.valueOf(list.get(i).get("itemWeight")).equals("")||String.valueOf(list.get(i).get("itemWeight")).equals("null")?0:Double.valueOf(list.get(i).get("itemWeight").toString());
        }
        weight.setText(""+x);
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.remove)
        Button remove;
        @BindView(R.id.item_weight)
        EditText itemWeight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
