package com.codepath.apps.mytweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.adapter.TweetsRecycleAdapter;
import com.codepath.apps.mytweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by sheetal on 8/17/16.
 */
public class TweetsListFragment extends Fragment{

    private ArrayList<Tweet> tweets;
    private TweetsRecycleAdapter recycleAdapter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_tweets_list,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setAdapter(recycleAdapter);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

       /* recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page++);
            }

        });*/

        return v;
    }

    //inflate logic
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        recycleAdapter = new TweetsRecycleAdapter(this.getActivity(),tweets);

    }



    public void add(Tweet tweet)
    {
        tweets.add(0, tweet);

        recycleAdapter.notifyItemInserted(0);

    }

    public void addAll(ArrayList<Tweet> tweetz)
    {
        this.tweets.addAll(tweetz);

        int curSize = recycleAdapter.getItemCount();

        recycleAdapter.notifyItemRangeInserted(curSize, tweets.size());

    }
}