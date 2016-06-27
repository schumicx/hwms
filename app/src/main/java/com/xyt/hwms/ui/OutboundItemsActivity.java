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
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.Outbound;
import com.xyt.hwms.bean.OutboundDetail;
import com.xyt.hwms.bean.OutboundDetailListBean;
import com.xyt.hwms.bean.OutboundListBean;
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

public class OutboundItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    private List<Outbound> list = new ArrayList<>();
    private List<OutboundDetail> querylist = new ArrayList<>();
    private OutboundItemsAdapter outboundItemsAdapter;
    private String id;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        request(list.get(position).getLabel_code(), false, list.get(position).getOut_record_id());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        if (outboundItemsAdapter == null) {
            outboundItemsAdapter = new OutboundItemsAdapter(context, list, id, empty);
        }
        listview.setAdapter(outboundItemsAdapter);

        listRequest();
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
        completeRequest(data);
    }

    @Override
    public void getBarcode(String data) {
        barCodeData = data;
        request(barCodeData, true, null);
    }

    @Override
    public void closeDialog() {
        listRequest();
    }

    //出库查询
    private void listRequest() {
        String url = Constants.SERVER + "mobile-out-store";
        Map<String, Object> params = new HashMap<>();
        params.put("transfer_id", id);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, OutboundListBean.class, params, new Response.Listener<OutboundListBean>() {
                    @Override
                    public void onResponse(OutboundListBean response) {
                        list.clear();
                        if (response.getData().getCollection() != null && response.getData().getCollection().size() > 0) {
                            list.addAll(response.getData().getCollection());
                        }
                        if (list.size() == 0) {
                            empty.setVisibility(View.VISIBLE);
                        } else {
                            empty.setVisibility(View.GONE);
                        }
                        outboundItemsAdapter.notifyDataSetChanged();
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

    //出库明细查询
    private void request(String barCodeData, final boolean isOperate, String outRecordId) {
        String url = Constants.SERVER + (isOperate ? "mobile-get-in" : "mobile-get-out");
        Map<String, Object> params = new HashMap<>();
        params.put("out_record_id", outRecordId);
        if (barCodeData.startsWith(Constants.LABEL_LIB)) {
            params.put("store_label_code", barCodeData);//库
        } else if (barCodeData.startsWith(Constants.LABEL_LSL)) {
            params.put("position_label_code", barCodeData);//库位
        } else if (barCodeData.startsWith(Constants.LABEL_CON)) {
            params.put("container_label_code", barCodeData);//容器
        } else if (barCodeData.startsWith(Constants.LABEL_HW)) {
            params.put("label_code", barCodeData);//固废
        }
        if (isOperate) {
            params.put("status", 1);
        }

        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, OutboundDetailListBean.class, params, new Response.Listener<OutboundDetailListBean>() {
                    @Override
                    public void onResponse(OutboundDetailListBean response) {
                        querylist.clear();
                        if (response.getData().getCollection() != null && response.getData().getCollection().size() > 0) {
                            querylist.addAll(response.getData().getCollection());
                            OutboundDialogFragment.newInstance(querylist, isOperate).show(getSupportFragmentManager(), getLocalClassName());
                        } else {
                            Toast.makeText(context, "该固废已出库!", Toast.LENGTH_SHORT).show();
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

    //出库
    public void submitRequest() {
        String url = Constants.SERVER + "mobile-out-store";
        Map<String, Object> params = new HashMap<>();
        params.put("label_code", barCodeData);
        params.put("transfer_id", id);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        listRequest();
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

    //出库完成
    public void completeRequest(String nfcId) {
        String url = Constants.SERVER + "mobile-hwot/finish";
        Map<String, Object> params = new HashMap<>();
        params.put("transfer_id", id);
        params.put("card_id", nfcId);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        finish();
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
