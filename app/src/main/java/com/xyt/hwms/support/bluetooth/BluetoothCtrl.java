package com.xyt.hwms.support.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

/**
 * 蓝牙的私有API接口调用工具类
 */
public class BluetoothCtrl {
    /**
     * 常量:蓝牙配对绑定过滤监听器名称
     */
    static public final String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

    /**
     * 对蓝牙设备进行配对
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean createBond(BluetoothDevice btDevice) throws Exception {
        Class<? extends BluetoothDevice> btClass = btDevice.getClass();
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 解除蓝牙设备的配对
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean removeBond(BluetoothDevice btDevice) throws Exception {
        Class<? extends BluetoothDevice> btClass = btDevice.getClass();
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }
}