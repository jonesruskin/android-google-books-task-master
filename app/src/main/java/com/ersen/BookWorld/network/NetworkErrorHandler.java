package com.ersen.BookWorld.network;

import com.ersen.BookWorld.R;
import com.ersen.BookWorld.application.FaxiApplication;
import java.io.IOException;


import retrofit.Response;

public class NetworkErrorHandler {
    /**
     * This is when the server responds with a non http 200 status request and has some kind of message to display
     *
     * @param response : the entire response obtained from the call
     * @return A string that contains the response to display
     */
    public static String getUnsuccessfulRequestMessage(Response response) {
        //TODO Find out what Google Books API error body looks like so that it can be deserialize and displayed
        return FaxiApplication.getInstance().getResources().getString(R.string.error_server_unknown,response.code());
    }

    /**
     * This is when we get failures from our side rather than the server i.e. our network connection dropped etc.
     */
    public static String getFailedRequestMessage(Throwable throwable) {
        if(throwable instanceof IOException){ //Java SocketTimeoutException is a sub class of IOException so IOException covers all the possible network communication errors
            return FaxiApplication.getInstance().getResources().getString(R.string.error_network_badConnection);
        }

       //Unknown cause
        return FaxiApplication.getInstance().getResources().getString(R.string.error_network_unknown);
    }

}
