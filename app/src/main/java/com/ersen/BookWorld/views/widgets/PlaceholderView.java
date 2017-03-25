package com.ersen.BookWorld.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ersen.BookWorld.R;

public class PlaceholderView extends LinearLayout implements View.OnClickListener {

    private View mLoadingView, mInitialContentFailedToLoadView;
    private TextView mLoadingMessage, mInitialContentFailedToLoadMessage;
    private Button mRetry;
    private OnRetryButtonPressedListener mOnRetryButtonPressedListener;

    public interface OnRetryButtonPressedListener {
        void onRetryPressed();
    }

    public PlaceholderView(Context context) {
        super(context);
        init(context);
    }

    public PlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnRetryButtonPressedListener(OnRetryButtonPressedListener onRetryButtonPressedListener) {
        this.mOnRetryButtonPressedListener = onRetryButtonPressedListener;
    }

    private void init(Context context){
        /** The loading view must always be match parent to take up the whole screen so lets force it just in case we did not set match parent in XML */
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);

        mLoadingView = inflate(context, R.layout.view_initial_content_loading_placeholder, null);
        mInitialContentFailedToLoadView = inflate(context, R.layout.view_initial_loading_failed_placeholder, null);
        mLoadingMessage = (TextView)mLoadingView.findViewById(R.id.loadingMessage);
        mInitialContentFailedToLoadMessage = (TextView)mInitialContentFailedToLoadView.findViewById(R.id.failedMessage);
        mInitialContentFailedToLoadView.findViewById(R.id.retry).setOnClickListener(this);
    }

    public void showFailure(String message) {
        removeAllViews();
        addView(mInitialContentFailedToLoadView);
        mInitialContentFailedToLoadMessage.setText(message);
    }

    public void showLoading(String message) {
        removeAllViews();
        addView(mLoadingView);
        mLoadingMessage.setText(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry:
                if (mOnRetryButtonPressedListener != null) {
                    mOnRetryButtonPressedListener.onRetryPressed();
                }
                break;
        }
    }
}
