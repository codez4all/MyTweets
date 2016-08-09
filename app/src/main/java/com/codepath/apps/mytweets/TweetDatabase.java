package com.codepath.apps.mytweets;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.models.User;

/**
 * Created by sheetal on 8/8/16.
 */
public class TweetDatabase extends SQLiteOpenHelper {

    private  static  TweetDatabase sInstance;

    // Database Info
    private static final String DATABASE_NAME = "myTweetDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TWEETS = "tweets";
    private static final String TABLE_USERS = "users";

    //Tweet table column
    private static final String KEY_TWEET_ID = "id";
    private static  final String KEY_TWEET_USER_ID_FK = "userId";
    private  static final String KEY_TWEET_BODY = "body";
    private  static final String KEY_TWEET_CREATEDAT = "createdAt";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    //SingleTone - make only one database instance.
    public static synchronized TweetDatabase getsInstance(Context context)
    {
        if(sInstance == null)
        {
            sInstance = new TweetDatabase(context.getApplicationContext());
        }
        return sInstance;
    }


    public TweetDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TWEET_TABLE = "CREATE TABLE "+ TABLE_TWEETS +
                "(" +
                    KEY_TWEET_ID + "INTEGER PRIMARY KEY," +
                    KEY_TWEET_USER_ID_FK + "INTEGER REFERENCES" + TABLE_USERS + "," +
                    KEY_TWEET_BODY + "TEXT," +
                    KEY_TWEET_CREATEDAT + "TEXT" +

                ")";


        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                ")";

        try {

            db.execSQL(CREATE_TWEET_TABLE);
            db.execSQL(CREATE_USERS_TABLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEETS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    // Insert a post into the database
    public void addTweet(Tweet tweet) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            long userId = addOrUpdateUser(tweet.getUser());

            ContentValues values = new ContentValues();
            values.put(KEY_TWEET_USER_ID_FK, userId);
            values.put(KEY_TWEET_BODY, tweet.getBody());
            values.put(KEY_TWEET_CREATEDAT,tweet.getCreatedAt());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TWEETS, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("DEBUG", "Error while trying to add tweet to database");
        } finally {
            db.endTransaction();
        }
    }


    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.getScreenName());
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.getProfileImageUrl());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.getScreenName()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getScreenName())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getLong(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("DEBUG", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }
}
