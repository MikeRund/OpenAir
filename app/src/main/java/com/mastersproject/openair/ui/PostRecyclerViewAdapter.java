package com.mastersproject.openair.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mastersproject.openair.R;
import com.mastersproject.openair.model.Post;


import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public PostRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item2, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerViewAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);
        String postImageURL;
        String profileImageURL;

        holder.userTitle.setText(post.getUsername());
        holder.description.setText(post.getDescription());
        holder.activityType.setText(post.getActivityType());
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                post.getTimestamp().getSeconds()*1000);
        holder.dateAdded.setText(timeAgo);
        postImageURL = post.getPostImageURL();
        profileImageURL = post.getProfileImageURL();
        Glide.with(context).load(postImageURL).fitCenter().into(holder.postImage);
        Glide.with(context).load(profileImageURL).into(holder.profileImage);


//        holder.title.setText(post.getTitle());
//        holder.description.setText(post.getDescription());
//        holder.name.setText(post.getUsername());
//        imageUrl = post.getImageUrl();
//
//        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
//                post.getTimestamp().getSeconds()*1000);
//        holder.dateAdded.setText(timeAgo);
//
//        // Using Glide Library to Display the images
//        Glide.with(context).load(imageUrl) //.placeholder()
//                .fitCenter().into(holder.image);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public PostRecyclerViewAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    // View Holder:
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userTitle, description, activityType, dateAdded;
        public ImageView postImage, profileImage, sendImage;
        public EditText commentBar;
        String userID;
        String username;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // These Widgets belong to Journal_row.xml
            userTitle = itemView.findViewById(R.id.usernameTV);
            description = itemView.findViewById(R.id.descriptionTextView);
            dateAdded = itemView.findViewById(R.id.dateTV);
            activityType = itemView.findViewById(R.id.activityTV);
            postImage = itemView.findViewById(R.id.postIV);
            profileImage = itemView.findViewById(R.id.profileIV);
            sendImage = itemView.findViewById(R.id.sendIV);
            commentBar = itemView.findViewById(R.id.commentET);
        }
    }
}
