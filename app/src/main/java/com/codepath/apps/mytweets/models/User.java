package com.codepath.apps.mytweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
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
       "screen_name": "oauth_dancer"
    ....
    }
 */
public class User implements Serializable {

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    //List the attributes
    private  String name;
    private  long uid;
    private  String screenName;
    private  String profileImageUrl;

    public static User fromJason(JSONObject jsonObject)
    {
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id_str");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return  user;
    }

}
