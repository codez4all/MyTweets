package com.codepath.apps.mytweets.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.activity.TimelineActivity;
import com.codepath.apps.mytweets.fragments.TweetsListFragment;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.models.User;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

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
        public ImageView ivRetweet;
        public TextView tvRetweetcount;
        public ImageView ivFavorite;
        public TextView tvFavoritesCount;
        public ImageView ivReply;


        public ViewHolder(View tweetView)
        {
            super(tweetView);
            ivProfileImage = (ImageView)tweetView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView)tweetView.findViewById(R.id.tvUserName);
            tvBody = (TextView)tweetView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView)tweetView.findViewById(R.id.tvTimeStamp);
            ivRetweet = (ImageView) tweetView.findViewById(R.id.ivRetweet);
            tvRetweetcount = (TextView) tweetView.findViewById(R.id.tvRetweetcount);
            ivFavorite = (ImageView) tweetView.findViewById(R.id.ivFavorite);
            tvFavoritesCount = (TextView) tweetView.findViewById(R.id.tvFavoritesCount);
            ivReply = (ImageView) tweetView.findViewById(R.id.ivReply);
        }

    }


    // Store a member variable for the tweets
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;
    private TweetsListFragment mfgTweetList;
    private TimelineActivity timelineActivity;

    // Pass in the tweet array into the constructor
    public TweetsRecycleAdapter(Context context, List<Tweet> tweets, TweetsListFragment fragment ) {
        mTweets = tweets;
        mContext = context;
        mfgTweetList = fragment;
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

        //get Tweet item
        final Tweet tweet = mTweets.get(position);

        //get User
        final User user = tweet.getUser();

        //body
        viewHolder.tvBody.setText(tweet.getBody().toString());

        // username
        StringTokenizer strToken = new StringTokenizer(tweet.getUser().getName());
        viewHolder.tvUserName.setText(strToken.nextToken() +" @"+ tweet.getUser().getScreenName().toString());

       // timestamp
        String relativeDate = TwitterUtil.getFormattedRelativeTime(tweet.getCreatedAt());
        viewHolder.tvTimestamp.setText(relativeDate);

       // ProfileImage
        viewHolder.ivProfileImage.setImageResource(0);

        Picasso.with(this.getContext())
            .load(tweet.getUser().getProfileImageUrl())
            .transform(TwitterUtil.getRoundedCornersTreansformation())
            .into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*timelineActivity = new TimelineActivity();
                timelineActivity.onProfileView(mContext,user);*/

                mfgTweetList.onShowProfile(mContext, user);

            }
        });

        // ReTweets
        viewHolder.ivRetweet.setTag(position);
        viewHolder.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (int) v.getTag();
                if(mfgTweetList != null)
                    mfgTweetList.onRetweetUnretweet(position);
            }
        });

        if(tweet.isReTweeted()) {
            viewHolder.ivRetweet.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGreen));
        }

        viewHolder.tvRetweetcount.setText(String.valueOf(tweet.getRetweetCount()));


        //Favourites
        viewHolder.ivFavorite.setTag(position);
        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                if (mfgTweetList != null)
                    mfgTweetList.onFavoriteCreateDestroy(position);
            }
        });

        if(tweet.isfavorited()) {
            viewHolder.ivFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorRed));
        }

        viewHolder.tvFavoritesCount.setText(String.valueOf(tweet.getFavouritesCount()));


        //Reply
        viewHolder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfgTweetList.onReply(tweet);
            }
        });
    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    public void clear() {

        mTweets.clear();
        notifyDataSetChanged();
    }
}
