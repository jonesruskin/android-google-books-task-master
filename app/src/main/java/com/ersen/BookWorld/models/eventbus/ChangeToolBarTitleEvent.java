package com.ersen.BookWorld.models.eventbus;

public class ChangeToolBarTitleEvent {
    private String mTitle;

    public ChangeToolBarTitleEvent(String title) {
        this.mTitle = title;

    }

    public String getTitle() {
        return mTitle;
    }

}
