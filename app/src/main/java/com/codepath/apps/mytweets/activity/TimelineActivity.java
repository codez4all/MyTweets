package com.codepath.apps.mytweets.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.fragments.ComposeDialogFragment;
import com.codepath.apps.mytweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mytweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mytweets.fragments.TweetsListFragment;
import com.codepath.apps.mytweets.models.Tweet;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimelineActivity extends AppCompatActivity
          implements ComposeDialogFragment.ComposeDialogListener{

    private TweetsListFragment fragmentTweets;

    private final int REQUEST_CODE = 200;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Get ViewPager

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        //Attach ViewPager to PagerAdapter
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        //Find the sliding tabstrip
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //attach tabstrip to ViewPager
        tabStrip.setViewPager(viewPager);


       if(savedInstanceState== null) {
            fragmentTweets = new TweetsListFragment();
        }
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu_main);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable drawable = menu.findItem(R.id.miCompose).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorLightGray));
        menu.findItem(R.id.miCompose).setIcon(drawable);

        Drawable drawable2 = menu.findItem(R.id.miProfile).getIcon();
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, ContextCompat.getColor(this, R.color.colorLightGray));
        menu.findItem(R.id.miProfile).setIcon(drawable2);

        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("DEBUG", "Menu item id selected " + item.getTitle());

        switch (item.getItemId()) {

           // call Compose activity on compose icon click in toolbar
           case R.id.miCompose: {
               FragmentManager fm = getSupportFragmentManager();
               ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance();
               composeDialogFragment.show(fm,"fragment_compose");
           }

            case R.id.miProfile: {
                onProfileView(item);

            }

           default:
                return super.onOptionsItemSelected(item);
       }
    }


    public void onProfileView(MenuItem item) {

        Intent iProfile = new Intent(TimelineActivity.this,ProfileActivity.class);
        startActivity(iProfile);

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


  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

    @Override
    public void onFinishComposeDialog(Tweet newTweet) {

        Log.d("DEBUG", "New Tweet received"+ newTweet.getBody());
        fragmentTweets.add(newTweet);

    }
}
