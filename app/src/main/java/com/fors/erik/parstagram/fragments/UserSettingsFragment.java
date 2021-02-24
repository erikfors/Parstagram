package com.fors.erik.parstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Activities.LoginActivity;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.fors.erik.parstagram.adapters.TimeLineAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserSettingsFragment extends Fragment {

    //TAG
    public static final String TAG = "UserSettingFragment";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    //member variables
    private File photoFile;
    private String photoFileName = "photo.jpg";
    ParseUser parseUser;
    TimeLineAdapter timeLineAdapter;
    private List<Post> userPostList;

    //widgets
    Button btnLogOut;
    ImageView ivUserProfilePicture;
    TextView tvUserName;
    TextView tvStartDate;
    RecyclerView rvUserPosts;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        //initializing view
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        //initializing variables
        parseUser = ParseUser.getCurrentUser();
        userPostList = new ArrayList<>();
        timeLineAdapter = new TimeLineAdapter(getContext(),userPostList);
        
        //finding view by id
        btnLogOut = view.findViewById(R.id.btnLogOut);
        ivUserProfilePicture = view.findViewById(R.id.ivUserProfileAccount);
        tvUserName = view.findViewById(R.id.tvUserNameAccount);
        tvStartDate = view.findViewById(R.id.tvDateOfStart);
        rvUserPosts = view.findViewById(R.id.rvUserPosts);

        //setting adapter
        rvUserPosts.setAdapter(timeLineAdapter);
        //set layout manager
        rvUserPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPost();

        //setting widgets
        tvUserName.setText(parseUser.getUsername());

        //setting date format
        String pattern = "E, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(parseUser.getCreatedAt());

        tvStartDate.setText(date);

        //setting user picture if user has one
        ParseFile userProfilePic = (ParseFile) parseUser.get(PROFILE_PICTURE);
        if(userProfilePic != null)
        Glide.with(getContext()).load(userProfilePic.getUrl()).placeholder(getActivity().getDrawable(R.drawable.avatar)).into(ivUserProfilePicture);
        
        //listener

        //logout
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log user out
                logOutUser();
            }
        });

        //change profile pic
        ivUserProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        
        // Inflate the layout for this fragment
        return view;
    }

    private void queryPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                userPostList.addAll(posts);
                //notify adapter
                timeLineAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivUserProfilePicture.setImageBitmap(takenImage);

                //save image of the user
                saveProfilePic();

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfilePic() {

        //save the picture
        parseUser.put(PROFILE_PICTURE,new ParseFile(photoFile));

        //upload to database
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                //check if fail
                if(e != null){
                    Log.d(TAG,"Image fail to upload!",e);
                    Toast.makeText(getContext(), "Image fail to upload!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i(TAG,"Image save was a success!");
                Toast.makeText(getContext(), "Image save was a success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //launches the camera and get photo
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    //logs user out and send to log in activity
    private void logOutUser() {
        ParseUser.logOut();

        if(ParseUser.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        else {
            Toast.makeText(getContext(), "Failed to Log Out!", Toast.LENGTH_SHORT).show();
        }

        }

    }
