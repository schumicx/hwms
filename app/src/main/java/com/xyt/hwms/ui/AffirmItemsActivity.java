package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    private List<Integer> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        Intent intent = new Intent(getBaseContext(), AffirmDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("customer", list.get(position));
//        bundle.putBoolean("isEdit", false);
//        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
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
}
