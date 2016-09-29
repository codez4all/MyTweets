package com.codepath.apps.mytweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.TwitterApplication;
import com.codepath.apps.mytweets.TwitterClient;
import com.codepath.apps.mytweets.fragments.UserTimelineFragment;
import com.codepath.apps.mytweets.models.User;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.StringTokenizer;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();

        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                user = User.fromJason(response);

                Log.d("DEBUG", user.getScreenName().toString());
                populateProfileHeader(user);

                //create usertimeline fragment
                UserTimelineFragment fragmentUser = UserTimelineFragment.newInstance(user.getScreenName());
                //Display user fragment within activity dynamically
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flContainer, fragmentUser);
                ft.commit();
            }


            @Override
            public void onFailure(int statusCode,  cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }

        });


    }




    private void populateProfileHeader(User user)
    {
        Log.d("DEBUG","In populateProfileHeader. User = "+ user.getScreenName());
        //TextView tvScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvUserTag);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivUserProfile);

        //tvScreenName.setText(user.getScreenName());
        StringTokenizer stok = new StringTokenizer(user.getName());
        tvName.setText(stok.nextToken());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");


        Picasso.with(this)
            .load(user.getProfileImageUrl())
            .transform(TwitterUtil.getRoundedCornersTreansformation())
            .fit()
            .into(ivProfileImage);
    }


}
