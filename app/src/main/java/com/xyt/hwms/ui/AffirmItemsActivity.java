package com.xyt.hwms.ui;

import android.os.Bundle;
import android.view.MenuItem;
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

    @BindView(R.id.listview)
    ListView listview;
    public AffirmDetailsDialogFragment dialog;
    private List<Map> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;
    private int applyIndex;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        dialog = AffirmDetailsDialogFragment.newInstance(applyIndex, position);
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
                dialog = AffirmDetailsDialogFragment.newInstance(applyIndex, i);
                dialog.show(getSupportFragmentManager(), getLocalClassName());
                break;
            }
        }
    }

    public void updateView() {
        affirmItemsAdapter.notifyDataSetChanged();
    }
}
