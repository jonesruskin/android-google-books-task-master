package com.ersen.BookWorld.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;
import com.ersen.BookWorld.models.eventbus.ChangeToolBarTitleEvent;
import com.ersen.BookWorld.models.eventbus.LaunchBookSearchFragmentEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class BookSearch extends Fragment implements View.OnClickListener {

    private static final String GOOGLE_BOOKS_SEARCH_IN_AUTHOR_TERM = "inauthor:%s";
    private static final String GOOGLE_BOOKS_SEARCH_IN_TITLE_TERM = "intitle:%s";
    private static final String GOOGLE_BOOKS_SEARCH_IN_ISBN_TERM = "isbn:%s";
    private static final String GOOGLE_BOOKS_SEARCH_PHRASE_WORD_OR_PASSAGE= "\"%s\"";

    private EditText mBookName,mAuthor,mISBN, mBookContainingWordOrPhrase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search,container,false);
        mBookName = (EditText) view.findViewById(R.id.bookName);
        mAuthor = (EditText)view.findViewById(R.id.author);
        mISBN = (EditText)view.findViewById(R.id.isbn);
        mBookContainingWordOrPhrase = (EditText)view.findViewById(R.id.containingWordOrPhrase);
        Button search = (Button)view.findViewById(R.id.search);
        search.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new ChangeToolBarTitleEvent(FaxiApplication.getInstance().getResources().getString(R.string.fragment_bookSearch)));
    }

    private String prepareSearchQueryParameter(){
        String bookName = mBookName.getText().toString().trim();
        String author = mAuthor.getText().toString().trim();
        String isbn = mISBN.getText().toString().trim();
        String containingWordOrPhrase = mBookContainingWordOrPhrase.getText().toString().trim();

        //We cannot have empty params in the query i.e. intitle: (note the emptiness after the colon)
        ArrayList<String> inputtedSearchTerms = new ArrayList<>();

        if(!TextUtils.isEmpty(bookName)){
            bookName = String.format(GOOGLE_BOOKS_SEARCH_IN_TITLE_TERM,bookName);
            inputtedSearchTerms.add(bookName);
        }

        if(!TextUtils.isEmpty(author)){
            author = String.format(GOOGLE_BOOKS_SEARCH_IN_AUTHOR_TERM,author);
            inputtedSearchTerms.add(author);
        }

        if(!TextUtils.isEmpty(isbn)){
            isbn = String.format(GOOGLE_BOOKS_SEARCH_IN_ISBN_TERM,isbn);
            inputtedSearchTerms.add(isbn);
        }

        if(!TextUtils.isEmpty(containingWordOrPhrase)){
            containingWordOrPhrase = String.format(GOOGLE_BOOKS_SEARCH_PHRASE_WORD_OR_PASSAGE,containingWordOrPhrase);
            inputtedSearchTerms.add(containingWordOrPhrase);
        }

        //Every search param must be separated by a plus sign so use the TextUtils join to do this but must convert the array list to array because this is what that method excepts)
        String[] tempArray = new String[inputtedSearchTerms.size()];
        return TextUtils.join("+",inputtedSearchTerms.toArray(tempArray));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:
                String searchQuery = prepareSearchQueryParameter();
                if(TextUtils.isEmpty(searchQuery)){
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.book_search_nothingInputted), Toast.LENGTH_SHORT).show();
                }else{
                    EventBus.getDefault().post(new LaunchBookSearchFragmentEvent(searchQuery));
                }
                break;
        }
    }
}
