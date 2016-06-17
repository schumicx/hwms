package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;
import com.xyt.hwms.bean.TransferDetail;
import com.xyt.hwms.bean.User;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmItemsActivity extends BaseActivity {

    public CacheWasteDialogFragment cacheWasteDialogFragment;
    @BindView(R.id.listview)
    ListView listview;
    @NotEmpty(trim = true, message = "请输入操作人账号")
    EditText operator;
    private TextView total;
    private TextView verified;
    private TextView unverified;
    private TextView back;
    private List<TransferDetail> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;
    private int applyIndex;
    private int totalNum;
    private int verifiedNum;
    private int unverifiedNum;
    private int backNum;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        cacheWasteDialogFragment = CacheWasteDialogFragment.newInstance(applyIndex, position - 1);
        cacheWasteDialogFragment.show(getSupportFragmentManager(), getLocalClassName());
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
        operator = (EditText) head.findViewById(R.id.operator);

        totalNum = list.size();
        total.setText("" + totalNum);
//        if(TextUtils.isEmpty(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getOperator().trim())){
//            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setOperator("xxxx".trim());
//            PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
//            PreferencesUtils.putBoolean(context, "isSync", false);
//        }
        if (TextUtils.isEmpty(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getOperator()) || Constants.TRANSFER_TYPE_INNER.equals(getIntent().getStringExtra("type"))) {
            operator.setText(new Gson().fromJson(PreferencesUtils.getString(ApplicationController.getInstance(), "user"), User.class).getAccount());
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setOperator(operator.getText().toString().trim());
            PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
//            PreferencesUtils.putBoolean(context, "isSync", false);
        } else {
            operator.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getOperator());
        }

        if (Constants.TRANSFER_TYPE_INNER.equals(getIntent().getStringExtra("type"))) {
            operator.setFocusableInTouchMode(false);
            operator.setLongClickable(false);
        } else {
            operator.setFocusableInTouchMode(true);
            operator.setLongClickable(true);
        }

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
                Constants.AFFIRM_LIST.getCollection().get(applyIndex).setOperator(s.toString().trim());
                PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                PreferencesUtils.putBoolean(context, "isSync", false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onValidationSucceeded() {
        for (int i = 0; i < list.size(); i++) {
            if (barCodeData.equals(list.get(i).getLabel_code())) {
                cacheWasteDialogFragment = CacheWasteDialogFragment.newInstance(applyIndex, i);
                cacheWasteDialogFragment.show(getSupportFragmentManager(), getLocalClassName());
                break;
            }
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        super.onValidationFailed(errors);
        Toast.makeText(context, "操作人不能为空!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Constants.TRANSFER_TYPE_INNER.equals(getIntent().getStringExtra("type"))) {
            getMenuInflater().inflate(R.menu.menu_group, menu);
        }
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
        barCodeData = data;
        if (cacheWasteDialogFragment != null) {
            cacheWasteDialogFragment.dismiss();
        }
        validator.validate();
    }

    @Override
    public void closeDialog() {
        updateView();
        cacheWasteDialogFragment = null;
    }

    public void updateView() {
        affirmItemsAdapter.notifyDataSetChanged();
        backNum = 0;
        verifiedNum = 0;
        unverifiedNum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (Constants.WASTE_BACK.equals(list.get(i).getStatus())) {
                backNum++;
            } else if (Constants.WASTE_PASS.equals(list.get(i).getStatus())) {
                verifiedNum++;
            } else {
                unverifiedNum++;
            }
        }
        back.setText("" + backNum);
        verified.setText("" + verifiedNum);
        unverified.setText("" + unverifiedNum);

        if (totalNum == unverifiedNum) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setDetail_status("0");
        }
        if (totalNum > unverifiedNum) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setDetail_status("-1");
        }
        if (unverifiedNum == 0) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setDetail_status("1");
        }
        PreferencesUtils.putString(context, "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
    }
}
