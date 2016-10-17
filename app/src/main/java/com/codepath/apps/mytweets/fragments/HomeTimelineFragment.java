package com.codepath.apps.mytweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mytweets.adapter.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.networking.TwitterApplication;
import com.codepath.apps.mytweets.networking.TwitterClient;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.codepath.apps.mytweets.utils.TweetDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sheetal on 8/17/16.
 */
public class HomeTimelineFragment extends TweetsListFragment  {

    private TwitterClient client;
    TweetDatabase dbHelper;



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

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                populateTimeline(1);
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = TweetDatabase.getInstance(getContext());

        client = TwitterApplication.getRestClient(); //singletone client

        if (TwitterUtil.isOnline()) {
            populateTimeline(1);
        }
        else
        {
            ArrayList<Tweet> readTweetsFromDB = dbHelper.getAllTweetsFromDB();
            addAll(readTweetsFromDB);
        }
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

                recycleAdapter.clear();
                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);
                addAll(arrayList);
                swipeContainer.setRefreshing(false);

                //add tweets to db
                for (int i=0; i< arrayList.size(); i++)
                {
                    dbHelper.addTweetToDB(arrayList.get(i));
                }
            }

            //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }
        }, page);
    }


    /*public void addNewTweet(Tweet newTweet)
    {
        add(newTweet);
        Log.d("DEBUG","In method addNewTweet - HomeTimeline Fragment ");
    }*/
}
