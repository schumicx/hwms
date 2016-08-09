package com.xyt.hwms.support.bluetooth;

/**
 * PV操作锁的资源信号量
 *
 * @author JerryLi (lijian@dzs.mobi)
 * @version 1.0 2014-04-24
 */
final public class CResourcePV {
    private int iCount = 0;

    /**
     * 构造
     *
     * @param iResourceCount int 资源的数量
     */
    public CResourcePV(int iResourceCount) {
        this.iCount = iResourceCount;
    }

    /**
     * 抢占资源操作
     *
     * @return
     */
    public boolean seizeRes() {
        synchronized (this) {
            if (this.iCount > 0) {
                iCount--;
                return true;
            }
            return false;
        }
    }

    /**
     * 归还资源操作
     *
     * @return
     */
    public void revert() {
        synchronized (this) {
            iCount++;
        }
    }
}
