package com.fors.erik.parstagram.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.fors.erik.parstagram.adapters.TimeLineAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimeLineFragment extends Fragment {

    //TAGS
    public static final String  TAG = "TimeLineFragment";

    //widget declaration
    private RecyclerView rvPost;
    private TimeLineAdapter adapter;

    //variables
    private List<Post> postList;

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
        //create layout for one row
        postList = new ArrayList<>();
        //create the adapter
        adapter = new TimeLineAdapter(getContext(),postList);
        //create data source
        //getting all the post
        //set adapter on recycler view
        rvPost.setAdapter(adapter);
        //set layout manager
        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPost();
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
}