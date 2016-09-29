package com.codepath.apps.mytweets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.ParseRelativeDate;
import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

//import android.widget.TextView;

/**
 * Created by sheetal on 8/9/16.
 */
public class TweetsRecycleAdapter extends RecyclerView.Adapter<TweetsRecycleAdapter.ViewHolder> {

    public  static  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTimestamp;

        public ViewHolder(View tweetView)
        {
            super(tweetView);
            ivProfileImage = (ImageView)tweetView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView)tweetView.findViewById(R.id.tvUserName);
            tvBody = (TextView)tweetView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView)tweetView.findViewById(R.id.tvTimeStamp);
        }

    }


    // Store a member variable for the tweets
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;

    // Pass in the tweet array into the constructor
    public TweetsRecycleAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetsRecycleAdapter.ViewHolder viewHolder, int position) {

        //get item
        Tweet tweet = mTweets.get(position);
      //  Log.d("DEBUG", "Tweet body in onBlindViewHolder: " + tweet.getBody());

        //TextView tBody = viewHolder.tvBody;

       // Log.d("DEBUG","ViewHolder textview body: "+viewHolder.tvBody);
        viewHolder.tvBody.setText(tweet.getBody().toString());

       // TextView tUserName = viewHolder.tvUserName;
        viewHolder.tvUserName.setText(tweet.getUser().getScreenName().toString());

       // TextView tTimeStamp = viewHolder.tvTimestamp;
        String relativeDate = ParseRelativeDate.getFormattedRelativeTime(tweet.getCreatedAt());
        viewHolder.tvTimestamp.setText(relativeDate);

       // ImageView ivProfile = viewHolder.ivProfileImage;
        viewHolder.ivProfileImage.setImageResource(0);


        Picasso.with(this.getContext())
            .load(tweet.getUser().getProfileImageUrl())
            .transform(TwitterUtil.getRoundedCornersTreansformation())
            .into(viewHolder.ivProfileImage);

    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
