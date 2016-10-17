package com.codepath.apps.mytweets.utils;

import android.text.format.DateUtils;

import com.codepath.apps.mytweets.adapter.RoundedCornersTransformation;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class TwitterUtil {

  public TwitterUtil()
  {

  }

  public static Transformation getRoundedCornersTreansformation(){
    final int radius = 5;
    final int margin = 0;
    final Transformation transformation = new RoundedCornersTransformation(radius, margin);

    return transformation;
  }



  // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
  public static String getRelativeTimeAgo(String rawJsonDate) {
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

  public static String getFormattedRelativeTime(String rawJsonDate) {


    String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    sf.setLenient(true);

    String relativeDate = "";

    try {

      Date timestamp = sf.parse(rawJsonDate);

      String relativeTime = DateUtils.getRelativeTimeSpanString(timestamp.getTime()).toString();

      String[] words = relativeTime.split("\\s+");
      if (relativeTime.startsWith("in")) {
        relativeDate = "new";
      } else {
        if (words.length > 1) {
          if (words[0].length() > 2) {
            relativeDate = String.format("%s %s", words[0], words[1].charAt(0));
          } else {
            relativeDate = String.format("%s%s", words[0], words[1].charAt(0));
          }

        } else {
          relativeDate = words[0];
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return relativeDate;
  }


  public static boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int     exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException e)          { e.printStackTrace(); }
    catch (InterruptedException e) { e.printStackTrace(); }
    return false;
  }

}
