package com.ersen.BookWorld.utils;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.ersen.BookWorld.views.widgets.PlaceholderView;


public class VisibilityManager {
    private View[] mMainContent; //Stores all the main content views whose visibility are changed together i.e. all visible or all gone
    private PlaceholderView mPlaceholderView;

    public VisibilityManager(@NonNull PlaceholderView placeholderView, View... mainContent) {
        this.mPlaceholderView = placeholderView;
        this.mMainContent = mainContent;
    }

    public void showMainContent() {
        setMainContentVisibility(View.VISIBLE);
    }

    public void showLoading(@StringRes String message){
        setMainContentVisibility(View.GONE);
        mPlaceholderView.showLoading(message);
    }

    public void showFailure(@StringRes String message){
        setMainContentVisibility(View.GONE);
        mPlaceholderView.showFailure(message);
    }

    private void setMainContentVisibility(int visibility) {
        if (mMainContent != null) {
            for (View view : mMainContent) {
                if (view != null) {
                    view.setVisibility(visibility);
                }
            }
        }

        if (visibility == View.VISIBLE) { //If the main content is going to be visible, then hide the placeholder and remove its views
            mPlaceholderView.setVisibility(View.GONE);
            mPlaceholderView.removeAllViews();
        } else {
            mPlaceholderView.setVisibility(View.VISIBLE); //Else if the content is not going to be visible, show the placeholder
        }
    }
}
