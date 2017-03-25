package com.ersen.BookWorld.models;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Book {
    @SerializedName("selfLink")
    private String mLinkToFullDetail;

    @SerializedName("volumeInfo")
    private BookInfo mBookInfoLite;

    public String getLinkToFullDetail() {
        return mLinkToFullDetail;
    }

    public BookInfo getBookInfo() {
        return mBookInfoLite;
    }

    public static class BookInfo {
        @SerializedName("title")
        private String mTitle;

        @SerializedName("publishedDate")
        private String mPublishedDate;

        @SerializedName("authors")
        private String[] mAuthors;

        @SerializedName("publisher")
        private String mPublisher;

        @SerializedName("pageCount")
        private int mPageCount;

        @SerializedName("language")
        private String mLanguage;

        @SerializedName("categories")
        private String[] mCatagories;

        @SerializedName("description")
        private String mDescription;

        @SerializedName("averageRating")
        private double mAverageRating;

        @SerializedName("ratingsCount")
        private int mAmountOfRatings;

        @SerializedName("infoLink")
        private String mUrlToBookOnGoogleBooks;

        @SerializedName("imageLinks")
        private BookImages mImages;

        @SerializedName("industryIdentifiers")
        private ArrayList<BookIdentifiers> mBookIdentifiers;

        /** Returns the language of the book based on its content in two letter ISO 639-1 format i.e. en or fr*/
        public String getLanguage(){
            return mLanguage.toUpperCase();
        }

        public String getDescription(){
            /** Some books may not have a description so let's check it */
            if(TextUtils.isEmpty(mDescription)){
                return FaxiApplication.getInstance().getResources().getString(R.string.book_detail_noDescription); /** Return a place holder */
            }
            return mDescription; /** Return the description because it is not empty */
        }

        @Nullable
        public String getCategory(){
            if(mCatagories != null){ /** it appears that some books may not have any categories */
                String category = "";
                for(String string : mCatagories){
                    category += string + " ";
                }
                return category;
            }
            return FaxiApplication.getInstance().getResources().getString(R.string.book_detail_noCategories);
        }

        public String getBookIdentifiers() {
            if(mBookIdentifiers != null){
                String identifiers = "";
                for(BookIdentifiers bookIdentifiers : mBookIdentifiers){
                    identifiers += (bookIdentifiers.getType().replaceAll("_", " ")) + ": " + bookIdentifiers.getIdentifier() + "\n";
                }
                return identifiers.trim();
            }
            return  FaxiApplication.getInstance().getResources().getString(R.string.book_detail_noIdentifiers);
        }

        /** The average rating for this book minimum is 1.0 and max is 5.0 */
        public double getAverageRating() {
            return mAverageRating;
        }

        public String getPublisher() {
            return mPublisher;
        }

        public int getPageCount() {
            return mPageCount;
        }

        public int getAmountOfRatings() {
            return mAmountOfRatings;
        }

        public String getUrlToBookOnGoogleBooks() {
            return mUrlToBookOnGoogleBooks;
        }


        public String getTitle() {
            return mTitle;
        }

        public String getPublishedDate() {
            return mPublishedDate;
        }

        @Nullable
        public String getAuthors() {
            if(mAuthors != null){
                return TextUtils.join(",", mAuthors);
            }
            return null;
        }

        @Nullable
        public BookImages getBookImages() {
            return mImages;
        }
    }

    public static class BookImages {
        @SerializedName("thumbnail")
        private String mThumbnail;

        @SerializedName("small")
        private String mSmall;

        @SerializedName("medium")
        private String mMedium;

        @SerializedName("large")
        private String mLarge;

        @SerializedName("extraLarge")
        private String mExtraLarge;

        /** The API may not have these images so each image(starting from largest) is checked if it exists. The fall back is the thumbnail. */
        public String getLargestPossibleImage(){
            if(!TextUtils.isEmpty(mExtraLarge)){
                return mExtraLarge;
            }

            if(!TextUtils.isEmpty(mLarge)){
                return mLarge;
            }

            if(!TextUtils.isEmpty(mMedium)){
                return mMedium;
            }

            if(!TextUtils.isEmpty(mSmall)){
                return mSmall;
            }

            return mThumbnail;
        }
    }

    /** Means stuff like ISBN 10 or ISBN 13 etc. */
    public static class BookIdentifiers{
        @SerializedName("type")
        private String mType;

        @SerializedName("identifier")
        private String mIdentifer;

        public String getType() {
            return mType;
        }

        public String getIdentifier() {
            return mIdentifer;
        }
    }
}
