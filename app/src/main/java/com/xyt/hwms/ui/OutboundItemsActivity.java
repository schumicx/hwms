package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;
import com.xyt.hwms.adapter.OutboundItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class OutboundItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    private List<Integer> list = new ArrayList<>();
    private OutboundItemsAdapter outboundItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.add(1);
        list.add(1);
        list.add(1);
        if (outboundItemsAdapter == null) {
            outboundItemsAdapter = new OutboundItemsAdapter(context, list);
        }
        listview.setAdapter(outboundItemsAdapter);
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
}
