package com.ersen.BookWorld.models.eventbus;

public class LaunchBookSearchFragmentEvent {
    private String mSearchQuery;

    public LaunchBookSearchFragmentEvent(String mSearchQuery) {
        this.mSearchQuery = mSearchQuery;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }
}
