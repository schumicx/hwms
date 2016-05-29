package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.xyt.hwms.R;

public class InboundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound);

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
