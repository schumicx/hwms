package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.TransferList;
import com.xyt.hwms.bean.TransferListBean;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static long back_pressed;

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                //Affirm
                Intent intent1 = new Intent(context, AffirmActivity.class);
                intent1.putExtra("type", Constants.TRANSFER_TYPE_OUTER);
                startActivity(intent1);
                break;
            case R.id.button2:
                //Affirm In
                Intent intent2 = new Intent(context, AffirmActivity.class);
                intent2.putExtra("type", Constants.TRANSFER_TYPE_INNER);
                startActivity(intent2);
                break;
            case R.id.button3:
                //Inbound
                startActivity(new Intent(context, InboundActivity.class));
                break;
            case R.id.button4:
                //Recycle
                startActivity(new Intent(context, RecycleActivity.class));
                break;
            case R.id.button5:
                //Outbound
                startActivity(new Intent(context, OutboundActivity.class));
                break;
            case R.id.button6:
                //Logout
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
        } else {
            obtainRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
            syncRequest();
        }
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
    private void obtainRequest() {
        String url = Constants.SERVER + "mobile-hwit";
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url, TransferListBean.class, null, new Response.Listener<TransferListBean>() {
                    @Override
                    public void onResponse(TransferListBean response) {
                        if (response.getData().getCollection().size() > 0) {
                            PreferencesUtils.putString(context, "affirm", new Gson().toJson(response.getData()));
                            PreferencesUtils.putBoolean(context, "isSync", true);
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
                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }

    //同步固废转移单
    public void syncRequest() {
        String url = Constants.SERVER + "mobile-hwit";
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.PUT, url, BaseBean.class, new Gson().toJson(new Gson().fromJson(PreferencesUtils.getString(context, "affirm"), TransferList.class).getCollection()), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        Toast.makeText(context, "同步成功!", Toast.LENGTH_SHORT).show();
                        PreferencesUtils.putString(context, "affirm", null);
                        PreferencesUtils.putBoolean(context, "isSync", true);
                        Constants.AFFIRM_LIST = null;
                        obtainRequest();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
                            SyncDialogFragment.newInstance().show(getSupportFragmentManager(), getLocalClassName());
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

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
