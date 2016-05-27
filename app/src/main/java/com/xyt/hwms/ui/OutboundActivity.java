package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.OutboundAdapter;
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
import butterknife.OnItemClick;

public class OutboundActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.empty)
    TextView empty;
    private List<Map> list = new ArrayList<>();
    private OutboundAdapter outboundAdapter;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        Intent intent = new Intent(getBaseContext(), OutboundItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (outboundAdapter == null) {
            outboundAdapter = new OutboundAdapter(context, list);
        }
        listview.setAdapter(outboundAdapter);

        swiperefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request();
            }
        });
        swiperefresh.setProgressViewOffset(true, 0, BaseUtils.dip2px(context, 24));
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
        NFCTagId = data;
        swiperefresh.setRefreshing(true);
        request();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }

    //获取出库转移单
    private void request() {
        if (TextUtils.isEmpty(NFCTagId)) {
            if (swiperefresh.isRefreshing()) {
                swiperefresh.setRefreshing(false);
            }
            return;
        }
        String url = Constants.SERVER + "hwit-transfer-record";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("_username", "develop");
        params.put("_password", "gbros:{2014}");
        params.put("nfc", NFCTagId);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, EADObject.class, params, new Response.Listener<EADObject>() {
                    @Override
                    public void onResponse(EADObject response) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
                        if (response.getData().getCollection().size() > 0) {
                            list.clear();
                            list.addAll(response.getData().getCollection());
                        }
                        if (list.size() == 0) {
                            empty.setText("no data");
                            empty.setVisibility(View.VISIBLE);
                        }
                        outboundAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
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
                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }
}
