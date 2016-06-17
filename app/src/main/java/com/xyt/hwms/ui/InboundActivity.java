package com.xyt.hwms.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.InboundAdapter;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.InboundQuery;
import com.xyt.hwms.bean.InboundQueryBean;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InboundActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.submit)
    Button submit;

    private EditText store;
    private EditText position;
    private EditText container;
    private EditText weight;

    private List<InboundQuery> list = new ArrayList<>();
    private InboundAdapter inboundAdapter;
    private String labelCode;

    @OnClick(R.id.submit)
    public void onClick(View v) {
        submitRequest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View head = getLayoutInflater().inflate(R.layout.list_head_inbound, null);
        store = (EditText) head.findViewById(R.id.store);
        position = (EditText) head.findViewById(R.id.position);
        container = (EditText) head.findViewById(R.id.container);
        weight = (EditText) head.findViewById(R.id.weight);
        listview.addHeaderView(head, null, true);

        if (inboundAdapter == null) {
            inboundAdapter = new InboundAdapter(context, list, weight);
        }
        listview.setAdapter(inboundAdapter);

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (weight.isFocused()) {
//                    if (list.size() <= 0) {
//                        Toast.makeText(context, "请先完成组盘!", Toast.LENGTH_SHORT).show();
//                        weight.getText().clear();
//                        return;
//                    }
                    if (!TextUtils.isEmpty(s.toString())) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setWeight(Float.valueOf(s.toString()) / list.size());
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setWeight(null);
                        }
                    }
                    inboundAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTagId(String data) {
    }

    @Override
    public void getBarcode(String data) {
        barCodeData = data;
        if (data.startsWith(Constants.LABEL_LIB)) {
            if (!TextUtils.isEmpty(position.getText().toString()) && !barCodeData.equals(store.getText().toString())) {
                position.getText().clear();
            }
            store.getText().clear();
            store.setText(data);
        } else if (data.startsWith(Constants.LABEL_LSL)) {
            position.getText().clear();
            position.setText(data);
            store.getText().clear();
            store.setText(data.replace(Constants.LABEL_LSL, Constants.LABEL_LIB).substring(0, data.length() - 4));
        } else if (data.startsWith(Constants.LABEL_CON)) {
            if (!TextUtils.isEmpty(container.getText().toString()) && !barCodeData.equals(container.getText().toString())) {
                labelCode = null;
                store.getText().clear();
                position.getText().clear();
            }
            container.getText().clear();
            container.setText(data);
            getRequest();
        } else if (data.startsWith(Constants.LABEL_HW)) {
            labelCode = data;
            getRequest();
        }
        inboundAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeDialog() {
//        updateView();
    }

    public void backWaste(InboundQuery inboundQuery, int index) {
//        updateView();
        backRequest(inboundQuery, index);
    }

    public void showReasonDialog(InboundQuery inboundQuery) {
        ReasonDialogFragment.newInstance(inboundQuery).show(getSupportFragmentManager(), getLocalClassName());
    }

    //入库查询
    private void getRequest() {
        String url = Constants.SERVER + "mobile-get-in";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("store_label_code", store.getText().toString());//库
        params.put("position_label_code", position.getText().toString());//库位
        params.put("container_label_code", container.getText().toString());//容器
        params.put("label_code", labelCode);//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, InboundQueryBean.class, new Gson().toJson(params), new Response.Listener<InboundQueryBean>() {
                    @Override
                    public void onResponse(InboundQueryBean response) {
                        list.clear();
                        if (response.getData().size() > 0) {
//                            if (TextUtils.isEmpty(store.getText().toString())) {
                            store.setText(response.getData().get(0).getStore_label_code());
//                            }
//                            if (TextUtils.isEmpty(position.getText().toString())) {
                            position.setText(response.getData().get(0).getPosition_label_code());
//                            }
//                            if (TextUtils.isEmpty(container.getText().toString())) {
                            container.setText(response.getData().get(0).getContainer_label_code());
//                            }
//                            if (TextUtils.isEmpty(weight.getText().toString())) {
//                            }
                            /*if (response.getData().get(0).getTotal_weight() != null) {
                                weight.setText("" + response.getData().get(0).getTotal_weight());
                            } else {
                                weight.getText().clear();
                            }*/
                            if (!TextUtils.isEmpty(response.getData().get(0).getLabel_code())) {
                                list.addAll(response.getData());
                            }
                        }
                        inboundAdapter.calculate();
                        inboundAdapter.notifyDataSetChanged();
                        if (list.size() <= 0) {
                            weight.getText().clear();
                        }
                        if (Constants.WARNING.equals(response.getType())) {
                            Toast.makeText(context, response.getContent(), Toast.LENGTH_SHORT).show();
                        }
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
                }), getLocalClassName());
    }

    //入库提交
    private void submitRequest() {
        String url = Constants.SERVER + "mobile-get-in/weight";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("list", list);
        params.put("store_label_code", store.getText().toString());//库
        params.put("position_label_code", position.getText().toString());//库位
        params.put("container_label_code", container.getText().toString());//容器
//        params.put("label_code", labelCode);//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        Toast.makeText(context, "操作成功!", Toast.LENGTH_SHORT).show();
                        labelCode = null;
                        list.clear();
                        store.getText().clear();
                        position.getText().clear();
                        container.getText().clear();
                        weight.getText().clear();
                        inboundAdapter.notifyDataSetChanged();
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
                }), getLocalClassName());
    }

    //固废退回
    private void backRequest(final InboundQuery inboundQuery, int index) {
        String url = Constants.SERVER + "mobile-get-in/back";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("store_label_code", store.getText().toString());//库
        params.put("transfer_detail_id", inboundQuery.getTransfer_detail_id());
        params.put("status", Constants.WASTE_BACK);
        params.put("back_reason", getResources().getStringArray(R.array.reason)[index]);
        params.put("back_reason_index", index);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, InboundQueryBean.class, new Gson().toJson(params), new Response.Listener<InboundQueryBean>() {
                    @Override
                    public void onResponse(InboundQueryBean response) {
                        list.remove(inboundQuery);
                        inboundAdapter.calculate();
                        inboundAdapter.notifyDataSetChanged();
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
                }), getLocalClassName());
    }
}
