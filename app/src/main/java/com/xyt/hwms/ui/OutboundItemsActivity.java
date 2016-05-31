package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.OutboundItemsAdapter;
import com.xyt.hwms.bean.EADMsgObject;
import com.xyt.hwms.bean.EADObject;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutboundItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    private List<Map> list = new ArrayList<>();
    private OutboundItemsAdapter outboundItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (outboundItemsAdapter == null) {
            outboundItemsAdapter = new OutboundItemsAdapter(context, list);
        }
        listview.setAdapter(outboundItemsAdapter);
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
        Toast.makeText(getBaseContext(), "xxxxxxxxxxx-----" + data, Toast.LENGTH_SHORT).show();
        request();
    }

    @Override
    public void getBarcode(String data) {
        barCodeData = data;
    }

    @Override
    public void closeAffirmDialog() {
    }

    //出库查询
    private void request() {
        String url = Constants.SERVER + "mobile-hwiu/" + "id" + "/detail";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("_username", "develop");
        params.put("_password", "whchem@2016");
        params.put("store_label_code", barCodeData);//库
        params.put("position_label_code", barCodeData);//库位
        params.put("container_label_code", barCodeData);//容器
        params.put("label_code", barCodeData);//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, EADObject.class, params, new Response.Listener<EADObject>() {
                    @Override
                    public void onResponse(EADObject response) {
                        if (response.getData().getCollection() != null && response.getData().getCollection().size() > 0) {
                            list.clear();
                            list.addAll(response.getData().getCollection());
                        }
                        if (list.size() == 0) {
                            empty.setText("no data");
                            empty.setVisibility(View.VISIBLE);
                        }
                        outboundItemsAdapter.notifyDataSetChanged();
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
                }), getLocalClassName());
    }

    //出库
    private void submitRequest() {
        String url = Constants.SERVER + "mobile-hwiu/" + "id" + "/detail";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("label_code", barCodeData);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url + "?_username=develop&_password=whchem@2016", EADMsgObject.class, new Gson().toJson(params), new Response.Listener<EADMsgObject>() {
                    @Override
                    public void onResponse(EADMsgObject response) {
                        Map m = new HashMap();
                        m.put("label_code", barCodeData);
                        list.add(0, m);
                        outboundItemsAdapter.notifyDataSetChanged();
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
                }), getLocalClassName());
    }
}
