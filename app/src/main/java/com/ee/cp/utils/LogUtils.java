package com.ee.cp.utils;

import android.util.Log;

public class LogUtils {
    private static boolean enableLog = false;

    public static void Le(String tag, String msg) {
        if (enableLog)
            Log.e(tag, msg);
    }

    public static void Ld(String tag, String msg) {
        if (enableLog)
            Log.d(tag, msg);
    }

    public static void Li(String tag, String msg) {
        if (enableLog)
            Log.i(tag, msg);
    }

    public static void Lv(String tag, String msg) {
        if (enableLog)
            Log.v(tag, msg);
    }

    public static void Lw(String tag, String msg) {
        if (enableLog)
            Log.w(tag, msg);
    }
}
