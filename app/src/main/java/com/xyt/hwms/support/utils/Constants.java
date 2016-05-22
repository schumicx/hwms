package com.xyt.hwms.support.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constants {
    //测试
    public static final String BASE_URL = "http://ead.xiyouqi.cn:8080";
//    public static final String BASE_URL = "http://192.168.43.126:8080";
    //生产
//    public static final String BASE_URL = "http://120.25.153.217";

    public static final String SERVER = BASE_URL + "/hwms/api/";

    public static final int PAGESIZE = 10;

    public static final int SUCCESS = 0;

    public static final String TOKEN = "token";

    public static List<Map> AFFIRM_LIST = new ArrayList<>();

    public static final String PAGE = "_page";
    public static final String SIZE = "_size";
    public static final int STARTPAGE = 1;
}
