package com.dqqdo.dochart.util;

import android.util.Log;


/**
 * 作者：duqingquan
 * 时间：2017/3/15 15:17
 */
public class LogUtil {

    private final static String TAG = "dqqdo";

    public static void e(String message) {
        Log.e(TAG, message);

    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

}
