package com.xyt.hwms.support.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BaseUtils {

    /**
     * 获取当前程序版本名
     *
     * @param context
     * @return
     */
    public static String getPackageVersion(Context context) {
        String versionName = "";
        try {
            PackageInfo pi = context
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(
                            context.getApplicationContext().getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            versionName = "";
        }
        return versionName;
    }

    /**
     * 获取当前程序包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            PackageInfo pi = context
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(
                            context.getApplicationContext().getPackageName(), 0);
            packageName = pi.packageName;
        } catch (Exception e) {
            packageName = "";
        }
        return packageName;
    }

    /**
     * 获取当前程序版本code
     *
     * @param context
     * @return
     */
    public static int getPackageCode(Context context) {
        int versionCode;
        try {
            PackageInfo pi = context
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(
                            context.getApplicationContext().getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            versionCode = 1;
        }
        return versionCode;
    }

    /**
     * 判断是否是平板
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(dpValue * scale);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(pxValue / scale);
    }

    /**
     * 获取手机屏幕宽度,单位是像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取手机屏幕高度,单位是像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取网络信息
     *
     * @param context
     * @return
     */
    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

}
