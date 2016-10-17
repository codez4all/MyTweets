package com.codepath.apps.mytweets.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sheetal on 8/5/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public  TweetsArrayAdapter(Context context,List<Tweet> tweets)
    {
        super(context, R.layout.support_simple_spinner_dropdown_item,tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        //get Tweet
        Tweet tweet = getItem(position);

        //find or inflate the template
        if(convertView== null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        TextView tvTimestamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);

        //populate the data into subviews
        tvUserName.setText(tweet.getUser().getScreenName());
        Log.d("DEBUG", tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        Log.d("DEBUG", tweet.getBody());
        ivProfileImage.setImageResource(0);
        Picasso.with(this.getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        tvBody.setTextColor(Color.BLACK);
        tvUserName.setTextColor(Color.BLACK);

        String relativeDate = TwitterUtil.getRelativeTimeAgo(tweet.getCreatedAt().toString());
        tvTimestamp.setText(relativeDate);

        return  convertView;
    }


}
