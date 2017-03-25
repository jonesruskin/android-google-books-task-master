package com.ersen.BookWorld.utils;

import android.util.DisplayMetrics;

import com.ersen.BookWorld.application.FaxiApplication;

public class DisplayMetricsUtils {

    public static int convertDpToPixels(int dp){
        DisplayMetrics displayMetrics = FaxiApplication.getInstance().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / 160.0F));
    }

    public static int convertPixelsToDp(float px){
        DisplayMetrics displayMetrics = FaxiApplication.getInstance().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.densityDpi / 160.F));
    }
}
