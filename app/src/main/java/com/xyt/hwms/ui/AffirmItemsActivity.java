package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.AffirmItemsAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AffirmItemsActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    private List<Map> list = new ArrayList<>();
    private AffirmItemsAdapter affirmItemsAdapter;
    private List object;
    private int applyIndex;

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
//        Intent intent = new Intent(getBaseContext(), AffirmDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("detail", (Serializable) list.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);
        AffirmDetailsDialogFragment.newInstance(object, applyIndex, position).show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_items);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        object = (List) getIntent().getSerializableExtra("object");
        applyIndex = getIntent().getIntExtra("position", 0);
        list = (List)((Map)object.get(applyIndex)).get("detail");

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", (Serializable)object);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);

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
//                finish();
                Toast.makeText(context,new Gson().toJson(((Map) ((List) ((Map) object.get(applyIndex)).get("detail")).get(0))),Toast.LENGTH_LONG).show();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTagId(String data) {
        Toast.makeText(getBaseContext(), "xxxxxxxxxxx-----" + data, Toast.LENGTH_SHORT).show();
    }
}
