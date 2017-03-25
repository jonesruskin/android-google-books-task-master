package com.ersen.BookWorld.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;

/** This utility class is used to start other applications outside of Moozeo such as internet browser, email client and google maps etc. */
public class IntentUtils {

    public static void startInternetBrowser(@NonNull Context context,@NonNull String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(browserIntent, FaxiApplication.getInstance().getResources().getString(R.string.chooser_choose_internet_browser_app)));
    }

}
