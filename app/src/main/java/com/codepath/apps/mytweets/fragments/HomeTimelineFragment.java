package com.codepath.apps.mytweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mytweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sheetal on 8/17/16.
 */
public class HomeTimelineFragment extends TweetsListFragment implements ComposeDialogFragment.ComposeDialogListener {

    private TwitterClient client;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  super.onCreateView(inflater, container, savedInstanceState);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG","In add on Scroll Listener"+ page);
                populateTimeline(page++);
            }

        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singletone client
        populateTimeline(1);

    }


    //send api request to get timeline Json
    // fill the ListView by creating tweet objects from Json.
    private void populateTimeline(int page) {

        Log.d("DEBUG", "Page: " + page);
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            //Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //Deserialize Json, Create models and load model data into listview

                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);

                addAll(arrayList);

            }

            //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }
        }, page);
    }


    @Override
    public void onFinishComposeDialog(Tweet newTweet) {

        Log.d("DEBUG", "New Tweet received"+ newTweet.getBody());
        add(newTweet);

    }
}
