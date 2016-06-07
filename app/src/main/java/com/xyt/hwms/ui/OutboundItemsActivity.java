package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.Menu;
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

public class OutboundItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    private List<Outbound> list = new ArrayList<>();
    private List<OutboundDetail> querylist = new ArrayList<>();
    private OutboundItemsAdapter outboundItemsAdapter;
    private String id;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.complete:
                completeRequest();
                break;
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
        request();
    }

    @Override
    public void closeDialog() {
        listRequest();
    }

    //出库查询
    private void listRequest() {
        String url = Constants.SERVER + "mobile-out-store";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("_username", "develop");
        params.put("_password", "whchem@2016");
        params.put("transfer_id", id);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, OutboundListBean.class, params, new Response.Listener<OutboundListBean>() {
                    @Override
                    public void onResponse(OutboundListBean response) {
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

    //出库明细查询
    private void request() {
        String url = Constants.SERVER + "mobile-get-in/";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("_username", "develop");
        params.put("_password", "whchem@2016");
        if (barCodeData.startsWith(Constants.LABEL_LIB)) {
            params.put("store_label_code", barCodeData);//库
        } else if (barCodeData.startsWith(Constants.LABEL_LSL)) {
            params.put("position_label_code", barCodeData);//库位
        } else if (barCodeData.startsWith(Constants.LABEL_CON)) {
            params.put("container_label_code", barCodeData);//容器
        } else if (barCodeData.startsWith(Constants.LABEL_HW)) {
            params.put("label_code", barCodeData);//固废
        }

        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, OutboundDetailListBean.class, params, new Response.Listener<OutboundDetailListBean>() {
                    @Override
                    public void onResponse(OutboundDetailListBean response) {
                        if (response.getData().getCollection() != null && response.getData().getCollection().size() > 0) {
                            querylist.clear();
                            querylist.addAll(response.getData().getCollection());
                        }
                        OutboundDialogFragment.newInstance(querylist).show(getSupportFragmentManager(), getLocalClassName());
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
    public void submitRequest() {
        String url = Constants.SERVER + "mobile-out-store";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("label_code", barCodeData);
        params.put("transfer_id", id);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url + "?_username=develop&_password=whchem@2016", BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        listRequest();
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

    //出库完成
    public void completeRequest() {
        String url = Constants.SERVER + "mobile-hwot/finish";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("label_code", barCodeData);
        params.put("transfer_id", id);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url + "?_username=develop&_password=whchem@2016", BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        finish();
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
