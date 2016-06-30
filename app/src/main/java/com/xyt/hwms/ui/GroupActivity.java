package com.xyt.hwms.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.GroupAdapter;
import com.xyt.hwms.bean.TransferDetail;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class GroupActivity extends BaseActivity {

    public CacheWasteDialogFragment affirmDialog;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.submit)
    Button submit;
    private List<TransferDetail> list = new ArrayList<>();
    private List<TransferDetail> listData = new ArrayList<>();
    private GroupAdapter groupAdapter;
    private int applyIndex;
    private int position;
    private EditText container;

    @OnClick(R.id.submit)
    public void onClick(View v) {
        if (list.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).setContainer_label_code(null);
                for (int j = 0; j < list.size(); j++) {
                    if (listData.get(i).getLabel_code().equals(list.get(j).getLabel_code())) {
                        listData.get(i).setContainer_label_code(container.getText().toString());
                        break;
                    }
                }
            }
            PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
            PreferencesUtils.putBoolean(context, "isSync", false);
            Toast.makeText(context, "组盘成功!", Toast.LENGTH_SHORT).show();
            list.clear();
            groupAdapter.notifyDataSetChanged();
        }
        container.getText().clear();
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        this.position = position - 1;
        affirmDialog = CacheWasteDialogFragment.newInstance(applyIndex, position - 1);
        affirmDialog.show(getSupportFragmentManager(), getLocalClassName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applyIndex = getIntent().getIntExtra("applyIndex", 0);
        listData = Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail();

        View head = getLayoutInflater().inflate(R.layout.list_head_group, null);
        container = (EditText) head.findViewById(R.id.container);
        listview.addHeaderView(head);

        if (groupAdapter == null) {
            groupAdapter = new GroupAdapter(context, list, listData);
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
    }

    @Override
    public void getBarcode(String data) {
        if (data.startsWith(Constants.LABEL_CON)) {
            container.setText(data);
            list.clear();
            for (int i = 0; i < listData.size(); i++) {
                if (data.equals(listData.get(i).getContainer_label_code())) {
                    list.add(0, listData.get(i));
                }
            }
        } else if (data.startsWith(Constants.LABEL_HW)) {
            if (affirmDialog != null) {
                affirmDialog.dismiss();
            }
            if (TextUtils.isEmpty(container.getText().toString().trim())) {
                Toast.makeText(context, "请先扫描容器!", Toast.LENGTH_SHORT).show();
                return;
            }
            int l = 0;
            for (int i = 0; i < listData.size(); i++) {
                if (data.equals(listData.get(i).getLabel_code())) {
                    this.position = i;
                    if (list.size() > 0) {
                        int k = 0;
                        for (int j = 0; j < list.size(); j++) {
                            if (!(listData.get(i).getLabel_code()).equals(list.get(j).getLabel_code())) {
                                k++;
                            } else {
                                break;
                            }
                        }
                        if (k >= list.size()) {
                            list.add(0, listData.get(i));
                        }
                    } else {
                        list.add(0, listData.get(i));
                    }
                    affirmDialog = CacheWasteDialogFragment.newInstance(applyIndex, position);
                    affirmDialog.show(getSupportFragmentManager(), getLocalClassName());
                    break;
                } else {
                    l++;
                }
            }
            if (l >= listData.size()) {
                Toast.makeText(context, "转移单内无该固废!", Toast.LENGTH_SHORT).show();
            }
        }
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeDialog() {
        for (int i = 0; i < list.size(); i++) {
            if (Constants.WASTE_BACK.equals(list.get(i).getStatus())) {
                list.remove(i);
                break;
            }
        }
        groupAdapter.notifyDataSetChanged();
        affirmDialog = null;
    }
}
