package com.xyt.hwms.support.bluetooth;

/**
 * 蓝牙通信的SPP客户端
 *
 * @author JerryLi (lijian@dzs.mobi)
 * @version 1.0 2013-03-17
 */
public final class BluetoothSppClient extends BTSerialComm {

    /**
     * 创建蓝牙SPP客户端类
     *
     * @param String MAC 蓝牙MAC地址
     * @return void
     */
    public BluetoothSppClient(String MAC) {
        super(MAC); //执行父类的构造函数
    }

    /**
     * 接收设备数据
     *
     * @return String null:未连接或连接中断 / String:数据
     */
    public String Receive() {
        byte[] btTmp = this.ReceiveData();

        if (null != btTmp) {
            return new String(btTmp);
        }
        return null;
    }
}
