package com.codepath.apps.mytweets.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.models.User;

import java.util.ArrayList;

/**
 * Created by sheetal on 8/8/16.
 */

public  class TweetDatabase extends SQLiteOpenHelper {

    private  static TweetDatabase sInstance;

    // Database Info
    private static final String DATABASE_NAME = "TweetDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_TWEETS = "tweets";
    private static final String TABLE_USERS = "users";

    //Tweet table column
    private static final String KEY_TWEET_ID = "id";
    private  static  final String KEY_TWEET_UID = "uId";
    private  static final String KEY_TWEET_BODY = "body";
    private  static final String KEY_TWEET_CREATEDAT = "createdAt";
    private  static  final String KEY_TWEET_USERID_FK = "userId";

    //Users table Columns info
    private  static  final String KEY_USER_ID = "id";
    private  static  final String KEY_USER_UID = "uid";
    private  static  final String KEY_USER_NAME = "name";
    private  static  final String KEY_USER_SCRNNAME = "screenName";
    private  static  final String KEY_USER_IMGURL = "profileImgUrl";
    private  static  final String KEY_USER_TAGLINE = "tagLine";
    private  static  final String KEY_USER_FOLLOWERSCOUNT = "followersCount";
    private  static  final String KEY_USER_FRIENDSCOUNT = "friendsCount";

    //SingleTone - make only one database instance.
    public static synchronized TweetDatabase getInstance(Context context)
    {
        if(sInstance == null)
        {
            sInstance = new TweetDatabase(context.getApplicationContext());
        }
        Log.d("DEBUG","db instance created");
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

        String CREATE_TWEETS_TABLE = "CREATE TABLE "+ TABLE_TWEETS +
                "(" +
                KEY_TWEET_ID + " INTEGER PRIMARY KEY," +
                KEY_TWEET_UID +" INTEGER," +
                KEY_TWEET_BODY +" TEXT," +
                KEY_TWEET_CREATEDAT +" TEXT," +
                KEY_TWEET_USERID_FK +" TEXT REFERENCES " + TABLE_USERS +
                ")";


        String CREATE_USERS_TABLE = "CREATE TABLE "+ TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_UID +" INTEGER," +
                KEY_USER_NAME +" TEXT," +
                KEY_USER_SCRNNAME +" TEXT," +
                KEY_USER_IMGURL +" TEXT," +
                KEY_USER_TAGLINE +" TEXT," +
                KEY_USER_FOLLOWERSCOUNT +" TEXT, "+
                KEY_USER_FRIENDSCOUNT +" TEXT " +
                ")";

        String ALTER_TWEETS_TABLE =  "ALTER TABLE "+ TABLE_TWEETS +
                "ADD CONSTRAINT UK_TWEET_UID UNIQUE " + "(" + KEY_TWEET_UID + ")";

        String ALTER_USERS_TABLE =  "ALTER TABLE "+ TABLE_USERS +
                "ADD CONSTRAINT UK_USER_UID UNIQUE " + "(" + KEY_USER_UID + ")";


        try {

            db.execSQL(CREATE_TWEETS_TABLE);
            db.execSQL(CREATE_USERS_TABLE);

            db.execSQL(ALTER_TWEETS_TABLE);
            db.execSQL(ALTER_USERS_TABLE);

            Log.d("DEBUG","tables created");
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

            Log.d("DEBUG", "DB Upgraded");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    // add list item to database
    public void addTweetToDB(Tweet tweetItem)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            long userId = addOrUpdateUser(tweetItem.getUser());

            ContentValues values = new ContentValues();
            values.put(KEY_TWEET_UID, String.valueOf(tweetItem.getUid()));
            values.put(KEY_TWEET_BODY, tweetItem.getBody());
            values.put(KEY_TWEET_CREATEDAT,tweetItem.getCreatedAt());
            //values.put(KEY_TWEET_USERID_FK, String.valueOf(tweetItem.getUser().getUid()));

            db.insertOrThrow(TABLE_TWEETS,null,values);
            db.setTransactionSuccessful();

            Log.d("DEBUG", "tweet added to database: "+ tweetItem.toString());

        }catch (Exception e)
        {
            Log.d("DEBUG","Error while trying to add tweet item to database");

        }finally {
            db.endTransaction();
        }

    }


