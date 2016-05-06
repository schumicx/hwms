package com.xyt.hwms.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected Context context;
    protected int pageNum = 0;
    protected int visibleLastIndex = 0;
    protected int curPageSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }
}
