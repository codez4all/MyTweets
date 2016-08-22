package com.codepath.apps.mytweets.fragments;

/**
 * Created by sheetal on 8/17/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {

  /* private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page++);
            }

        });

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singletone client

        populateTimeline(1);

    }

    //send api request to get timeline Json
    // fill the ListView by creating tweet objects from Json.
    public void populateTimeline(int page) {

        Log.d("DEBUG", "Page: " + page);
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            //Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //Deserialize Json, Create models and load model data into listview

                ArrayList<Tweet> arrayList = Tweet.fromJsonArray(json);

                // tweets.addAll(arrayList);
                addAll(arrayList);


                //  aTweets.addAll(tweets);
                //  aTweets.notifyDataSetChanged();

                // int curSize = recycleAdapter.getItemCount();

                // recycleAdapter.notifyItemRangeInserted(curSize, tweets.size());

                //get last tweet's id
                if(tweets.size() > 0) {
                    lastSince_Id = arrayList.get((arrayList.size() - 1)).getUid();
                    Log.d("DEBUG","tweet size:"+arrayList.size());
                    Log.d("DEBUG","last since_id:"+lastSince_Id);

                }


            }

            //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("DEBUG", errorResponse.toString());
            }
        },page);

    }

    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes

        populateTimeline(page);

    }*/


}
