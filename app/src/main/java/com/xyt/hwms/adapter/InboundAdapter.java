package com.xyt.hwms.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.InboundQuery;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.ui.InboundWasteDialogFragment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
    private List<InboundQuery> list;
    private EditText weight;


    public InboundAdapter(Context context, List<InboundQuery> list, EditText weight) {
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

        viewHolder.name.setText( list.get(position).getWaste_name());
        viewHolder.code.setText( list.get(position).getLabel_code());
        viewHolder.itemWeight.setText(String.valueOf(list.get(position).getWeight()).equals("null") ? "" : String.valueOf(list.get(position).getWeight()));

        viewHolder.itemWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewHolder.itemWeight.isFocused()) {
                    list.get(position).setWeight(Float.valueOf(s.toString()));
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
                removeRequest(position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                InboundWasteDialogFragment.newInstance(list.get(position)).show(fm, "xxx");
            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        jisuan();
        super.notifyDataSetChanged();
    }

    public void jisuan() {
        double x = 0;
        for (int i=0;i<list.size();i++){
            x+=String.valueOf(list.get(i).getWeight()).equals("")||String.valueOf(list.get(i).getWeight()).equals("null")?0:Double.valueOf(list.get(i).getWeight());
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

    //解除组盘
    private void removeRequest(final int position) {
        String url = Constants.SERVER + "mobile-get-in/relieve";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("store_label_code", store.getText().toString());//库
//        params.put("position_label_code", position.getText().toString());//库位
//        params.put("container_label_code", container.getText().toString());//容器
        params.put("label_code", list.get(position).getLabel_code());//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url + "?_username=develop&_password=whchem@2016", BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        list.remove(position);
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
}
