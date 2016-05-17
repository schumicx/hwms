package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.GsonObjectRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                //Affirm
                startActivity(new Intent(getBaseContext(), AffirmActivity.class));
                break;
            case R.id.button2:
                //Group
                startActivity(new Intent(getBaseContext(), GroupActivity.class));
                break;
            case R.id.button3:
                //Weigh
                startActivity(new Intent(getBaseContext(), WeighActivity.class));
                break;
            case R.id.button4:
                //Inbound
                startActivity(new Intent(getBaseContext(), InboundActivity.class));
                break;
            case R.id.button5:
                //Outbound
                startActivity(new Intent(getBaseContext(), OutboundActivity.class));
                break;
            case R.id.button6:
                //Recycle
                startActivity(new Intent(getBaseContext(), RecycleActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void getTagId(String data) {
        Toast.makeText(getBaseContext(), "bbb-----"+data, Toast.LENGTH_SHORT).show();
    }
}
