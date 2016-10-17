package com.codepath.apps.mytweets.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytweets.R;
import com.codepath.apps.mytweets.networking.TwitterApplication;
import com.codepath.apps.mytweets.networking.TwitterClient;
import com.codepath.apps.mytweets.models.Tweet;
import com.codepath.apps.mytweets.models.User;
import com.codepath.apps.mytweets.utils.TwitterUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;


public class ComposeDialogFragment extends DialogFragment  {

    EditText etNewWeet;
    TextView tvUserName;
    Button btnTweet;
    Button btnCancel;
    ImageView ivProfileImageCompose;
    User user;
    private Tweet newTweet;


    private TwitterClient client;
    private ComposeDialogListener mListener;

    // for Compose option
    public static ComposeDialogFragment newInstance(Tweet tweetFrom)
    {
        ComposeDialogFragment dialogFragment = new ComposeDialogFragment();

        // for reply option bundle the tweet from original user and send to dialog fragment
        // if tweetFrom is null, it is simply compose option
        if(tweetFrom != null)
        {
            Bundle args = new Bundle();
            args.putSerializable("TweetFrom", tweetFrom);
            dialogFragment.setArguments(args);
        }
        return  dialogFragment;
    }


    public ComposeDialogFragment()
    {
        //empty constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_compose,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNewWeet = (EditText) view.findViewById(R.id.etTweetBody);
        tvUserName = (TextView) view.findViewById(R.id.tvUsernameCompose);
        ivProfileImageCompose = (ImageView) view.findViewById(R.id.ivProfileImageCompose);
        btnTweet = (Button)view.findViewById(R.id.btnTweet);

        // for reply option set the @FromUserName in body
        if(getArguments() != null) {

            Tweet tweetFrom = (Tweet) getArguments().getSerializable("TweetFrom");
            if (tweetFrom != null)
            {
                etNewWeet.setText( "@" +tweetFrom.getUser().getScreenName() + " ");
            }
        }

        etNewWeet.requestFocus();

        //on Tweet button click
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeTweet();

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        client = TwitterApplication.getRestClient(); //singletone client

        client.getCurrentUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                user = User.fromJason(response);
                //set username
                StringTokenizer token = new StringTokenizer(user.getName());
                tvUserName.setText(token.nextToken() +" @" + user.getScreenName());
                //set profile image
                Picasso.with(getContext())
                    .load(user.getProfileImageUrl())
                    .transform(TwitterUtil.getRoundedCornersTreansformation())
                    .into(ivProfileImageCompose);

            }
            @Override
            public void onFailure(int statusCode,  cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }

        });

        }

    public void composeTweet()
    {

        client.updateHomeTime(new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("DEBUG", "Tweet Successfully posted - response=" + responseBody.toString());

                try {
                    JSONObject jsonObject= new JSONObject(new String(responseBody));
                    Log.d("DEBUG","Tweet Object"+jsonObject.toString());

                    newTweet = Tweet.fromJson(jsonObject);

                    if(mListener !=null && newTweet !=null) {

                        mListener.onFinishComposeDialog(newTweet);
                        dismiss(); // close dialogfragment

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("DEBUG","Tweet post failed- Error="+error.toString());

            }
        },etNewWeet.getText().toString());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ComposeDialogListener)
        {
            mListener = (ComposeDialogListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
            + " must implement OnFinishComposeDialog");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Defines the listener interface
    public interface ComposeDialogListener {

        void onFinishComposeDialog(Tweet newTweet);

    }

}
