package com.codepath.apps.mytweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.adapter.TweetsArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

       Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //find listview
        lvTweets = (ListView)findViewById(R.id.lvTweets);

        //Create arraylist
        tweets = new ArrayList<>();

        //Create adapter
        aTweets = new TweetsArrayAdapter(this.getApplicationContext(),tweets);

        //bind arraylist to listview using adapter
        lvTweets.setAdapter(aTweets);

        client = TwitterApplication.getRestClient(); //singletone client

        populateTimeline();
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

            // tweets.add(0,tweet);
            aTweets.insert(tweet,0);
            aTweets.notifyDataSetChanged();
        }
    }

    //send api request to get timeline Json
   // fill the ListView by creating tweet objects from Json.
    private void populateTimeline() {

        client.getHomeTimeline(new JsonHttpResponseHandler(){

            //Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //Deserialize Json, Create models and load model data into listview

                tweets = Tweet.fromJsonArray(json);
                aTweets.addAll(tweets);
                aTweets.notifyDataSetChanged();

                Log.d("DEBUG", aTweets.toString());


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
