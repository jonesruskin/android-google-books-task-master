package com.ersen.BookWorld.utils;

import android.util.Log;

import com.ersen.BookWorld.BuildConfig;

public class DebugUtils {
    private static final String LOG_TAG_MESSAGE = "ERSEN"; //The tag of all log messages, set your log cat filter to display this tag.

    /**
     * Write message to logcat
     */
    public static void Log(String message) {
        if (BuildConfig.DEBUG) { //Only display the log messages if we are on debug
            Log.e(LOG_TAG_MESSAGE, message);
        }
    }
}
