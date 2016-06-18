package com.xyt.hwms.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.DecimalFormat;
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

        viewHolder.warning.setVisibility("1".equals(list.get(position).getIs_key_waste()) ? View.VISIBLE : View.GONE);
        viewHolder.name.setText(list.get(position).getWaste_name());
        viewHolder.code.setText(list.get(position).getLabel_code());

        if (list.get(position).getWeight() != null) {
            viewHolder.itemWeight.setText(new DecimalFormat("#0.00").format(Double.valueOf(list.get(position).getWeight())));
        } else {
            viewHolder.itemWeight.getText().clear();
        }
        viewHolder.itemWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (viewHolder.itemWeight.isFocused()) {
                    if (!TextUtils.isEmpty(s.toString())) {
                        list.get(position).setWeight(Float.valueOf(s.toString()));
                    } else {
                        list.get(position).setWeight(null);
                    }
                    calculate();
                }
            }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRequest(position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                InboundWasteDialogFragment.newInstance(list.get(position)).show(fm, "inboundwastedialogfragment");
            }
        });

        return convertView;
    }

    public void calculate() {
        float temp = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWeight() != null) {
                temp += list.get(i).getWeight();
            }
        }
        weight.setText(new DecimalFormat("#0.00").format(Double.valueOf(temp)));
    }

    //解除组盘
    private void removeRequest(final int position) {
        String url = Constants.SERVER + "mobile-get-in/relieve";
        Map<String, Object> params = new HashMap<>();
        params.put("label_code", list.get(position).getLabel_code());//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        list.remove(position);
                        calculate();
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }), "inboundadapter");
    }

    static class ViewHolder {
        @BindView(R.id.warning)
        ImageView warning;
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
