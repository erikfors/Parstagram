package com.fors.erik.parstagram.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.fors.erik.parstagram.adapters.EndlessRecyclerViewScrollListener;
import com.fors.erik.parstagram.adapters.TimeLineAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeLineFragment extends Fragment {

    //TAGS
    public static final String  TAG = "TimeLineFragment";

    //widget declaration
    private RecyclerView rvPost;
    SwipeRefreshLayout swipeRefreshLayout;

    //variables
    private List<Post> postList;
    EndlessRecyclerViewScrollListener scrollListener;
    private TimeLineAdapter adapter;

    public TimeLineFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //finding views by id
        rvPost =  view.findViewById(R.id.rvTimeline);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        //create layout for one row
        postList = new ArrayList<>();
        //create the adapter
        adapter = new TimeLineAdapter(getContext(),postList);
        //create data source
        //getting all the post
        //set adapter on recycler view
        rvPost.setAdapter(adapter);
        //set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPost.setLayoutManager(linearLayoutManager);
        //setting refresh layout loading bar colors
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        //listeners
        
        //load more infinite scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG,"onLoadMore: " + page);
                //Toast.makeText(getContext(), "Loading more ...", Toast.LENGTH_SHORT).show();
                try {
                    loadMoreData();
                }
                catch (Exception e){
                    Log.d(TAG,"Error in load more!",e);
                }
            }
        };
        //setting refresh layout listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"Fetching new data");
                queryPost();
            }
        });

        //adding scroll listener to rv
        rvPost.addOnScrollListener(scrollListener);

        queryPost();
    }

    private void loadMoreData() throws java.text.ParseException {

        //getting last date loaded
        Date lastPostDate = postList.get(postList.size() - 1).getCreatedDate();

        //getting post that are older than lattest date loaded
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereLessThan(Post.KEY_CREATED_AT,lastPostDate);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.setLimit(20);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem loading the posts!!", e);
                    Toast.makeText(getContext(), "There was a problem loading the posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                //going throgh all the posts
                for(Post post : posts){
                    Log.i(TAG,"Post: " + post.getDescription());
                }

                //add all
                postList.addAll(posts);
                //notify adapter
                adapter.notifyDataSetChanged();
            }
        });
    }

    //gets all the posts
    private void queryPost() {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {

                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem loading the posts!!", e);
                    Toast.makeText(getContext(), "There was a problem loading the posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                //this succeed

                //clearing array list
                postList.clear();
                //set refreshing to false
                swipeRefreshLayout.setRefreshing(false);

                //add all
                postList.addAll(posts);
                //notify adapter
                adapter.notifyDataSetChanged();
            }
        });
    }
}