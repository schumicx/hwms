package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;
import com.xyt.hwms.bean.TransferDetail;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmItemsActivity extends BaseActivity {

    public CacheWasteDialogFragment affirmDialog;
    @BindView(R.id.listview)
    ListView listview;
    TextView total;
    TextView verified;
    TextView unverified;
    TextView back;
    private List<TransferDetail> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;
    private int applyIndex;
    //    private int position;
    private int totalNum;
    private int verifiedNum;
    private int unverifiedNum;
    private int backNum;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
//        this.position = position - 1;
        affirmDialog = CacheWasteDialogFragment.newInstance(applyIndex, position - 1);
        affirmDialog.show(getSupportFragmentManager(), getLocalClassName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applyIndex = getIntent().getIntExtra("position", 0);
        list.addAll(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail());

        View head = getLayoutInflater().inflate(R.layout.list_head_affirm_items, null);
        total = (TextView) head.findViewById(R.id.total);
        verified = (TextView) head.findViewById(R.id.verified);
        unverified = (TextView) head.findViewById(R.id.unverified);
        back = (TextView) head.findViewById(R.id.back);
        EditText operator = (EditText) head.findViewById(R.id.operator);

        totalNum = list.size();
        total.setText("" + totalNum);

        listview.addHeaderView(head);

        if (affirmItemsAdapter == null) {
            affirmItemsAdapter = new AffirmItemsAdapter(context, list);
        }
        listview.setAdapter(affirmItemsAdapter);
        updateView();

        operator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //////////
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.group:
                Intent intent = new Intent(context, GroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("applyIndex", applyIndex);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTagId(String data) {
    }

    @Override
    public void getBarcode(String data) {
        if (affirmDialog != null) {
            affirmDialog.dismiss();
        }
        for (int i = 0; i < list.size(); i++) {
            if (data.equals(list.get(i).getLabel_code())) {
//                this.position = i;
                affirmDialog = CacheWasteDialogFragment.newInstance(applyIndex, i);
                affirmDialog.show(getSupportFragmentManager(), getLocalClassName());
                break;
            }
        }
    }

    @Override
    public void closeDialog() {
        updateView();
        affirmDialog = null;
    }

    public void updateView() {
//        if (Constants.WASTE_BACK.equals(((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(position)).get("status").toString())) {
//            if(reasonDialog == null) {
//                reasonDialog = ReasonDialogFragment.newInstance(applyIndex, position);
//                reasonDialog.show(getSupportFragmentManager(), getLocalClassName());
//            }
//        }
        backNum = 0;
        verifiedNum = 0;
        unverifiedNum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (Constants.WASTE_BACK.equals(list.get(i).getStatus())) {
                backNum++;

//                ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status", 0);//////////
            } else if (Constants.WASTE_PASS.equals(list.get(i).getStatus())) {
                verifiedNum++;
//                ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status", 1);//////////
            } else {
                unverifiedNum++;
            }

//            ((Map) Constants.AFFIRM_LIST.get(applyIndex)).put("detail_status",);
        }
        back.setText("" + backNum);
        verified.setText("" + verifiedNum);
        unverified.setText("" + unverifiedNum);
        Constants.AFFIRM_LIST.getCollection().get(applyIndex).setOperator("xxxxxxxx");
        PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
        affirmItemsAdapter.notifyDataSetChanged();
    }
}
