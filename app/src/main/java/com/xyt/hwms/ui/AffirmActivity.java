package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
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
import com.xyt.hwms.adapter.AffirmAdapter;
import com.xyt.hwms.bean.EADMsgObject;
import com.xyt.hwms.bean.EADObject;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.empty)
    TextView empty;
    private List<Map> list = new ArrayList<>();
    private AffirmAdapter affirmAdapter;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        Intent intent = new Intent(context, AffirmItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (affirmAdapter == null) {
            affirmAdapter = new AffirmAdapter(context, list);
        }
        listview.setAdapter(affirmAdapter);

        swiperefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
                    swiperefresh.setRefreshing(false);
                    SyncDialogFragment.newInstance().show(getSupportFragmentManager(), getLocalClassName());
                } else {
                    obtainRequest();
                }
            }
        });

        swiperefresh.setProgressViewOffset(true, 0, BaseUtils.dip2px(context, 24));

        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
            Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), List.class);
            list.addAll(Constants.AFFIRM_LIST);
            SyncDialogFragment.newInstance().show(getSupportFragmentManager(), getLocalClassName());
        } else {
            swiperefresh.setRefreshing(true);
            obtainRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync, menu);
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
            case R.id.sync:
                if (!PreferencesUtils.getBoolean(context, "isSync", false)) {
                    syncRequest();
                } else {
                    Toast.makeText(context, "最新,无需同步", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTagId(String data) {
        Toast.makeText(context, "NFC TagId:" + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeDialog() {
    }

    //获取固废转移单
    private void obtainRequest() {
        String url = Constants.SERVER + "mobile-hwit";
//        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));inner/outer
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url + "?_username=develop&_password=whchem@2016&transfer_type=outer", EADObject.class, null, new Response.Listener<EADObject>() {
                    @Override
                    public void onResponse(EADObject response) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
                        if (response.getData().getCollection().size() > 0) {
                            list.clear();
                            PreferencesUtils.putString(context, "affirm", new Gson().toJson(response.getData().getCollection()));
                            PreferencesUtils.putBoolean(context, "isSync", true);
                            Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), List.class);
                            list.addAll(Constants.AFFIRM_LIST);
                        }
                        if (list.size() == 0) {
                            empty.setText("no data");
                            empty.setVisibility(View.VISIBLE);
                        }
                        affirmAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), EADMsgObject.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), List.class);
                        list.addAll(Constants.AFFIRM_LIST);
                        affirmAdapter.notifyDataSetChanged();

                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }

    //同步固废转移单
    public void syncRequest() {
        String url = Constants.SERVER + "mobile-hwit";
//        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("", "gbros:{2014}");
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.PUT, url + "?_username=develop&_password=whchem@2016", EADMsgObject.class, PreferencesUtils.getString(context, "affirm"), new Response.Listener<EADMsgObject>() {
                    @Override
                    public void onResponse(EADMsgObject response) {
                        if (response.getCode() == 200) {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            PreferencesUtils.putString(context, "affirm", null);
                            PreferencesUtils.putBoolean(context, "isSync", true);
                            Constants.AFFIRM_LIST = null;
                            obtainRequest();
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), EADMsgObject.class).getContent(), Toast.LENGTH_SHORT).show();
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
