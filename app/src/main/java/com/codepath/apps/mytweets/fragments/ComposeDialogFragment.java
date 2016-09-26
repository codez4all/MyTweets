package com.codepath.apps.mytweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ComposeDialogFragment extends DialogFragment {

    EditText etText;
    TextView tvUserName;
    Button btnTweet;
    private TwitterClient client;

    public  static ComposeDialogFragment newInstance()
    {
        ComposeDialogFragment dialogFragment = new ComposeDialogFragment();
        return  dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_compose,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etText = (EditText) view.findViewById(R.id.etTweetBody);
        tvUserName = (TextView) view.findViewById(R.id.tvUsernameCompose);
        btnTweet = (Button)view.findViewById(R.id.btnTweet);

        etText.requestFocus();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singletone client

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeTweet();
            }
        });

    }

    public  void  composeTweet()
    {

        client.updateHomeTime(new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent data = new Intent();
                Log.d("DEBUG", "Tweet Successfully posted - response=" + responseBody.toString());
                //data.putExtra("respose")
                try {
                    JSONObject jsonObject= new JSONObject(new String(responseBody));
                    Log.d("DEBUG","Tweet Object"+jsonObject.toString());

                    Tweet tweet = Tweet.fromJson(jsonObject);
                    // Pass relevant data back as a result
                    data.putExtra("newTweet", tweet);
                    data.putExtra("code", 200); // ints work too

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
        },etText.getText().toString());

    }

}
