package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.RecycleItemsAdapter;
import com.xyt.hwms.bean.EADMsgObject;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleItemsActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;
    private List<Map> list = new ArrayList<>();
    private RecycleItemsAdapter recycleItemsAdapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        if (recycleItemsAdapter == null) {
            recycleItemsAdapter = new RecycleItemsAdapter(context, list);
        }
        listview.setAdapter(recycleItemsAdapter);
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
        request();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }

    //内部利用
    private void request() {
        String url = Constants.SERVER + "mobile-hwiu/" + id + "/detail";
//        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("", "gbros:{2014}");
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url + "?_username=develop&_password=gbros:{2014}", EADMsgObject.class, PreferencesUtils.getString(context, "affirm"), new Response.Listener<EADMsgObject>() {
                    @Override
                    public void onResponse(EADMsgObject response) {
//                        if (response.getCode() == 200) {
//                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        Map m = new HashMap();
                        m.put("k", System.currentTimeMillis() + "");
                        list.add(0, m);
                        recycleItemsAdapter.notifyDataSetChanged();
//                        } else {
//                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                        }
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
