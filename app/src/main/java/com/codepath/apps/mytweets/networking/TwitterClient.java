package com.codepath.apps.mytweets.networking;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "khHStWUnH3lg0knIooYxwxXUy";       // Change this
	public static final String REST_CONSUMER_SECRET = "pJGOD5hQ2y0uTay0agFF34mb91u1kUdYf554L216crTUBmygig"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://SmwMyTweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    // Method == EndPoint

    //Get HomeTimeline
    //Resource URL- GET  statuses/home_timeline.json
    //count = 25
    //since_id  = 1
    public void getHomeTimeline(AsyncHttpResponseHandler handler, int page)
    {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",25);
        params.put("since_id", 1);
        params.put("page", page);

        //Execute the request
        getClient().get(apiUrl, params, handler);

    }


    //Post new tweet
    public void updateHomeTime(AsyncHttpResponseHandler handler, String tweetText)
    {
        String apiUrl = getApiUrl("statuses/update.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("status", tweetText);

        getClient().post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",25);
        params.put("since_id", 1);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",25);
        params.put("screen_name", screenName);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }


    public void getCurrentUserInfo(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        //Execute the request
        getClient().get(apiUrl, null, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler, long userId, String screenName)
    {
        //String apiUrl = getApiUrl("account/verify_credentials.json");

        String apiUrl = getApiUrl("users/show.json");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("screen_name", screenName);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void postReTweetUnReTweet(AsyncHttpResponseHandler handler, long retweetId, boolean unRetweet)
    {
        String apiUrl;
        if(unRetweet)
        {
            apiUrl = getApiUrl(String.format("statuses/unretweet/%s.json", retweetId));
        }
        else
        {
            apiUrl = getApiUrl(String.format("statuses/retweet/%s.json", retweetId));
        }

        Log.d("DEBUG", "apiUrl: "+ apiUrl);

        RequestParams params = new RequestParams();
        params.put("trim_user", true);

        getClient().post(apiUrl, params, handler);

    }


    public void postFavoriteCreateDestroy(AsyncHttpResponseHandler handler, long tweetId, boolean unFavorite)
    {
        String apiUrl;
        if(unFavorite)
        {

            apiUrl = getApiUrl(String.format("favorites/destroy.json?id=%s", tweetId));
        }
        else
        {
            apiUrl = getApiUrl(String.format("favorites/create.json?id=%s",tweetId));
        }

        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);

    }

    @Override
    protected String getApiUrl(String path) {
        String apiUrl = super.getApiUrl(path);
        Log.d("DEBUG", "apiUrl: "+ apiUrl);
        return apiUrl;
    }
}