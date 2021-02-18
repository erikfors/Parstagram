package com.fors.erik.parstagram.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.fors.erik.parstagram.fragments.CreatePostFragment;
import com.fors.erik.parstagram.fragments.TimeLineFragment;
import com.fors.erik.parstagram.fragments.UserSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //tags
   public static final String TAG = "MainActivity";

   //widget declaration
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding views by id
        bottomNavigationView = findViewById(R.id.bnvBottomNavigation);
        bottomNavigationView.setItemIconTintList(null );

        //listener

        //bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.timeLineFragment:
                        selectedFragment = new TimeLineFragment();
                        break;
                    case R.id.createPostFragment:
                        selectedFragment = new CreatePostFragment();
                        break;
                    case R.id.userSettingsFragment:
                        selectedFragment = new UserSettingsFragment();
                        break;
                }

                switchIcons(item.getItemId());

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentNav, selectedFragment).commit();

                return true;
            }
        });

        //showing the timeline as default
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentNav, new TimeLineFragment()).commit();

        //getting all the post
        //queryPost();
    }

    private void switchIcons(int itemId) {

    }

    //gets all the posts
    private void queryPost() {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {

                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem loading the posts!!", e);
                    Toast.makeText(MainActivity.this, "There was a problem loading the posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                //this succeed

                //going throgh all the posts
                for(Post post : posts){
                    Log.i(TAG,"Post: " + post.getDescription());
                }
            }
        });
    }
}