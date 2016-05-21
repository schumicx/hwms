package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AffirmDetailsActivity extends BaseActivity {

    @BindView(R.id.detail)
    TextView detail;

    private Map map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        map = (Map) getIntent().getSerializableExtra("detail");
        detail.setText(new Gson().toJson(map));
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
}
