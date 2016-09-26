package com.codepath.apps.mytweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.fragments.UserTimelineFragment;
import com.codepath.apps.mytweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        client = TwitterApplication.getRestClient();

        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                user = User.fromJason(response);

                Log.d("DEBUG", user.getScreenName().toString());

                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);

            }


            @Override
            public void onFailure(int statusCode,  cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }

        });


        //get the screen_name

        //String screenName = user.getScreenName();
        String screenName = getIntent().getStringExtra("screen_name");

        Log.d("DEBUG","Screen_Name in ProfileActivity "+ screenName);

        if(savedInstanceState == null)
        {

            //create usertimeline fragment
            UserTimelineFragment fragmentUser = UserTimelineFragment.newInstance(screenName);
            //Display user fragment within activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUser);
            ft.commit();
        }
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu_main);
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return  true;
    }*/


  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/


    private void populateProfileHeader(User user)
    {
        Log.d("DEBUG","In populateProfileHeader. User = "+ user.getScreenName());
        TextView tvScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        TextView tvTagline = (TextView) findViewById(R.id.tvUserTag);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivUserProfile);

        tvScreenName.setText(user.getScreenName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }


}
