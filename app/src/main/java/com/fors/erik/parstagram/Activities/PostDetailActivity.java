package com.fors.erik.parstagram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Models.Comment;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.fors.erik.parstagram.adapters.CommentAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    //TAGS
    public static final String TAG = "PostDetailActivity";

    //widgets
    ImageView ivPostImage;
    ImageView ivPrifilePic;
    TextView tvUserName;
    TextView tvDescription;
    TextView tvTimeStamp;
    ListView listView;
    EditText etCommentBox;
    Button btnPostComment;

    //variables
    private Post post;
    private String id;
    List<Comment> commentList;
    CommentAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //getting post throug intent
        id = getIntent().getStringExtra("postID");

        //initializing variables
        commentList = new ArrayList<>();

        //settings views by id
        ivPostImage = findViewById(R.id.ivPostDetail);
        ivPrifilePic = findViewById(R.id.ivUserProfilePicDetail);
        tvUserName = findViewById(R.id.tvUsernameDetail);
        tvDescription = findViewById(R.id.tvDescriptionDetail);
        tvTimeStamp = findViewById(R.id.tvTimeStampDetail);
        listView = findViewById(R.id.lvComments);
        etCommentBox = findViewById(R.id.etCommentBox);
        btnPostComment = findViewById(R.id.btnPostComment);

        commentsAdapter = new CommentAdapter(this, R.layout.comment, commentList);

        listView.setAdapter(commentsAdapter);

        //get post
        getPost();

    }

    private void getPost() {

        //getting post id
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_OBJECT_ID,id);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {

                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem loading the post!!", e);
                    Toast.makeText(PostDetailActivity.this, "There was a problem loading the post", Toast.LENGTH_SHORT).show();
                    return;
                }


                //get post
                post = objects.get(0);
                //was a success, load widgets
                loadWidgets();
            }
        });
    }

    private void loadWidgets() {
        //variables
        ParseFile postImage = post.getImage();
        ParseFile userProfileImage = (ParseFile) post.getUser().get("profile_picture");

        //setting the views
        if(postImage != null)
            Glide.with(this).load(postImage.getUrl()).into(ivPostImage);

        if(userProfileImage != null)
            Glide.with(this).load(userProfileImage.getUrl()).placeholder(R.drawable.avatar).into(ivPrifilePic);

        tvUserName.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimeStamp.setText(post.getCreatedDate().toString());

        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAComent();
            }
        });

        //get comments
        loadComments();
    }

    private void postAComent() {

        //get edit text text
        String comment = etCommentBox.getText().toString();
        //validate that it is not empty
        if(comment.isEmpty()){
            Toast.makeText(this,"Comment can not be empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        //post comment
        postCommentQuery(comment);
    }

    private void postCommentQuery(String text) {
        //create comment
        Comment comment = new Comment();
        comment.setComment(text);
        comment.setPost(post);
        comment.setUser(ParseUser.getCurrentUser());

        //save in background
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem posting the comment!", e);
                    Toast.makeText(PostDetailActivity.this, "There was a problem posting the comment!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //clear text
                etCommentBox.setText("");
                //add comment to UI
                commentsAdapter.add(comment);

            }
        });
    }

    private void loadComments() {

        //getting post comments
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_POST,post);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {

                //something went wrong
                if(e != null){
                    Log.e(TAG,"There was a problem loading the comments!!", e);
                    Toast.makeText(PostDetailActivity.this, "There was a problem loading the comments", Toast.LENGTH_SHORT).show();
                    return;
                }


                //going throgh all the posts
                for(Comment comment : comments){
                    Log.i(TAG,"Comment: " + comment.getComment());
                    commentsAdapter.add(comment);
                }
                //Log.i(TAG,"Comment load was a success!!" + comments.get(0).getComment());
                //Toast.makeText(PostDetailActivity.this, "Loading comments was a success", Toast.LENGTH_SHORT).show();

            }
        });


    }


}