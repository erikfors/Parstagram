package com.fors.erik.parstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Models.Post;
import com.fors.erik.parstagram.R;
import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

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

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivUserProfilePic);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }

        public void bind(Post post) {

            String pattern = "MMM dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(post.getCreatedDate());
            //bind post
            tvDescription.setText(post.getDescription());
            tvUserName.setText(post.getUser().getUsername());
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

        }
    }
}
