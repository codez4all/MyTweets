package com.codepath.apps.mytweets.utils;

import com.codepath.apps.mytweets.adapter.RoundedCornersTransformation;
import com.squareup.picasso.Transformation;

/**
 * Created by Shyam Rokde on 9/28/16.
 */

public class TwitterUtil {

  public static Transformation getRoundedCornersTreansformation(){
    final int radius = 5;
    final int margin = 0;
    final Transformation transformation = new RoundedCornersTransformation(radius, margin);

    return transformation;
  }
}
