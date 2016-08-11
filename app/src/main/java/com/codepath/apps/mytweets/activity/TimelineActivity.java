package com.codepath.apps.mytweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mytweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mytweets.adapter.TweetsRecycleAdapter;
import com.codepath.apps.mytweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private final int REQUEST_CODE = 200;
    private RecyclerView recyclerView;
    private TweetsRecycleAdapter recycleAdapter;

    private long lastSince_Id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

       Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //find listview
       // lvTweets = (ListView)findViewById(R.id.lvTweets);

        //Create arraylist
        tweets = new ArrayList<>();

        //Create adapter
      //  aTweets = new TweetsArrayAdapter(this.getApplicationContext(),tweets);

        //bind arraylist to listview using adapter
       //  lvTweets.setAdapter(aTweets);

        client = TwitterApplication.getRestClient(); //singletone client



        //endless scrolling using Recycleview
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);

        recycleAdapter = new TweetsRecycleAdapter(this.getApplicationContext(),tweets);
        recyclerView.setAdapter(recycleAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page++);
            }

        });

        populateTimeline(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu_main);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch (item.getItemId()) {

            // Log.d("DEBUG",ParseUser.getCurrentUser().toString());
           // i.putExtra("UserName", ParseUser.getCurrentUser().getUsername());

           // call Compose activity on compose icon click in toolbar
           case R.id.miCompose: {
               Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
               //startActivity(i);
               startActivityForResult(i, REQUEST_CODE);

           }

           default:
           return super.onOptionsItemSelected(item);
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = (Tweet)data.getSerializableExtra("newTweet");
            int code = data.getExtras().getInt("code", 0);

            tweets.add(0,tweet);

           // aTweets.insert(tweet,0);
           // aTweets.notifyDataSetChanged();

            recycleAdapter.notifyItemInserted(0);
        }
    }

    //send api request to get timeline Json
   // fill the ListView by creating tweet objects from Json.
    private void populateTimeline(int page) {

        Log.d("DEBUG","Page: "+ page);
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            //Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //Deserialize Json, Create models and load model data into listview

                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);

                tweets.addAll(arrayList);
                Log.d("DEBUG","getHomeTimeline tweets:"+ tweets.toString());


                //  aTweets.addAll(tweets);
              //  aTweets.notifyDataSetChanged();
                int curSize = recycleAdapter.getItemCount();
                Log.d("DEBUG", "Recycle adapter Size before:" + curSize);

                recycleAdapter.notifyItemRangeInserted(curSize, tweets.size());

                Log.d("DEBUG", "Recycle adapter Size After:" + recycleAdapter.getItemCount());
                Log.d("DEBUG","getHomeTimeline with recycleAdapter:"+ recycleAdapter.toString());


                //get last tweet's id
               /* if(tweets.size() > 0) {
                    lastSince_Id = arrayList.get((arrayList.size() - 1)).getUid();
                    Log.d("DEBUG","tweet size:"+arrayList.size());
                    Log.d("DEBUG","last since_id:"+lastSince_Id);

                }*/


            }

            //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }
        },page);

    }

    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes

        populateTimeline(page);

    }

}
