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
import com.xyt.hwms.bean.EADMsgObject;
import com.xyt.hwms.bean.EADObject;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                //Affirm
                Intent intent1 = new Intent(getBaseContext(), AffirmActivity.class);
                intent1.putExtra("type",Constants.TRANSFER_TYPE_OUTER);
                startActivity(intent1);
                break;
            case R.id.button2:
                //Affirm In
                Intent intent2 = new Intent(getBaseContext(), AffirmActivity.class);
                intent2.putExtra("type",Constants.TRANSFER_TYPE_INNER);
                startActivity(intent2);
                break;
            case R.id.button3:
                //Inbound
                startActivity(new Intent(getBaseContext(), InboundActivity.class));
                break;
            case R.id.button4:
                //Recycle
                startActivity(new Intent(getBaseContext(), RecycleActivity.class));
                break;
            case R.id.button5:
                //Outbound
                startActivity(new Intent(getBaseContext(), OutboundActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!PreferencesUtils.getBoolean(context, "isSync", false) && !TextUtils.isEmpty(PreferencesUtils.getString(context, "affirm"))) {
            SyncDialogFragment.newInstance().show(getSupportFragmentManager(), getLocalClassName());
        } else {
            obtainRequest();
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
//        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url + "?_username=develop&_password=whchem@2016", EADObject.class, null, new Response.Listener<EADObject>() {
                    @Override
                    public void onResponse(EADObject response) {
                        if (response.getData().getCollection().size() > 0) {
                            String a = new Gson().toJson(response.getData());
                            PreferencesUtils.putString(context, "affirm", new Gson().toJson(response.getData()));
                            PreferencesUtils.putBoolean(context, "isSync", true);
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
