package com.codepath.instagram.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bill on 12/1/15.
 */
public class InstagramClient {
    private static  String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private static String BASE_URL = "https://api.instagram.com/v1/media/popular";
    //private static final String BASE_URL = "https://api.twitter.com/1/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private  static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void getPopularFeed(final JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(BASE_URL, params, responseHandler);

    }

    public static void getComments(String mediaId, final JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        String url = String.format("https://api.instagram.com/v1/media/%s/comments", mediaId);
        params.put("client_id", CLIENT_ID);
        client.get(url, params, responseHandler);

    }

}
