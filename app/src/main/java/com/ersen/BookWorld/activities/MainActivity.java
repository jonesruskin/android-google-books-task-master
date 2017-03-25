package com.ersen.BookWorld.activities;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.ersen.BookWorld.R;
import com.ersen.BookWorld.fragments.BookDetail;
import com.ersen.BookWorld.fragments.BookSearch;
import com.ersen.BookWorld.fragments.BookSearchResults;
import com.ersen.BookWorld.models.eventbus.ChangeToolBarTitleEvent;
import com.ersen.BookWorld.models.eventbus.LaunchBookDetailFragmentEvent;
import com.ersen.BookWorld.models.eventbus.LaunchBookSearchFragmentEvent;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        EventBus.getDefault().register(this);
        replaceFragment(new BookSearch(), R.id.fragmentContainer, false, BookSearch.class.getName(), true);
    }

    private void showBookSearchResultsFragment(String searchQuery){
        BookSearchResults bookSearchResults = BookSearchResults.newInstance(searchQuery);
        replaceFragment(bookSearchResults, R.id.fragmentContainer, true, BookSearchResults.class.getName(), true);
    }

    private void showBookDetailFragment(String linkToBookDetails){
        BookDetail bookDetail = BookDetail.newInstance(linkToBookDetails);
        replaceFragment(bookDetail, R.id.fragmentContainer, true, BookDetail.class.getName(), true);
    }

    /** Event Bus subscribers*/

    public void onEvent(LaunchBookSearchFragmentEvent launchBookSearchFragmentEvent){
        showBookSearchResultsFragment(launchBookSearchFragmentEvent.getSearchQuery());
    }

    public void onEvent(LaunchBookDetailFragmentEvent launchBookDetailFragmentEvent){
        showBookDetailFragment(launchBookDetailFragmentEvent.getLinkToBookDetails());
    }

    public void onEvent(ChangeToolBarTitleEvent changeToolBarTitleEvent){
        setToolbarTitle(changeToolBarTitleEvent.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackStackChanged() {
        displayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() != 0);
    }
}
