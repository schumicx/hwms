package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;
import com.xyt.hwms.support.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmItemsActivity extends BaseActivity {

    public AffirmDetailsDialogFragment dialog;
    @BindView(R.id.listview)
    ListView listview;
    private List<Map> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;
    private int applyIndex;
    private int position;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        this.position = position - 1;
        dialog = AffirmDetailsDialogFragment.newInstance(applyIndex, position - 1);
        dialog.show(getSupportFragmentManager(), getLocalClassName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applyIndex = getIntent().getIntExtra("position", 0);
        list.addAll((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail"));

        View head = getLayoutInflater().inflate(R.layout.list_head_affirm_items, null);
        listview.addHeaderView(head);

        if (affirmItemsAdapter == null) {
            affirmItemsAdapter = new AffirmItemsAdapter(context, list);
        }
        listview.setAdapter(affirmItemsAdapter);
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
        Toast.makeText(getBaseContext(), (String) list.get(0).get("waste_detail_id"), Toast.LENGTH_SHORT).show();
        if (dialog != null) {
            dialog.dismiss();
        }
        for (int i = 0; i < list.size(); i++) {
            if ("0154985b79348a8ae61a538908b447e5".equals((String) list.get(i).get("waste_detail_id"))) {
                this.position = i;
                dialog = AffirmDetailsDialogFragment.newInstance(applyIndex, position);
                dialog.show(getSupportFragmentManager(), getLocalClassName());
                break;
            }
        }
    }

    @Override
    public void getBarcode(String data) {
        Toast.makeText(context, "Barcode:" + data, Toast.LENGTH_SHORT).show();
    }

    public void updateView() {
        if (((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(position)).get("status").toString().equals("退回")) {
            ReasonDialogFragment.newInstance(applyIndex, position).show(getSupportFragmentManager(), getLocalClassName());
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("status").toString().equals("退回")) {

            } else if (list.get(i).get("status").toString().equals("接受")) {

            } else {

            }
        }
        affirmItemsAdapter.notifyDataSetChanged();
    }
}
