package com.msgl.bound.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by gldgf on 2017/11/9.
 *
 * 一些转换tools
 */

public class Tools {

    private static DisplayMetrics sDisplayMetrics;

    // 添加默认显示
    public static void addDisplayMetrics(Activity activity) {
        sDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(sDisplayMetrics);
    }

    /* 如sp, dp, mm 等各种单位转px.使用之方法必须先初始化DisplayMetrics,
    调用addDisplayMetrics(Activity activity)进行初始化.
     */
    public static float unitSwap(int unit, float value) {
        if (sDisplayMetrics == null) {
            throw new NullPointerException("DisplayMetrics没有初始化, 请调用addDisplayMetrics(Activity activity)进行初始化.");
        }
        return TypedValue.applyDimension(unit, value, sDisplayMetrics);
    }
}
