package com.codepath.apps.mytweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mytweets.networking.TwitterApplication;
import com.codepath.apps.mytweets.networking.TwitterClient;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  super.onCreateView(inflater, container, savedInstanceState);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateUserTimeline();
            }
        });

        return v;
    }

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
                recycleAdapter.clear();
                //Deserialize Json, Create models and load model data into listview
                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);
                Log.d("DEBUG", "UserTimeLine"+ arrayList.toString());
                addAll(arrayList);
                swipeContainer.setRefreshing(false);
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
