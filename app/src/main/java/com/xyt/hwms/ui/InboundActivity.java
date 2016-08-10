package com.xyt.hwms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.adapter.InboundAdapter;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.InboundQuery;
import com.xyt.hwms.bean.InboundQueryBean;
import com.xyt.hwms.support.bluetooth.BluetoothCtrl;
import com.xyt.hwms.support.bluetooth.BluetoothSppClient;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InboundActivity extends BaseActivity {
    //常量:搜索页面返回
    public static final byte REQUEST_DISCOVERY = 0x01;
    //未设限制的AsyncTask线程池(重要)
    protected static ExecutorService FULL_TASK_EXECUTOR;

    static {
        FULL_TASK_EXECUTOR = Executors.newCachedThreadPool();
    }

    //线程终止标志(用于终止监听线程)
    protected boolean mbThreadStop = false;
    //对象:引用全局的蓝牙连接对象
    protected BluetoothSppClient mBSC = null;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.submit)
    Button submit;

    private EditText store;
    private EditText position;
    private EditText container;
    private EditText weight;
    private List<InboundQuery> list = new ArrayList<>();
    private InboundAdapter inboundAdapter;
    private String labelCode;
    private String serialData;
    //手机的蓝牙适配器
    private BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    //蓝牙设备连接句柄
    private BluetoothDevice mBDevice = null;
    //选定设备的配置信息
    private Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();
    //蓝牙配对进程操作标志
    private boolean mbBonded = false;
    //获取到的UUID Service 列表信息
    private ArrayList<String> mslUuidList = new ArrayList<String>();
    //保存蓝牙进入前的开启状态
    private boolean mbBleStatusBefore = false;
    //广播监听:获取UUID服务
    private BroadcastReceiver _mGetUuidServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            int iLoop = 0;
            if (BluetoothDevice.ACTION_UUID.equals(action)) {
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID");
                if (null != uuidExtra) {
                    iLoop = uuidExtra.length;
                }
                /*uuidExtra should contain my service's UUID among his files, but it doesn't!!*/
                for (int i = 0; i < iLoop; i++) {
                    mslUuidList.add(uuidExtra[i].toString());
                }
            }
        }
    };
    //广播监听:蓝牙配对处理
    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {    //配对状态改变时，的广播处理
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mbBonded = true;//蓝牙配对设置成功
                } else {
                    mbBonded = false;//蓝牙配对进行中或者配对失败
                }
            }
        }
    };

    @OnClick(R.id.submit)
    public void onClick(View v) {
        submitRequest();
    }

    @OnClick(R.id.getdata)
    public void onClickGetData(View v) {
        try {
            serialStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mBSC = ApplicationController.getInstance().mBSC;

        View head = getLayoutInflater().inflate(R.layout.list_head_inbound, null);
        store = (EditText) head.findViewById(R.id.store);
        position = (EditText) head.findViewById(R.id.position);
        container = (EditText) head.findViewById(R.id.container);
        weight = (EditText) head.findViewById(R.id.weight);
        listview.addHeaderView(head, null, true);

        weight.setFocusable(true);
        weight.setFocusableInTouchMode(true);

        if (inboundAdapter == null) {
            inboundAdapter = new InboundAdapter(context, list, weight);
        }
        listview.setAdapter(inboundAdapter);

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(".")) {
                    weight.setText("0.");
                    CharSequence text = weight.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        try {
                            Selection.setSelection(spanText, weight.length());
                        } catch (IndexOutOfBoundsException e) {
                            Selection.setSelection(spanText, text.length());
                        }
                    }
                    return;
                }
                if (weight.isFocused()) {
                    if (!TextUtils.isEmpty(s.toString())) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setWeight(Float.valueOf(s.toString()) / list.size());
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setWeight(null);
                        }
                    }
                    inboundAdapter.notifyDataSetChanged();
                }
            }
        });

        if (null == mBT) { //系统中不存在蓝牙模块
            Toast.makeText(this, "Bluetooth module not found", Toast.LENGTH_LONG).show();
            this.finish();
        }

        new startBluetoothDeviceTask().execute(""); //启动蓝牙设备
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ApplicationController.getInstance().closeConn();//关闭连接

        //检查如果进入前蓝牙是关闭的状态，则退出时关闭蓝牙
        if (null != mBT && !this.mbBleStatusBefore) {
            mBT.disable();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_btoperate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mbThreadStop = true; //终止接收线程
                finish();
                return true;
            case R.id.research: //开始扫描
                ApplicationController.getInstance().closeConn();//关闭连接
                this.openDiscovery(); //进入搜索页面
                return true;
            case R.id.connect: //连接设备
                connectDevice();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 建立设备的串行通信连接
    public void connectDevice() {
        if (mBDevice != null) {
            new connSocketTask().execute(mBDevice.getAddress());
        } else {
            Toast.makeText(context, "连接失败，请重新搜索设备!", Toast.LENGTH_SHORT).show();
        }
    }

    // 建立设备的串行通信连接
    public void connectDevice(String Mac) {
        new connSocketTask().execute(Mac);
    }

    //通信模式选择-串行流模式
    public void serialStream() {
        this.mBSC = ApplicationController.getInstance().mBSC;
        new receiveTask().executeOnExecutor(FULL_TASK_EXECUTOR);
    }

    @Override
    public void getTagId(String data) {
    }

    @Override
    public void getBarcode(String data) {
        barCodeData = data;
        if (data.startsWith(Constants.LABEL_LIB)) {
            if (!TextUtils.isEmpty(position.getText().toString()) && !barCodeData.equals(store.getText().toString())) {
                position.getText().clear();
            }
            store.getText().clear();
            store.setText(data);
        } else if (data.startsWith(Constants.LABEL_LSL)) {
            position.getText().clear();
            position.setText(data);
            store.getText().clear();
            store.setText(data.replace(Constants.LABEL_LSL, Constants.LABEL_LIB).substring(0, data.length() - 4));
        } else if (data.startsWith(Constants.LABEL_CON)) {
            if (!TextUtils.isEmpty(container.getText().toString()) && !barCodeData.equals(container.getText().toString())) {
                labelCode = null;
                store.getText().clear();
                position.getText().clear();
            }
            container.getText().clear();
            container.setText(data);
            getRequest();
        } else if (data.startsWith(Constants.LABEL_HW)) {
            labelCode = data;
            getRequest();
        }
        inboundAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeDialog() {
    }

    public void backWaste(InboundQuery inboundQuery, int index) {
        backRequest(inboundQuery, index);
    }

    public void showReasonDialog(InboundQuery inboundQuery) {
        ReasonDialogFragment.newInstance(inboundQuery).show(getSupportFragmentManager(), getLocalClassName());
    }

    private void openDiscovery() {
        //进入蓝牙设备搜索界面
        Intent intent = new Intent(this, DiscoveryBluetoothActivity.class);
        this.startActivityForResult(intent, REQUEST_DISCOVERY); //等待返回搜索结果
    }

    //显示Service UUID信息
    private void showServiceUUIDs() {
        new GetUUIDServiceTask().execute("");
    }

    //蓝牙设备选择完后返回处理
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DISCOVERY) {
            if (Activity.RESULT_OK == resultCode) {
                this.mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                this.mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                this.mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));

                //如果设备未配对，显示配对操作
                if (this.mhtDeviceInfo.get("BOND").equals(getString(R.string.actDiscovery_bond_nothing))) {
                    new PairTask().execute(this.mhtDeviceInfo.get("MAC"));
                } else {
                    //已存在配对关系，建立与远程设备的连接
                    this.mBDevice = this.mBT.getRemoteDevice(this.mhtDeviceInfo.get("MAC"));
                    this.showServiceUUIDs();//显示设备的Service UUID列表
                    connectDevice();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            this.mbThreadStop = true; //终止接收线程
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //入库查询
    private void getRequest() {
        String url = Constants.SERVER + "mobile-get-in";
        Map<String, Object> params = new HashMap<>();
        params.put("store_label_code", store.getText().toString());//库
        params.put("position_label_code", position.getText().toString());//库位
        params.put("container_label_code", container.getText().toString());//容器
        params.put("label_code", labelCode);//固废
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, InboundQueryBean.class, new Gson().toJson(params), new Response.Listener<InboundQueryBean>() {
                    @Override
                    public void onResponse(InboundQueryBean response) {
                        list.clear();
                        if (response.getData().size() > 0) {
                            store.setText(response.getData().get(0).getStore_label_code());
                            position.setText(response.getData().get(0).getPosition_label_code());
                            container.setText(response.getData().get(0).getContainer_label_code());
                            if (!TextUtils.isEmpty(response.getData().get(0).getLabel_code())) {
                                list.addAll(response.getData());
                            }
                        }
                        inboundAdapter.calculate();
                        inboundAdapter.notifyDataSetChanged();
                        if (list.size() <= 0) {
                            weight.getText().clear();
                        }
                        if (Constants.WARNING.equals(response.getType())) {
                            Toast.makeText(context, response.getContent(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }), getLocalClassName());
    }

    //入库提交
    private void submitRequest() {
        String url = Constants.SERVER + "mobile-get-in/weight";
        Map<String, Object> params = new HashMap<>();
        params.put("list", list);
        params.put("store_label_code", store.getText().toString());//库
        params.put("position_label_code", position.getText().toString());//库位
        params.put("container_label_code", container.getText().toString());//容器
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, BaseBean.class, new Gson().toJson(params), new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean response) {
                        Toast.makeText(context, "操作成功!", Toast.LENGTH_SHORT).show();
                        labelCode = null;
                        list.clear();
                        store.getText().clear();
                        position.getText().clear();
                        container.getText().clear();
                        weight.getText().clear();
                        inboundAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }), getLocalClassName());
    }

    //固废退回
    private void backRequest(final InboundQuery inboundQuery, int index) {
        String url = Constants.SERVER + "mobile-get-in/back";
        Map<String, Object> params = new HashMap<>();
        params.put("transfer_detail_id", inboundQuery.getTransfer_detail_id());
        params.put("status", Constants.WASTE_BACK);
        params.put("back_reason", getResources().getStringArray(R.array.reason)[index]);
        params.put("back_reason_index", index);
        params.put("container_label_code", null);
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, InboundQueryBean.class, new Gson().toJson(params), new Response.Listener<InboundQueryBean>() {
                    @Override
                    public void onResponse(InboundQueryBean response) {
                        list.remove(inboundQuery);
                        inboundAdapter.calculate();
                        inboundAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }), getLocalClassName());
    }

    /*多线程处理(开机时启动蓝牙)*/
    private class startBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        //常量:蓝牙已经启动
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        //常量:设备启动失败
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        //等待蓝牙设备启动的最长时间(单位S)
        private static final int miWATI_TIME = 15;
        //每次线程休眠时间(单位ms)
        private static final int miSLEEP_TIME = 150;
        //进程等待提示框
        private ProgressDialog mpd;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            /*定义进程对话框*/
            mpd = new ProgressDialog(context);
            mpd.setMessage(getString(R.string.actDiscovery_msg_starting_device));//蓝牙启动中
            mpd.setCancelable(false);//不可被终止
            mpd.setCanceledOnTouchOutside(false);//点击外部不可终止
            mpd.show();
            mbBleStatusBefore = mBT.isEnabled(); //保存进入前的蓝牙状态
        }

        //异步的方式启动蓝牙，如果蓝牙已经启动则直接进入扫描模式
        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME * 1000;//倒减计数器
            /* BT isEnable */
            if (!mBT.isEnabled()) {
                mBT.enable(); //启动蓝牙设备
                //等待miSLEEP_TIME秒，启动蓝牙设备后再开始扫描
                while (iWait > 0) {
                    if (!mBT.isEnabled()) {
                        iWait -= miSLEEP_TIME; //剩余等待时间计时
                    } else {
                        break; //启动成功跳出循环
                    }
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0) {//表示在规定时间内,蓝牙设备未启动
                    return RET_BLUETOOTH_START_FAIL;
                }
            }
            return RET_BULETOOTH_IS_START;
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            if (mpd.isShowing()) {
                mpd.dismiss();//关闭等待对话框
            }
            if (RET_BLUETOOTH_START_FAIL == result) {    //蓝牙设备启动失败
                AlertDialog.Builder builder = new AlertDialog.Builder(context); //对话框控件
                builder.setTitle(getString(R.string.dialog_title_sys_err));//设置标题
                builder.setMessage(getString(R.string.actDiscovery_msg_start_bluetooth_fail));
                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBT.disable();
                        //蓝牙设备无法启动，直接终止程序
                        finish();
                    }
                });
                builder.create().show();
            } else if (RET_BULETOOTH_IS_START == result) {    //蓝牙启动成功
                if (TextUtils.isEmpty(PreferencesUtils.getString(context, "DEVICE_INFO"))) {
                    openDiscovery(); //进入搜索页面
                } else {
                    mhtDeviceInfo = new Gson().fromJson(PreferencesUtils.getString(context, "DEVICE_INFO"), Hashtable.class);
                    mBDevice = mBT.getRemoteDevice(mhtDeviceInfo.get("MAC"));
                    connectDevice(mhtDeviceInfo.get("MAC"));
                }
            }
        }
    }

    /*多线程处理(配对处理线程)*/
    private class PairTask extends AsyncTask<String, String, Integer> {
        //常量:配对成功
        static private final int RET_BOND_OK = 0x00;
        //常量: 配对失败
        static private final int RET_BOND_FAIL = 0x01;
        //常量: 配对等待时间(10秒)
        static private final int iTIMEOUT = 1000 * 10;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            //提示开始建立配对
            Toast.makeText(context, R.string.actMain_msg_bluetooth_Bonding, Toast.LENGTH_SHORT).show();
            /*蓝牙自动配对*/
            //监控蓝牙配对请求
            registerReceiver(_mPairingRequest, new IntentFilter(BluetoothCtrl.PAIRING_REQUEST));
            //监控蓝牙配对是否成功
            registerReceiver(_mPairingRequest, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            final int iStepTime = 150;
            int iWait = iTIMEOUT; //设定超时等待时间
            try {    //开始配对
                //获得远端蓝牙设备
                mBDevice = mBT.getRemoteDevice(arg0[0]);
                BluetoothCtrl.createBond(mBDevice);
                mbBonded = false; //初始化配对完成标志
            } catch (Exception e1) {    //配对启动失败
                e1.printStackTrace();
                return RET_BOND_FAIL;
            }
            while (!mbBonded && iWait > 0) {
                SystemClock.sleep(iStepTime);
                iWait -= iStepTime;
            }
            return iWait > 0 ? RET_BOND_OK : RET_BOND_FAIL;
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            unregisterReceiver(_mPairingRequest); //注销监听

            if (RET_BOND_OK == result) {//配对建立成功
                Toast.makeText(context, R.string.actMain_msg_bluetooth_Bond_Success, Toast.LENGTH_SHORT).show();
                mhtDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_bonded));//显示已绑定
                showServiceUUIDs();//显示远程设备提供的服务
                connectDevice();
            } else {    //在指定时间内未完成配对
                Toast.makeText(context, R.string.actMain_msg_bluetooth_Bond_fail, Toast.LENGTH_LONG).show();
                try {
                    BluetoothCtrl.removeBond(mBDevice);
                } catch (Exception e) {
                    Log.d(getString(R.string.app_name), "removeBond failed!");
                    e.printStackTrace();
                }
            }
        }
    }

    /*多线程处理(读取UUID Service信息线程)*/
    private class GetUUIDServiceTask extends AsyncTask<String, String, Integer> {
       //延时等待时间
        private static final int miWATI_TIME = 4 * 1000;
        //每次检测的时间
        private static final int miREF_TIME = 200;
        //uuis find service is run
        private boolean mbFindServiceIsRun = false;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            mslUuidList.clear();
            // Don't forget to unregister during onDestroy
            registerReceiver(_mGetUuidServiceReceiver, new IntentFilter(BluetoothDevice.ACTION_UUID));// Register the BroadcastReceiver
            this.mbFindServiceIsRun = mBDevice.fetchUuidsWithSdp();
        }

        //线程异步处理
        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME;//倒减计数器

            if (!this.mbFindServiceIsRun) {
                return null; //UUID Service扫瞄服务器启动失败
            }
            while (iWait > 0) {
                if (mslUuidList.size() > 0 && iWait > 1500) {
                    iWait = 1500; //如果找到了第一个UUID则继续搜索N秒后结束
                }
                SystemClock.sleep(miREF_TIME);
                iWait -= miREF_TIME;//每次循环减去刷新时间
            }
            return null;
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            StringBuilder sbTmp = new StringBuilder();
            unregisterReceiver(_mGetUuidServiceReceiver); //注销广播监听
            //如果存在数据，则自动刷新
            if (mslUuidList.size() > 0) {
                for (int i = 0; i < mslUuidList.size(); i++) {
                    sbTmp.append(mslUuidList.get(i) + "\n");
                }
            }
        }
    }

    /*多线程处理(建立蓝牙设备的串行通信连接)*/
    private class connSocketTask extends AsyncTask<String, String, Integer> {
        //常量:连接建立失败
        private static final int CONN_FAIL = 0x01;
        //常量:连接建立成功
        private static final int CONN_SUCCESS = 0x02;
        //进程等待提示框
        private ProgressDialog mpd = null;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            /*定义进程对话框*/
            this.mpd = new ProgressDialog(context);
            this.mpd.setMessage(getString(R.string.actMain_msg_device_connecting));
            this.mpd.setCancelable(false);//可被终止
            this.mpd.setCanceledOnTouchOutside(false);//点击外部可终止
            this.mpd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            if (ApplicationController.getInstance().createConn(arg0[0])) {
                return CONN_SUCCESS; //建立成功
            }
            return CONN_FAIL; //建立失败
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            this.mpd.dismiss();

            if (CONN_SUCCESS == result) {    //通信连接建立成功
                Toast.makeText(context, R.string.actMain_msg_device_connect_succes, Toast.LENGTH_SHORT).show();
                PreferencesUtils.putString(context, "DEVICE_INFO", new Gson().toJson(mhtDeviceInfo));
            } else {    //通信连接建立失败
                Toast.makeText(context, R.string.actMain_msg_device_connect_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*多线程处理(建立蓝牙设备的串行通信连接)*/
    private class receiveTask extends AsyncTask<String, String, Integer> {
        //Constant: the connection is lost
        private final static byte CONNECT_LOST = 0x01;
        //Constant: the end of the thread task
        private final static byte THREAD_END = 0x02;
        //Constant: the end of the thread task
        private final static byte CONNECT_NONE = 0x03;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            mbThreadStop = false;
        }

        //线程异步处理
        @Override
        protected Integer doInBackground(String... arg0) {
            if (mBSC == null) {
                mbThreadStop = false;
                return (int) CONNECT_NONE;
            }
            mBSC.Receive(); //首次启动调用一次以启动接收线程
            while (!mbThreadStop) {
                if (!mBSC.isConnect()) {//检查连接是否丢失
                    return (int) CONNECT_LOST;
                }
                if (mBSC.getReceiveBufLen() > 0) {
                    SystemClock.sleep(20); //先延迟让缓冲区填满
                    this.publishProgress(mBSC.Receive());
                }
            }
            return (int) THREAD_END;
        }

        //线程内更新处理
        @Override
        public void onProgressUpdate(String... progress) {
            if (null != progress[0]) {
                serialData += progress[0];
                if (serialData.length() > 50) {
                    serialData = serialData.substring(0, serialData.lastIndexOf("\n"));
                    serialData = serialData.substring(serialData.lastIndexOf("\n"), serialData.length());
                    weight.setText(serialData.substring(3, serialData.length() - 3));

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setWeight(Float.valueOf(weight.getText().toString()) / list.size());
                    }
                    inboundAdapter.notifyDataSetChanged();
                    mbThreadStop = true;
                }
            }
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            if (CONNECT_NONE == result) {
                Toast.makeText(context, "未连接设备!", Toast.LENGTH_SHORT).show();
            } else if (CONNECT_LOST == result) {//connection is lost
                Toast.makeText(getBaseContext(), R.string.msg_msg_bt_connect_lost, Toast.LENGTH_SHORT).show();
                ApplicationController.getInstance().closeConn();//释放连接对象
            } else {
                Toast.makeText(getBaseContext(), R.string.msg_receive_data_stop, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
