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
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.InboundAdapter;
import com.xyt.hwms.bean.EADMsgObject;
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

public class InboundActivity extends BaseActivity {

    public WasteDialogFragment wasteDialog;
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

    private List<Map> list = new ArrayList<>();
    private InboundAdapter inboundAdapter;

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
        listview.addHeaderView(head,null,true);

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

                s = TextUtils.isEmpty(s) ? "0" : s;
                if(weight.isFocused()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).put("itemWeight", Double.valueOf(s.toString()) / list.size());
                    }
                    inboundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        Toast.makeText(getBaseContext(), "xxxxxxxxxxx-----" + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
        if (data.startsWith(Constants.LABEL_LIB)) {
            store.getText().clear();
            store.setText(data);
        } else if (data.startsWith(Constants.LABEL_LSL)) {
            position.getText().clear();
            position.setText(data);
        } else if (data.startsWith(Constants.LABEL_CON)) {
            container.getText().clear();
            container.setText(data);
        } else if (data.startsWith(Constants.LABEL_HW)) {
            if (wasteDialog != null) {
                wasteDialog.dismiss();
            }

            wasteDialog = WasteDialogFragment.newInstance();
            wasteDialog.show(getSupportFragmentManager(), getLocalClassName());
            getRequest();

        }
        inboundAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeDialog() {
        for (int i = 0; i < list.size(); i++) {
            if (Constants.WASTE_BACK.equals((String) list.get(i).get("status"))) {
                list.remove(i);
                break;
            }
        }
        updateView();
        wasteDialog = null;
    }

    public void updateView() {
//        for (int i = 0; i < list.size(); i++) {
//            if (Constants.WASTE_BACK.equals(list.get(i).get("status").toString())) {
////                ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status", 0);//////////
//            } else if (Constants.WASTE_PASS.equals(list.get(i).get("status").toString())) {
////                ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status", 1);//////////
//            } else {
//
//            }
////            ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status",);
//        }
        inboundAdapter.notifyDataSetChanged();
    }

    //获取固废
    private void getRequest() {
        String url = Constants.SERVER + "mobile-hwiu";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
        params.put("label_code", barCodeData);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url + "?_username=develop&_password=whchem@2016", EADMsgObject.class, null, new Response.Listener<EADMsgObject>() {
                    @Override
                    public void onResponse(EADMsgObject response) {
                        Map m = new HashMap();
                        m.put("label_code", barCodeData);
                        list.add(0, m);
                        inboundAdapter.notifyDataSetChanged();
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
