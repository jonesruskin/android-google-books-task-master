package com.ersen.BookWorld.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;
import com.ersen.BookWorld.constants.FaxiConstants;
import com.ersen.BookWorld.models.Book;
import com.ersen.BookWorld.models.eventbus.ChangeToolBarTitleEvent;
import com.ersen.BookWorld.network.NetworkErrorHandler;
import com.ersen.BookWorld.utils.IntentUtils;
import com.ersen.BookWorld.utils.VisibilityManager;
import com.ersen.BookWorld.views.widgets.PlaceholderView;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BookDetail extends Fragment implements PlaceholderView.OnRetryButtonPressedListener, View.OnClickListener {

    private Book mBookFull;
    private VisibilityManager mVisibilityManager;

    public static BookDetail newInstance(String linkToBookDetails) {
        BookDetail bookDetail = new BookDetail();
        Bundle args = new Bundle();
        args.putString(FaxiConstants.BundleConstants.BOOK_SELF_LINK, linkToBookDetails);
        bookDetail.setArguments(args);
        return bookDetail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
        View viewOnGoogleBooks = view.findViewById(R.id.viewOnGoogleBooks);
        viewOnGoogleBooks.setOnClickListener(this);
        mVisibilityManager = new VisibilityManager(placeholderView,viewOnGoogleBooks, view.findViewById(R.id.mainContent));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new ChangeToolBarTitleEvent(FaxiApplication.getInstance().getResources().getString(R.string.fragment_bookDetail)));
        handleInitialContent();
    }

    private void handleInitialContent() {
        if (mBookFull == null) {
            mVisibilityManager.showLoading(FaxiApplication.getInstance().getResources().getString(R.string.loading_bookDetail));
            getFullDetailsOfBook();
        } else {
            showBookDetails();
        }
    }

    private void getFullDetailsOfBook() {
        String urlToSelfLink = getArguments().getString(FaxiConstants.BundleConstants.BOOK_SELF_LINK);
        Call<Book> call = FaxiApplication.getInstance().getNetworkAPI().getDetailsOfBook(urlToSelfLink);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Response<Book> response, Retrofit retrofit) {
                if (isAdded()) {
                    if (response.isSuccess()) {
                        mBookFull = response.body();
                        showBookDetails();
                    } else {
                        mVisibilityManager.showFailure(NetworkErrorHandler.getUnsuccessfulRequestMessage(response));
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                mVisibilityManager.showFailure(NetworkErrorHandler.getFailedRequestMessage(t));
            }
        });

    }

    private void showBookDetails() {
        final View view = getView();
        if(view != null){
            ImageView bookImage = (ImageView)view.findViewById(R.id.bookImage);
            TextView bookTitle = (TextView)view.findViewById(R.id.bookTitle);
            TextView bookAuthor = (TextView)view.findViewById(R.id.bookAuthor);
            RatingBar averageBookRating = (RatingBar)view.findViewById(R.id.averageBookRating);
            TextView ratingCount = (TextView)view.findViewById(R.id.ratingCount);
            TextView description = (TextView)view.findViewById(R.id.description);
            TextView categories = (TextView)view.findViewById(R.id.categories);
            TextView publisher = (TextView)view.findViewById(R.id.publisher);
            TextView miscellaneous = (TextView)view.findViewById(R.id.miscellaneous);
            TextView identifier = (TextView)view.findViewById(R.id.identifier);

            if(mBookFull.getBookInfo().getBookImages() != null){
                Glide.with(getActivity()).load(mBookFull.getBookInfo().getBookImages().getLargestPossibleImage()).fitCenter().into(bookImage);
            }

            bookTitle.setText(mBookFull.getBookInfo().getTitle());
            bookAuthor.setText(mBookFull.getBookInfo().getAuthors());
            averageBookRating.setRating((float) mBookFull.getBookInfo().getAverageRating());
            ratingCount.setText(String.valueOf(mBookFull.getBookInfo().getAmountOfRatings()));
            description.setText(Html.fromHtml(mBookFull.getBookInfo().getDescription()));
            categories.setText(mBookFull.getBookInfo().getCategory());
            publisher.setText(FaxiApplication.getInstance().getResources().getString(R.string.book_detail_publisherSummary, mBookFull.getBookInfo().getPublisher(), mBookFull.getBookInfo().getPublishedDate()));
            miscellaneous.setText(FaxiApplication.getInstance().getResources().getString(R.string.book_detail_miscellaneousSummary, mBookFull.getBookInfo().getLanguage(), mBookFull.getBookInfo().getPageCount()));
            identifier.setText(mBookFull.getBookInfo().getBookIdentifiers());
        }
        mVisibilityManager.showMainContent();
    }


    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewOnGoogleBooks:
                IntentUtils.startInternetBrowser(getActivity(),mBookFull.getBookInfo().getUrlToBookOnGoogleBooks());
                break;
        }
    }
}
