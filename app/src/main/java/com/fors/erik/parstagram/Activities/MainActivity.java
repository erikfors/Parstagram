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


                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentNav, selectedFragment).commit();

                return true;
            }
        });

        //showing the timeline as default
       getSupportFragmentManager().beginTransaction().replace(R.id.fragmentNav, new TimeLineFragment()).commit();

    }



}