package com.codepath.apps.mytweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.activity.ProfileActivity;
import com.codepath.apps.mytweets.adapter.TweetsRecycleAdapter;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.models.User;
import com.codepath.apps.mytweets.networking.TwitterApplication;
import com.codepath.apps.mytweets.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sheetal on 8/17/16.
 */
public class TweetsListFragment extends Fragment implements ComposeDialogFragment.ComposeDialogListener
{

    private ArrayList<Tweet> tweets;
    protected TweetsRecycleAdapter recycleAdapter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    protected SwipeRefreshLayout swipeContainer;
    protected ImageButton ibRetweet;
    private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_tweets_list,container,false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setAdapter(recycleAdapter);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);



        return v;
    }

    //inflate logic
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singletone client

        tweets = new ArrayList<>();
        recycleAdapter = new TweetsRecycleAdapter(this.getActivity(),tweets, this);

    }


    public void add(Tweet tweet)
    {
        tweets.add(0, tweet);
        recycleAdapter.notifyItemInserted(0);
    }

    protected void addAll(ArrayList<Tweet> newTweets)
    {
        int curSize = recycleAdapter.getItemCount();
        Log.d("DEBUG","RecycleAdapter Current Size Before :" + curSize);

        tweets.addAll(newTweets);
        recycleAdapter.notifyItemRangeInserted(curSize, newTweets.size());

        curSize = recycleAdapter.getItemCount();
        Log.d("DEBUG", "RecycleAdapter Current Size After:" + curSize);

    }

    public void onRetweetUnretweet(final int position)
    {
        final Tweet tweet = tweets.get(position);

         client.postReTweetUnReTweet(new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);


                 Tweet reTweetResponse =  Tweet.fromJson(response);

                 if(tweet.isReTweeted())
                 {
                     tweet.setReTweeted(false);

                     if(reTweetResponse.getRetweetCount() == tweet.getRetweetCount()) {
                         tweet.setRetweetCount(tweet.getRetweetCount() - 1);
                     }
                     else {
                         tweet.setRetweetCount(reTweetResponse.getRetweetCount());
                     }
                 }
                 else
                 {
                     tweet.setReTweeted(true);

                     if(reTweetResponse.getRetweetCount() == tweet.getRetweetCount()){
                         tweet.setRetweetCount(tweet.getRetweetCount() + 1);
                     }else {
                         tweet.setRetweetCount(reTweetResponse.getRetweetCount());
                     }
                 }

                 tweets.set(position, tweet);
                 recycleAdapter.notifyItemChanged(position);
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 super.onFailure(statusCode, headers, throwable, errorResponse);

                 Log.d("DEBUG",errorResponse.toString());
             }

         },tweet.getUid(),tweet.isReTweeted());
    }



    public void onFavoriteCreateDestroy(final  int position)
    {
        final Tweet tweet = tweets.get(position);

        client.postFavoriteCreateDestroy(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Tweet tweetResponse = Tweet.fromJson(response);

                if(tweet.isfavorited())
                {
                    tweet.setIsfavorited(false);

                    if(tweetResponse.getFavouritesCount() == tweet.getFavouritesCount()) {
                        tweet.setFavouritesCount(tweet.getFavouritesCount() - 1);
                    }
                    else {
                        tweet.setFavouritesCount(tweetResponse.getFavouritesCount());
                    }
                }
                else
                {
                    tweet.setIsfavorited(true);

                    if(tweetResponse.getFavouritesCount() == tweet.getFavouritesCount()){
                        tweet.setFavouritesCount(tweet.getFavouritesCount() + 1);
                    }else {
                        tweet.setFavouritesCount(tweetResponse.getFavouritesCount());
                    }
                }

                tweets.set(position, tweet);
                recycleAdapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG",errorResponse.toString());
            }

        },tweet.getUid(), tweet.isfavorited());

    }

    //show user profile when clicked on a profile image
    public void onShowProfile(Context context, User user) {

        Intent iProfile = new Intent(context,ProfileActivity.class);
        iProfile.putExtra("user", user);

        startActivity(iProfile);
    }

    public void onReply(Tweet tweetFrom)
    {
        FragmentManager fm = getFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(tweetFrom);
        composeDialogFragment.show(fm,"fragment_compose");
    }


    public void addNewTweet(Tweet newTweet)
    {
        add(newTweet);
        Log.d("DEBUG","In method addNewTweet - HomeTimeline Fragment ");
    }

    @Override
    public void onFinishComposeDialog(Tweet newTweet) {

        // TimelineActivity sends newTweet to HomeTimelineFragment through method
        addNewTweet(newTweet);
    }

}
