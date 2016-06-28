package com.xyt.hwms.support.utils;

import com.xyt.hwms.BuildConfig;
import com.xyt.hwms.bean.TransferList;

public class Constants {
    public static final String KEY = "01554cf384968a8a8b1f554cf38403ea";
    public static final String SERVER = BuildConfig.API_URL + "/hwms/api/";
    public static final String WARNING = "warning";
    public static final int STATE_IDLE = 0;
    public static final int STATE_DECODE = 1;
    public static final boolean BEEP_MODE = true;
    public static final String WASTE_PASS = "pass";
    public static final String WASTE_BACK = "back";
    public static final String LABEL_LIB = "LIB";
    public static final String LABEL_LSL = "LSL";
    public static final String LABEL_CON = "CON";
    public static final String LABEL_HW = "HW";
    public static final String TRANSFER_TYPE_INNER = "inner";
    public static final String TRANSFER_TYPE_OUTER = "outer";
    public static final String MAIN_ITEM1 = "装车确认";
    public static final String MAIN_ITEM2 = "内部转移";
    public static final String MAIN_ITEM3 = "固废入库";
    public static final String MAIN_ITEM4 = "内部利用";
    public static final String MAIN_ITEM5 = "固废出库";
    public static TransferList AFFIRM_LIST;
    public static long decode_start = 0;
    public static long decode_end = 0;
}
