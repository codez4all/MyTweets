package com.codepath.apps.mytweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mytweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mytweets.fragments.TweetsListFragment;
import com.codepath.apps.mytweets.models.Tweet;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TweetsListFragment fragmentTweets;

    private ArrayList<Tweet> tweets;
    private final int REQUEST_CODE = 200;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

       Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //Get ViewPager

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        //Attach ViewPager to PagerAdapter
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        //Find the sliding tabstrip
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //attach tabstrip to ViewPager
        tabStrip.setViewPager(viewPager);


        //Create arraylist
        tweets = new ArrayList<>();
        /*if(savedInstanceState== null) {
            fragmentTweets = (TweetsListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_timeline);
        }*/
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
          //  fragmentTweets.add(tweet);

           // recycleAdapter.notifyItemInserted(0);
           // aTweets.insert(tweet,0);
           // aTweets.notifyDataSetChanged();
        }
    }



    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        private  String tabTitles[]={"Home","Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
            {
                return  new HomeTimelineFragment();
            }
            else if(position ==1)
            {
             return  new MentionsTimelineFragment();
            }
            else{
                return  null;}
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }



}
