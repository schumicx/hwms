package com.xyt.hwms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.RecycleDetail;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Levin on 2016-05-04.
 */
public class RecycleItemsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<RecycleDetail> list;
    private String id;
    private TextView empty;

    public RecycleItemsAdapter(Context context, List<RecycleDetail> list, String id, TextView empty) {
        this.id = id;
        this.empty = empty;
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
            convertView = inflater.inflate(R.layout.list_item_recycle_items, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(list.get(position).getWaste_name());
        viewHolder.code.setText(list.get(position).getLabel_code());

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recallRequest(position);
            }
        });

        return convertView;
    }

    //内部利用撤销
    private void recallRequest(final int position) {
        String url = Constants.SERVER + "mobile-hwiu/" + id + "/detail/remove";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("label_code", list.get(position).getLabel_code());
//        params.put("record_id", );
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url + "?_username=develop&_password=whchem@2016", BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        list.remove(position);
                        if (list.size()==0){
                            empty.setVisibility(View.VISIBLE);
                        }
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, /*new Gson().fromJson(*/new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers))/*, BaseBean.class).getContent()*/, Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, "网络连接失败,请检查您的网络", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "服务器连接异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }), "xxxx");
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
