package com.fors.erik.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.Activities.PostDetailActivity;
import com.fors.erik.parstagram.R;
import com.parse.ParseFile;

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
        ImageButton ibCommentButton;
        TextView tvViewAllComments;

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
        }

        public void bind(Post post) {

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

        }

        private void sendToDetailScreen(String postID) {
            Intent i = new Intent(context, PostDetailActivity.class);

            i.putExtra("postID", postID);

            context.startActivity(i);
        }
    }
}
