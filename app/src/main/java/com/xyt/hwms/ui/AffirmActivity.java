package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmAdapter;
import com.xyt.hwms.support.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmActivity extends BaseActivity implements AbsListView.OnScrollListener {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
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
        setContentView(R.layout.activity_affirm);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("XXXXXXX");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        if (affirmAdapter == null) {
            affirmAdapter = new AffirmAdapter(context, list);
        }
        listview.setAdapter(affirmAdapter);
        listview.setOnScrollListener(this);
        swiperefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
//                cusRequest();
            }
        });
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = affirmAdapter.getCount() - 1;
        int lastIndex = itemsLastIndex; // headerView,footView各＋1
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && curPageSize == Constants.PAGESIZE) {
            swiperefresh.setRefreshing(true);
            curPageSize = 0;
//            cusRequestest();
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && curPageSize < Constants.PAGESIZE && curPageSize > 0) {
            Toast.makeText(context, "XXXX", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }
}
