package com.codepath.apps.mytweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.networking.TwitterApplication;
import com.codepath.apps.mytweets.networking.TwitterClient;
import com.codepath.apps.mytweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Toolbar toolbarCompose = (Toolbar)findViewById(R.id.toolbarCompose);
        toolbarCompose.setTitle("Compose Tweet");

        setSupportActionBar(toolbarCompose);

        client = TwitterApplication.getRestClient(); //singletone client
        tweet = new Tweet();

        // String userName =  getIntent().getStringExtra("UserName");
        TextView tvUsername =  (TextView)findViewById(R.id.tvUsernameCompose);
        // tvUsername.setText(ParseUser.getCurrentUser().getUsername().toString());

        Button btnTweet = (Button)findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeTweet();
            }
        });


    }

    public  void  composeTweet()
    {
        EditText tweetText = (EditText)findViewById(R.id.etTweetBody);


        client.updateHomeTime(new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent data = new Intent();
                Log.d("DEBUG", "Tweet Successfully posted - response=" + responseBody.toString());
                //data.putExtra("respose")
                try {
                    JSONObject jsonObject= new JSONObject(new String(responseBody));
                    Log.d("DEBUG","Tweet Object"+jsonObject.toString());

                    tweet = Tweet.fromJson(jsonObject);
                    // Pass relevant data back as a result
                    data.putExtra("newTweet", tweet);
                    data.putExtra("code", 200); // ints work too
                    setResult(RESULT_OK,data);
                    finish();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

               // JSONObject jsonObject =(JSONObject) responseBody;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("DEBUG","Tweet post failed- Error="+error.toString());

            }
        },tweetText.getText().toString());

    }



}
