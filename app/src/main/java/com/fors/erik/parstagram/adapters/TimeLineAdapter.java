package com.fors.erik.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Models.Like;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.Activities.PostDetailActivity;
import com.fors.erik.parstagram.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    public static final String TAG = "TimeLineAdapter";

    private Context context;
    private List<Post> posts;

    public TimeLineAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.instagram_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfilePic;
        ImageView ivPost;
        TextView tvUserName;
        TextView tvDescription;
        TextView tvDateTime;
        TextView tvUserNameInDescription;
        ImageButton ibLikeButton;
        ImageButton ibCommentButton;
        TextView tvViewAllComments;
        TextView tvnumberOfLikes;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivUserProfilePic);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvUserNameInDescription = itemView.findViewById(R.id.tvUsernameInDescription);
            ibCommentButton = itemView.findViewById(R.id.ibComment);
            tvViewAllComments = itemView.findViewById(R.id.tvViewAllComments);
            ibLikeButton = itemView.findViewById(R.id.ibLike);
            tvnumberOfLikes = itemView.findViewById(R.id.tvNumberOfLikes);
        }

        public void bind(Post post) {

            //checking to see if the post is liked
            if (!post.isLikedByUser){
                seeIfPostIsLikedByUser(post);
                ibLikeButton.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
                tvnumberOfLikes.setText("0 Likes");
            }
            else {
                Drawable mIcon = context.getDrawable(R.drawable.filled_hearth);
                ibLikeButton.setImageDrawable(mIcon);
                tvnumberOfLikes.setText(post.numberOfLikes + " Likes");
            }

            String pattern = "MMM dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(post.getCreatedDate());
            //bind post
            tvDescription.setText(post.getDescription());
            tvUserName.setText(post.getUser().getUsername());
            tvUserNameInDescription.setText(post.getUser().getUsername());
            tvDateTime.setText(date);
            ParseFile image = post.getImage();
            ParseFile profileImage = (ParseFile) post.getUser().get("profile_picture");

            if(image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPost);
            }

            if(profileImage != null){
                Glide.with(context).load(profileImage.getUrl()).placeholder(context.getDrawable(R.drawable.avatar)).into(ivProfilePic);
            }
            else {
                ivProfilePic.setImageResource(R.drawable.avatar);
            }

            //send user to post detail
            ivPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToDetailScreen(post.getId());
                }
            });

            ibCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToDetailScreen(post.getId());
                }
            });

            tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToDetailScreen(post.getId());
                }
            });

            ibLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //if post is already liked, get rid of the like
                    //else like the post
                    if(post.isLikedByUser){
                        removeLike(post);
                    }
                    else{
                        likePost(post);
                    }

                }
            });

        }

        private void removeLike(Post post) {

            //getting post that are older than lattest date loaded
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.whereEqualTo(Like.KEY_POST, post);
            query.whereEqualTo(Like.KEY_USER,ParseUser.getCurrentUser());
            query.getFirstInBackground(new GetCallback<Like>() {
                @Override
                public void done(Like like, ParseException e) {

                    //something went wrong
                    if(e != null){
                        Log.e(TAG,"There was a problem disliking!", e);
                        Toast.makeText(context, "There was a problem disliking", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        like.delete();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                        Log.e(TAG,"There was a problem disliking!", e);
                        Toast.makeText(context, "There was a problem disliking", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    post.isLikedByUser = false;
                    post.numberOfLikes--;

                    tvnumberOfLikes.setText(post.numberOfLikes + " Likes");
                    ibLikeButton.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
                }
            });

        }

        private void likePost(Post post) {

            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUser(ParseUser.getCurrentUser());

            newLike.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    //something went wrong
                    if(e != null){
                        Log.e(TAG,"There was a problem liking the post!!", e);
                        Toast.makeText(context, "There was a problem liking the post", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.i(TAG,"Post " + post.getId() + "was liked by " + ParseUser.getCurrentUser().getUsername());
                    post.isLikedByUser = true;
                    post.numberOfLikes++;

                    tvnumberOfLikes.setText(post.numberOfLikes + " Likes");
                    Drawable mIcon = context.getDrawable(R.drawable.filled_hearth);
                    ibLikeButton.setImageDrawable(mIcon);

                }
            });
        }

        private void seeIfPostIsLikedByUser(Post post) {
            //getting post that are older than lattest date loaded
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.include(Like.KEY_USER);
            query.whereEqualTo(Like.KEY_POST, post);

            query.findInBackground(new FindCallback<Like>() {
                @Override
                public void done(List<Like> likes, ParseException e) {

                    //something went wrong
                    if(e != null){
                        Log.e(TAG,"There was a problem loading the likes!!", e);
                        Toast.makeText(context, "There was a problem loading the likes", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tvnumberOfLikes.setText(likes.size() + " Likes");
                    post.numberOfLikes = likes.size();

                    for(Like oneLike : likes){
                        if(oneLike.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            Drawable mIcon = context.getDrawable(R.drawable.filled_hearth);
                            ibLikeButton.setImageDrawable(mIcon);
                            post.isLikedByUser = true;
                        }
                    }

                }
            });
        }

        private void sendToDetailScreen(String postID) {
            Intent i = new Intent(context, PostDetailActivity.class);

            i.putExtra("postID", postID);

            context.startActivity(i);
        }
    }
}
