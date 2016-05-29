package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xyt.hwms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeighActivity extends BaseActivity {

    @BindView(R.id.password)
    EditText password;

    @OnClick(R.id.email_sign_in_button)
    public void onClick(View v) {
        Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
        password.setText("sasa");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weigh);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("XXXXXXX");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Toast.makeText(getBaseContext(), "xxxxxxxxxxx-----"+data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeAffirmDialog() {
    }
}
