package com.ersen.BookWorld.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;
import com.ersen.BookWorld.constants.FaxiConstants;
import com.ersen.BookWorld.models.Book;
import com.ersen.BookWorld.models.BookList;
import com.ersen.BookWorld.models.eventbus.ChangeToolBarTitleEvent;
import com.ersen.BookWorld.network.NetworkErrorHandler;
import com.ersen.BookWorld.utils.RecyclerViewEndlessOnScrollListener;
import com.ersen.BookWorld.utils.VisibilityManager;
import com.ersen.BookWorld.views.RecyclerViewSpaceBetweenItems;
import com.ersen.BookWorld.views.adapters.BookSearchResultsAdapter;
import com.ersen.BookWorld.views.widgets.PlaceholderView;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BookSearchResults extends Fragment implements PlaceholderView.OnRetryButtonPressedListener {

    private RecyclerView mListOfBooks;
    private VisibilityManager mVisibilityManager;
    private BookSearchResultsAdapter mAdapter;
    private String mSearchQuery;
    private int mStartIndex; //Used for paging. 0 is default. Google Books API starts at 0 for the first element in the collection.
    private boolean mIsDoingSearch;

    public static BookSearchResults newInstance(String searchQueryParams) {
        BookSearchResults bookSearchResults = new BookSearchResults();
        Bundle args = new Bundle();
        args.putString(FaxiConstants.BundleConstants.SEARCH_QUERY_PARAMS, searchQueryParams);
        bookSearchResults.setArguments(args);
        return bookSearchResults;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchQuery = getArguments().getString(FaxiConstants.BundleConstants.SEARCH_QUERY_PARAMS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search_results, container, false);
        mListOfBooks = (RecyclerView) view.findViewById(R.id.list);
        mListOfBooks.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListOfBooks.setLayoutManager(linearLayoutManager);
        mListOfBooks.addItemDecoration(new RecyclerViewSpaceBetweenItems(10));
        mListOfBooks.addOnScrollListener(new RecyclerViewEndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                carryOutSearch();
            }
        });

        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
        mVisibilityManager = new VisibilityManager(placeholderView, mListOfBooks);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new ChangeToolBarTitleEvent(FaxiApplication.getInstance().getResources().getString(R.string.fragment_bookSearchResults)));
        handleInitialContent();
    }

    private void handleInitialContent() {
        if (mStartIndex == 0) { //If we are coming back from the back stack, only the view is destroyed but not the members.
            mAdapter = new BookSearchResultsAdapter(new ArrayList<Book>());
            mListOfBooks.setAdapter(mAdapter);
            mVisibilityManager.showLoading(FaxiApplication.getInstance().getResources().getString(R.string.loading_bookSearch));
            carryOutSearch();
        } else {
            mListOfBooks.setAdapter(mAdapter);
            mVisibilityManager.showMainContent();
        }
    }

    /** Checking mStartIndex is equal to 0 is a way to check if we have not on the first page already so that I can display failure or no books placeholder if it is the first request. This is important because if the user is scrolling
     *  to get the next page of results, I do not want to show these placeholder if something goes wrong thus
     * */
    private void carryOutSearch(){
        if(!mIsDoingSearch){
            if(mStartIndex > 0){ /** Show a progress item if the user is getting the next page of results */
                mAdapter.addProgressItem();
            }
            mIsDoingSearch = true;
            Call<BookList> call = FaxiApplication.getInstance().getNetworkAPI().searchForBooks(mSearchQuery,mStartIndex);
            call.enqueue(new Callback<BookList>() {
                @Override
                public void onResponse(Response<BookList> response, Retrofit retrofit) {
                    if(isAdded()){
                        mIsDoingSearch = false;
                        mAdapter.removeProgressItem(); /** Remove anyway, it is safe*/
                        if(response.isSuccess()){
                            ArrayList<Book> books = response.body().getBookItems();
                            if(books != null){ /** If the search yielded no books, the array is null so  */
                                if(mStartIndex == 0){
                                    mVisibilityManager.showMainContent();
                                }
                                mAdapter.addItems(books);
                                mStartIndex = mAdapter.getItemCount() + 1; /** Used for pagination. We must tell the API where we want to start in the collection of books. Simple calculation of our current adapter array size + 1 to start from the next book   */
                            }else{
                                if(mStartIndex == 0){
                                    mVisibilityManager.showFailure(FaxiApplication.getInstance().getResources().getString(R.string.error_no_books));
                                }
                            }
                        }else{
                            if(mStartIndex == 0){
                                mVisibilityManager.showFailure(NetworkErrorHandler.getUnsuccessfulRequestMessage(response));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mIsDoingSearch = false;
                    mAdapter.removeProgressItem();
                    if(mStartIndex == 0){
                        mVisibilityManager.showFailure(NetworkErrorHandler.getFailedRequestMessage(t));
                    }
                }
            });
        }

    }

    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }
}
