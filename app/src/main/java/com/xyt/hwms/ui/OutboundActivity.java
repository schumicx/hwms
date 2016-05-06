package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class OutboundActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    private List<Integer> list = new ArrayList<>();
    private AffirmAdapter affirmAdapter;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        Intent intent = new Intent(getBaseContext(), AffirmItemsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("customer", list.get(position));
//        bundle.putBoolean("isEdit", false);
//        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.add(1);
        if (affirmAdapter == null) {
            affirmAdapter = new AffirmAdapter(context, list);
        }
        listview.setAdapter(affirmAdapter);
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
