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
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.Transfer;
import com.xyt.hwms.bean.TransferList;
import com.xyt.hwms.bean.TransferListBean;
import com.xyt.hwms.bean.User;
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
import butterknife.OnItemClick;

public class AffirmActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.empty)
    TextView empty;
    private List<Transfer> list = new ArrayList<>();
    private AffirmAdapter affirmAdapter;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        if (Constants.AFFIRM_LIST.getCollection().get(position).getDetail() == null) {
            Toast.makeText(context, "该转移单没有转移明细!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, AffirmItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("type", getIntent().getStringExtra("type"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdialog.setMessage("同步中...");
        if (Constants.TRANSFER_TYPE_OUTER.equals(getIntent().getStringExtra("type"))) {
            getSupportActionBar().setTitle(R.string.main_item1);
        } else if (Constants.TRANSFER_TYPE_INNER.equals(getIntent().getStringExtra("type"))) {
            getSupportActionBar().setTitle(R.string.main_item2);
        }

        if (affirmAdapter == null) {
            affirmAdapter = new AffirmAdapter(context, list, getIntent().getStringExtra("type"));
        }
        listview.setAdapter(affirmAdapter);

        swiperefresh.setProgressViewOffset(true, 0, BaseUtils.dip2px(context, 24));
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

        Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class);
        if (Constants.AFFIRM_LIST != null && Constants.AFFIRM_LIST.getCollection().size() > 0) {
            list.addAll(Constants.AFFIRM_LIST.getCollection());
            affirmAdapter.notifyDataSetChanged();
        }

        empty.setVisibility(View.VISIBLE);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTransfer_type().equals(getIntent().getStringExtra("type"))) {
                empty.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        writeFileToSD(new Gson().toJson(new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class).getCollection()));
        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
            syncRequest();
        }
        affirmAdapter.notifyDataSetChanged();
    }

//    private void writeFileToSD(String s) {
//        String sdStatus = Environment.getExternalStorageState();
//        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
//            return;
//        }
//        try {
//            String pathName="/sdcard/test/";
//            String fileName="file.txt";
//            File path = new File(pathName);
//            File file = new File(pathName + fileName);
//            if( !path.exists()) {
//                path.mkdir();
//            }
//            if( !file.exists()) {
//                file.createNewFile();
//            }
//            FileOutputStream stream = new FileOutputStream(file);
//            byte[] buf = s.getBytes();
//            stream.write(buf);
//            stream.close();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdialog.isShowing()) {
            pdialog.dismiss();
        }
        ApplicationController.getInstance().cancelPendingRequests(getLocalClassName());
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
                if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
                    syncRequest();
                } else {
                    Toast.makeText(context, "最新数据,无需同步!", Toast.LENGTH_SHORT).show();
                }
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
    }

    @Override
    public void closeDialog() {
    }

    //获取固废转移单
    public void obtainRequest() {
//        PreferencesUtils.putString(context, "affirm", null);
        String url = Constants.SERVER + "mobile-hwit";
        Map<String, Object> params = new HashMap<>();
        params.put("card_id", new Gson().fromJson(PreferencesUtils.getString(context, "user"), User.class).getCard_id());
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, TransferListBean.class, params, new Response.Listener<TransferListBean>() {
                    @Override
                    public void onResponse(TransferListBean response) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
                        list.clear();
                        PreferencesUtils.putBoolean(context, "isSync", true);
                        PreferencesUtils.putString(context, "affirm", new Gson().toJson(response.getData()));
                        if (response.getData().getCollection().size() > 0) {
                            Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class);
                            list.addAll(Constants.AFFIRM_LIST.getCollection());
                        }
                        empty.setVisibility(View.VISIBLE);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTransfer_type().equals(getIntent().getStringExtra("type"))) {
                                empty.setVisibility(View.GONE);
                                break;
                            }
                        }
                        affirmAdapter.notifyDataSetChanged();
                        if (pdialog.isShowing()) {
                            pdialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
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
                        list.clear();
                        Constants.AFFIRM_LIST = new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class);
                        if (Constants.AFFIRM_LIST != null && Constants.AFFIRM_LIST.getCollection().size() > 0) {
                            list.addAll(Constants.AFFIRM_LIST.getCollection());
                            affirmAdapter.notifyDataSetChanged();
                        }
                        empty.setVisibility(View.VISIBLE);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getTransfer_type().equals(getIntent().getStringExtra("type"))) {
                                empty.setVisibility(View.GONE);
                                break;
                            }
                        }
                        if (pdialog.isShowing()) {
                            pdialog.dismiss();
                        }
                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }

    //同步固废转移单
    public void syncRequest() {
//        final Long t = System.currentTimeMillis();
        pdialog.show();
        String url = Constants.SERVER + "mobile-hwit/sync";
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class).getCollection()), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
//                        Toast.makeText(context, "time:" + ((System.currentTimeMillis() - t) / 1000) / 60 + ":" + ((System.currentTimeMillis() - t) / 1000) % 60, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "同步成功!", Toast.LENGTH_SHORT).show();
                        PreferencesUtils.putString(context, "affirm", null);
                        PreferencesUtils.putBoolean(context, "isSync", true);
                        Constants.AFFIRM_LIST = null;
                        obtainRequest();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pdialog.isShowing()) {
                            pdialog.dismiss();
                        }
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
