package com.fors.erik.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.fors.erik.parstagram.Models.Comment;
import com.fors.erik.parstagram.R;
import com.parse.ParseFile;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    public static final String TAG = "CommentAdapter";

    int layoutResource;


    public CommentAdapter(@NonNull Context context, int resource, List<Comment> comments) {
        super(context, resource);
        this.layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutResource,parent,false);

        Comment comment = getItem(position);

        Log.i(TAG,"Here in comment adapter" + comment.getUser().getUsername());

        ImageView ivUserImage = view.findViewById(R.id.ivUserProfilePicComment);
        TextView tvUserName = view.findViewById(R.id.tvUsernameComment);
        TextView tvCommentText = view.findViewById(R.id.tvCommentText);

        ParseFile userPhoto = (ParseFile) comment.getUser().get("profile_picture");

        if(userPhoto != null)
            Glide.with(getContext()).load(userPhoto.getUrl()).placeholder(R.drawable.avatar).into(ivUserImage);

        tvCommentText.setText(comment.getComment());

        tvUserName.setText(comment.getUser().getUsername());

        return view;
    }

    @Override
    public void add(@Nullable Comment object) {
        super.add(object);
        notifyDataSetChanged();
    }
}
