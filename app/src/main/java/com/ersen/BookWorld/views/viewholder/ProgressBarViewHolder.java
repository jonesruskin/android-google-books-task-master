package com.ersen.BookWorld.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ersen.BookWorld.R;

public class ProgressBarViewHolder extends RecyclerView.ViewHolder {

    private ProgressBar mProgressBar;
    public ProgressBarViewHolder(View itemView) {
        super(itemView);
        mProgressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
}
