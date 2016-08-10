package com.xyt.hwms.ui;

import android.app.Activity;
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
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xyt.hwms.R;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoveryBluetoothActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView listview;
    //Discovery is Finished
    private boolean _discoveryFinished;
    //手机的蓝牙适配器
    private BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    /**
     * Storage the found bluetooth devices
     * format:<MAC, <Key,Val>>;Key=[RSSI/NAME/COD(class od device)/BOND/UUID]
     */
    private Hashtable<String, Hashtable<String, String>> mhtFDS;
    //ListView的动态数组对象(存储用于显示的列表数组)
    private ArrayList<HashMap<String, Object>> malListItem;
    //SimpleAdapter对象(列表显示容器对象)
    private SimpleAdapter msaListItemAdapter;

    //Scan for Bluetooth devices. (broadcast listener)
    private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            /* bluetooth device profiles*/
            Hashtable<String, String> htDeviceInfo = new Hashtable<>();

			/* get the search results */
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            /* create found device profiles to htDeviceInfo*/
            if (null == device.getName()) {
                htDeviceInfo.put("NAME", "Null");
            } else {
                htDeviceInfo.put("NAME", device.getName());
            }
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                htDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_bonded));
            } else {
                htDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_nothing));
            }
            /*adding scan to the device profiles*/
            mhtFDS.put(device.getAddress(), htDeviceInfo);

			/*Refresh show list*/
            showDevices();
        }
    };
    //Bluetooth scanning is finished processing.(broadcast listener)
    private BroadcastReceiver _finshedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _discoveryFinished = true; //set scan is finished
            unregisterReceiver(_foundReceiver);
            unregisterReceiver(_finshedReceiver);

			/* 提示用户选择需要连接的蓝牙设备 */
            if (null != mhtFDS && mhtFDS.size() > 0) {    //找到蓝牙设备
                Toast.makeText(context, R.string.actDiscovery_msg_select_device, Toast.LENGTH_SHORT).show();
            } else {    //未找到蓝牙设备
                Toast.makeText(context, R.string.actDiscovery_msg_not_find_device, Toast.LENGTH_LONG).show();
            }
        }
    };

    //start run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discoverybluetooth);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView) this.findViewById(R.id.listview);

    	/* 选择项目后返回给调用页面 */
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String sMAC = (String) malListItem.get(arg2).get("MAC");
                Log.e("----------", sMAC);
                Intent result = new Intent();
                result.putExtra("MAC", sMAC);
                result.putExtra("NAME", mhtFDS.get(sMAC).get("NAME"));
                result.putExtra("BOND", mhtFDS.get(sMAC).get("BOND"));
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
        //立即启动扫描线程
        new scanDeviceTask().execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_btsearch, menu);
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
            case R.id.search:
                new scanDeviceTask().execute("");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时，强制终止搜索
        if (mBT.isDiscovering()) {
            mBT.cancelDiscovery();
        }
    }

    /**
     * 开始扫描周围的蓝牙设备
     * 备注:进入这步前必须保证蓝牙设备已经被启动
     *
     * @return void
     */
    private void startSearch() {
        _discoveryFinished = false; //标记搜索未结束

        //如果找到的设别对象为空，则创建这个对象。
        if (null == mhtFDS) {
            this.mhtFDS = new Hashtable<String, Hashtable<String, String>>();
        } else {
            this.mhtFDS.clear();
        }
        /* Register Receiver*/
        registerReceiver(_finshedReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(_foundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        mBT.startDiscovery();//start scan

        this.showDevices(); //the first scan clear show list
    }

    /* Show devices list */
    protected void showDevices() {
        if (null == this.malListItem) {//数组容器不存在时，创建
            this.malListItem = new ArrayList<HashMap<String, Object>>();
        }
        //如果列表适配器未创建则创建之
        if (null == this.msaListItemAdapter) {
            //生成适配器的Item和动态数组对应的元素
            this.msaListItemAdapter = new SimpleAdapter(this, malListItem,//数据源
                    R.layout.list_item_devices,//ListItem的XML实现
                    //动态数组与ImageItem对应的子项
                    new String[]{"NAME", "MAC", "BOND"},
                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                    new int[]{R.id.device_item_ble_name,
                            R.id.device_item_ble_mac,
                            R.id.device_item_ble_bond
                    }
            );
            //添加并且显示
            listview.setAdapter(this.msaListItemAdapter);
        }

        //构造适配器的数据
        this.malListItem.clear();//清除历史项
        Enumeration<String> e = this.mhtFDS.keys();
        /*重新构造数据*/
        while (e.hasMoreElements()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String sKey = e.nextElement();
            map.put("MAC", sKey);
            map.put("NAME", this.mhtFDS.get(sKey).get("NAME"));
            map.put("BOND", this.mhtFDS.get(sKey).get("BOND"));
            this.malListItem.add(map);
        }
        this.msaListItemAdapter.notifyDataSetChanged(); //通知适配器内容发生变化自动跟新
    }

    @Override
    public void getTagId(String data) {

    }

    @Override
    public void getBarcode(String data) {

    }

    @Override
    public void closeDialog() {

    }

    /*多线程处理:设备扫描监管线程*/
    private class scanDeviceTask extends AsyncTask<String, String, Integer> {
        //常量:蓝牙未开启
        private static final int RET_BLUETOOTH_NOT_START = 0x0001;
        //常量:设备搜索完成
        private static final int RET_SCAN_DEVICE_FINISHED = 0x0002;
        //等待蓝牙设备启动的最长时间(单位S)
        private static final int miWATI_TIME = 10;
        //每次线程休眠时间(单位ms)
        private static final int miSLEEP_TIME = 150;
        //进程等待提示框
        private ProgressDialog mpd = null;

        //线程启动初始化操作
        @Override
        public void onPreExecute() {
            /*定义进程对话框*/
            this.mpd = new ProgressDialog(context);
            this.mpd.setMessage(getString(R.string.actDiscovery_msg_scaning_device));
            this.mpd.setCancelable(true);//可被终止
            this.mpd.setCanceledOnTouchOutside(true);//点击外部可终止
            this.mpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {    //按下取消按钮后，终止搜索等待线程
                    _discoveryFinished = true;
                }
            });
            this.mpd.show();

            startSearch(); //执行蓝牙扫描
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (!mBT.isEnabled()) {//蓝牙未启动
                return RET_BLUETOOTH_NOT_START;
            }
            int iWait = miWATI_TIME * 1000;//倒减计数器
            //等待miSLEEP_TIME秒，启动蓝牙设备后再开始扫描
            while (iWait > 0) {
                if (_discoveryFinished) {
                    return RET_SCAN_DEVICE_FINISHED; //蓝牙搜索结束
                } else {
                    iWait -= miSLEEP_TIME; //剩余等待时间计时
                }
                SystemClock.sleep(miSLEEP_TIME);
            }
            return RET_SCAN_DEVICE_FINISHED; //在规定时间内，蓝牙设备未启动
        }

        //阻塞任务执行完后的清理工作
        @Override
        public void onPostExecute(Integer result) {
            if (this.mpd.isShowing()) {
                this.mpd.dismiss();//关闭等待对话框
            }
            if (mBT.isDiscovering()) {
                mBT.cancelDiscovery();
            }
            if (RET_BLUETOOTH_NOT_START == result) {    //提示蓝牙未启动
                Toast.makeText(context, R.string.actDiscovery_msg_bluetooth_not_start, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