    public long addOrUpdateUser(User user)
    {
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_USER_UID, user.getUid());
            values.put(KEY_USER_NAME, user.getName());
            values.put(KEY_USER_SCRNNAME, user.getScreenName());
            values.put(KEY_USER_IMGURL, user.getProfileImageUrl());
            values.put(KEY_USER_TAGLINE, user.getTagline());
            values.put(KEY_USER_FOLLOWERSCOUNT, user.getFollowersCount());
            values.put(KEY_USER_FRIENDSCOUNT, user.getFriendsCount());

            // First try to update the user in case the user already exists in the database
            // This assumes userId are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_UID + "= ?"
                    ,new String []{String.valueOf(user.getUid())});

            // Check if update succeeded
            if(rows == 1)
            {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_UID);

                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getUid())});

                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
            else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }


        }catch (Exception e)
        {
            Log.d("DEBUG","Error while trying to add or update User");
        }
        finally {
            db.endTransaction();
        }

        return  userId;
    }




    public ArrayList<Tweet> getAllTweetsFromDB()
    {
        ArrayList<Tweet> tweetsArray = new ArrayList<>();

        // SELECT * FROM TWEETS
        // LEFT OUTER JOIN USERS
        // ON TWEETS.KEY_TWEET_USERID_FK = USERS.KEY_USER_ID

        String TWEETS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_TWEETS,
                        TABLE_USERS,
                        TABLE_TWEETS, KEY_TWEET_USERID_FK,
                        TABLE_USERS, KEY_USER_ID);

        // String TWEET_SELECT_QUERY = String.format("Select * from " + TABLE_TWEET);

        SQLiteDatabase db= getReadableDatabase();

        Cursor cursor = db.rawQuery(TWEETS_SELECT_QUERY,null);

        try
        {
            if (cursor.moveToFirst())
            {
                do {
                    User newUser = new User();
                    newUser.setUid(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
                    newUser.setName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                    newUser.setScreenName(cursor.getString(cursor.getColumnIndex(KEY_USER_SCRNNAME)));
                    newUser.setTagline(cursor.getString(cursor.getColumnIndex(KEY_USER_TAGLINE)));
                    newUser.setProfileImageUrl(cursor.getString(cursor.getColumnIndex(KEY_USER_IMGURL)));
                    newUser.setFollowersCount(cursor.getInt(cursor.getColumnIndex(KEY_USER_FOLLOWERSCOUNT)));
                    newUser.setFollowingCount(cursor.getInt(cursor.getColumnIndex(KEY_USER_FRIENDSCOUNT)));

                    Tweet newItem = new Tweet();
                    newItem.setUid(cursor.getLong(cursor.getColumnIndex(KEY_TWEET_UID)));
                    newItem.setBody(cursor.getString(cursor.getColumnIndex(KEY_TWEET_BODY)));
                    newItem.setCreatedAt(cursor.getString(cursor.getColumnIndex(KEY_TWEET_CREATEDAT)));
                    newItem.setUser(newUser);

                    tweetsArray.add(newItem);

                }while (cursor.moveToNext());
            }

        }
        catch (Exception e)
        {
            Log.d("DEBUG","Error while reading tweets from database");
            e.printStackTrace();
        }
        finally {
            if(cursor !=null&& !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d("DEBUG", "Read tweets from DB: "+ tweetsArray.toString());
        return  tweetsArray;
    }


    public void deleteAllTweetsFromDB()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.delete(TABLE_TWEETS,null,null);
            db.delete(TABLE_USERS,null,null);
            db.setTransactionSuccessful();

        }catch (Exception e)
        {
            Log.d("DEBUG", "Error while trying to delete all tweets");
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }
}