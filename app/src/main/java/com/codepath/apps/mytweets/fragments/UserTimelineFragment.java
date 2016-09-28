package com.codepath.apps.mytweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sheetal on 8/22/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singletone client

        populateUserTimeline();

    }

    public static UserTimelineFragment newInstance(String screen_name)
    {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userTimelineFragment.setArguments(args);

        return  userTimelineFragment;
    }

    //send api request to get timeline Json
    // fill the ListView by creating tweet objects from Json.
    private void populateUserTimeline() {

        String screenName = getArguments().getString("screen_name");
        Log.d("DEBUG","Screen Name in Populate User Timeline: "+ screenName);

        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {

            //Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //Deserialize Json, Create models and load model data into listview
                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);
                Log.d("DEBUG", "UserTimeLine"+ arrayList.toString());
                addAll(arrayList);
            }


            //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

}
