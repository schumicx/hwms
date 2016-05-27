package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.GroupAdapter;
import com.xyt.hwms.adapter.RecycleItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    private List<Integer> list = new ArrayList<>();
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View head = getLayoutInflater().inflate(R.layout.list_head_group, null);
        listview.addHeaderView(head);

        list.add(1);
        list.add(1);
        if (groupAdapter == null) {
            groupAdapter = new GroupAdapter(context, list);
        }
        listview.setAdapter(groupAdapter);
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
        Toast.makeText(getBaseContext(), "aaa-----"+data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }
}
