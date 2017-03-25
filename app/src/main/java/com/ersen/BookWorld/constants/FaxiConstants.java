package com.ersen.BookWorld.constants;

public class FaxiConstants {

    public static final class BundleConstants{
        public static final String SEARCH_QUERY_PARAMS = "searchQueryParams";
        public static final String BOOK_SELF_LINK = "bookSelfLink";
    }

    public static final class URLConstants{
        public static final String GOOGLE_BOOKS_API_BASE_URL = "https://www.googleapis.com/books/";
        public static final String GOOGLE_BOOKS_API_SEARCH_VOLUMES = "v1/volumes?projection=lite"; /** Lite mode to get a smaller set of the data which can help save some bandwidth because it is data that is not needed for the result page */
    }

    public static final class URLParamConstants{
        public static final String GOOGLE_BOOKS_API_SEARCH_PARAM = "q";
        public static final String GOOGLE_BOOKS_API_START_INDEX = "startIndex";
    }
}
