package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmAdapter;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                pageNum = Constants.STARTPAGE;
                request();
            }
        });

        request();
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
            request();
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && curPageSize < Constants.PAGESIZE && curPageSize > 0) {
            Toast.makeText(context, "XXXX", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }

    @Override
    public void getTagId(String data) {
        Toast.makeText(getBaseContext(), "xxxxxxxxxxx-----"+data, Toast.LENGTH_SHORT).show();
    }

    //获取固废转移单
    private void request() {
        String url = Constants.SERVER + "hwit-transfer-apply";
        Map<String, Object> params = new HashMap<>();
//        params.put("tokenId", PreferencesUtils.getString(context, Constants.TOKEN));
//        params.put("pageNum", pageNum);
//        params.put("pageSize", );
        params.put("_username", "develop");
        params.put("_password", "gbros:{2014}");
        params.put(Constants.PAGE, pageNum);
        params.put(Constants.SIZE, Constants.PAGESIZE);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(url, BaseBean.class, params, new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
//                        if (response.getRet() == Constants.SUCCESS) {
//                            if (pageNum == Constants.STARTPAGE) {
//                                list.clear();
//                            }
//                            if (response.getList().size() > 0) {
//                                list.addAll(response.getList());
//                                curPageSize = response.getList().size();
//                            } else if (pageNum != Constants.STARTPAGE) {
//                                SuperToast.create(context, context.getResources().getString(R.string.noMoredata), SuperToast.Duration.SHORT, SuperToast.Animations.SCALE).show();
//                            }
////                            if (list.size() == 0 && pageNum == Constants.STARTPAGE) {
////                                empty.setText(getResources().getString(R.string.customerNoData));
////                                empty.setVisibility(View.VISIBLE);
////                            }
//                            if (curPageSize == Constants.PAGESIZE) {
//                                pageNum++;
//                            }
//                            affirmAdapter.notifyDataSetChanged();
//                        } else {
//                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                        }
                     }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (swiperefresh.isRefreshing()) {
                            swiperefresh.setRefreshing(false);
                        }
//                        if (!TextUtils.isEmpty(error.getMessage())) {
//                            SuperToast.create(context, context.getResources().getString(R.string.request_error), SuperToast.Duration.SHORT, SuperToast.Animations.SCALE).show();
//                        } else {
//                            SuperToast.create(context, context.getResources().getString(R.string.request_timeout), SuperToast.Duration.SHORT, SuperToast.Animations.SCALE).show();
//                        }
                    }
                }), getLocalClassName());
    }
}
