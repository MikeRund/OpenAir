package com.mastersproject.openair.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerViewAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);
        String imageUrl;

        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
        holder.name.setText(post.getUsername());
        imageUrl = post.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                post.getTimestamp().getSeconds()*1000);
        holder.dateAdded.setText(timeAgo);

        // Using Glide Library to Display the images
        Glide.with(context).load(imageUrl) //.placeholder()
                .fitCenter().into(holder.image);

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
        public TextView title, description, dateAdded, name;
        public ImageView image, shareBTN;
        String userID;
        String username;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // These Widgets belong to Journal_row.xml
            title = itemView.findViewById(R.id.journal_row_title_list);
            description = itemView.findViewById(R.id.journal_row_description_list);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);
            image = itemView.findViewById(R.id.journal_row_image_list);
            name = itemView.findViewById(R.id.journal_row_username);
            shareBTN = itemView.findViewById(R.id.journal_row_shareBTN);

        }
    }
}
