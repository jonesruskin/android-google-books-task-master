package com.ersen.BookWorld.activities;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.ersen.BookWorld.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) { //Does the activity have a toolbar?
            setSupportActionBar(toolbar); //Set the toolbar
        }
    }

    public void setToolbarTitle(String toolbarTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!TextUtils.isEmpty(toolbarTitle)) {
                actionBar.setTitle(toolbarTitle);
            }
        }
    }

    public void displayHomeAsUpEnabled(boolean show){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(show);
        }
    }

    public void replaceFragment(@NonNull Fragment fragment, int containerId, boolean addToBackStack, String tag, boolean animated) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (animated) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_out_from_right, R.anim.slide_out_from_left, R.anim.slide_in_from_left, R.anim.slide_in_from_right);
        }
        fragmentTransaction.replace(containerId, fragment,tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    /** Use this if you want to go back to a specific fragment identified by its tag
     * @param tag The tag of the fragment which should be given by fragmentClass.class.getName()
     * @return true if something was popped otherwise false is returned meaning nothing was popped
     * */
    public boolean popBackStackToTransaction(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.popBackStackImmediate(tag,0);
    }

    /**
     * This will pop the fragment transaction back stack to the first entry.
     */
    public boolean popBackStackToStart() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public Fragment findFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }
}
