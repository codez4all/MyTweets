package com.codepath.apps.mytweets;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by sheetal on 8/5/16.
 */

public class ParseRelativeDate {

    public ParseRelativeDate()
    {

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static  String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static String getFormattedRelativeTime(String rawJsonDate){


        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";

        try {

            Date timestamp = sf.parse(rawJsonDate);

            String relativeTime = DateUtils.getRelativeTimeSpanString(timestamp.getTime()).toString();

            String[] words = relativeTime.split("\\s+");
            if(relativeTime.startsWith("in")){
                relativeDate = "new";
            }else{
                if(words.length > 1){
                    if(words[0].length() > 1){
                        relativeDate = String.format("%s %s", words[0], words[1].charAt(0));
                    }else{
                        relativeDate = String.format("%s%s", words[0], words[1].charAt(0));
                    }

                }else{
                    relativeDate = words[0];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
