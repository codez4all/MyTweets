package com.codepath.apps.mytweets.models;

/*

 [
  {
    "coordinates": null,
    "truncated": false,
    "created_at": "Tue Aug 28 21:16:23 +0000 2012",
    "favorited": false,
    "id_str": "240558470661799936",
    "in_reply_to_user_id_str": null,
    "entities": {
      "urls": [
 
      ],
      "hashtags": [
 
      ],
      "user_mentions": [
 
      ]
    },
    "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
      "location": "San Francisco, CA",
      "follow_request_sent": false,
      "id_str": "119476949",
      "is_translator": false,
      "profile_link_color": "0084B4",
      "entities": {
        "url": {
          "urls": [
            {
              "expanded_url": null,
              "url": "http://bit.ly/oauth-dancer",
              "indices": [
                0,
                26
              ],
              "display_url": null
            }
          ]
        },
        "description": null
      },
      "default_profile": false,
      "url": "http://bit.ly/oauth-dancer",
      "contributors_enabled": false,
      "favourites_count": 7,
      "utc_offset": null,
      "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "id": 119476949,
      "listed_count": 1,
      "profile_use_background_image": true,
      "profile_text_color": "333333",
      "followers_count": 28,
      "lang": "en",
      "protected": false,
      "geo_enabled": true,
      "notifications": false,
      "description": "",
      "profile_background_color": "C0DEED",
      "verified": false,
      "time_zone": null,
      "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "statuses_count": 166,
      "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "default_profile_image": false,
      "friends_count": 14,
      "following": false,
      "show_all_inline_media": false,
      "screen_name": "oauth_dancer"
    },
    "in_reply_to_screen_name": null,
    "in_reply_to_status_id": null
  },

    {
      ...
    }
]

 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

//Parse the Json+ Store data, encapsulate logic or display logic

public class Tweet  implements Serializable {

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    public boolean isReTweeted() {
        return isReTweeted;
    }

    public boolean isfavorited() {
        return isfavorited;
    }

    public long getFavouritesCount() {
        return favouritesCount;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setReTweeted(boolean reTweeted) {
        this.isReTweeted = reTweeted;
    }

    public void setIsfavorited(boolean isfavorited) {
        this.isfavorited = isfavorited;
    }

    public void setFavouritesCount(long favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    //list out the attributes
    private String body;
    private  long uid; //uniquue id for the tweet
    private User user;
    private String createdAt;
    private long retweetCount;
    private boolean isReTweeted;
    private boolean isfavorited;
    private long favouritesCount;



    //DeSerialize Json

    public  static Tweet fromJson(JSONObject jsonObject)
    {
        Tweet tweet = new Tweet();
        //Extract the values from Json and store

        try {
            tweet.body= jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJason(jsonObject.getJSONObject("user"));
            tweet.isReTweeted = jsonObject.getBoolean("retweeted");
            tweet.retweetCount = jsonObject.getLong("retweet_count");
            tweet.isfavorited = jsonObject.getBoolean("favorited");
            tweet.favouritesCount = jsonObject.getLong("favourites_count");

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return the Tweet object
        return  tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray)
    {
        ArrayList<Tweet> resultTweets = new ArrayList<>();

        for(int i =0; i<jsonArray.length(); i++)
        {
            try {
                resultTweets.add(fromJson(jsonArray.getJSONObject(i)));

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return resultTweets;
    }





}
