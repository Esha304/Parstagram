package com.example.parstagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.CommentActivity;
import com.example.parstagram.PostDetailsActivity;
import com.example.parstagram.fragments.ProfileFragment;
import com.example.parstagram.Post;
import com.example.parstagram.User;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.parceler.Parcels;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";
    private final Context context;
    private final List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
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

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvUsername;
        private TextView tvLikes;
        private ImageView ivImage;
        private TextView tvCaption;
        private ImageButton ibLikes;
        private ImageButton ibComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLikes = itemView.findViewById(R.id.tvPostLikes);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ibLikes = itemView.findViewById(R.id.ibPostLikes);
            ibComments = itemView.findViewById(R.id.ibComment);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Post post) {
            tvCaption.setText(post.getCaption());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile profile = post.getUser().getProfile();
            if (profile != null) { Glide.with(context)
                    .load(profile.getUrl())
                    .into(ivProfile); }
            tvLikes.setText(post.likeCountDisplayText());

            if (post.getLikedBy().contains(ParseUser.getCurrentUser().getObjectId())) {
                ibLikes.setColorFilter(Color.RED);
            } else { ibLikes.setColorFilter(Color.DKGRAY); }

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to profile fragment
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment profileFragment = new ProfileFragment(post.getParseUser(Post.KEY_USER));
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, profileFragment).addToBackStack(null).commit();
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment profileFragment = new ProfileFragment(post.getParseUser(Post.KEY_USER));
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, profileFragment).addToBackStack(null).commit();
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetailsActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            ibLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> likedBy = post.getLikedBy();
                    if (!likedBy.contains(ParseUser.getCurrentUser().getObjectId())) {
                        likedBy.add(ParseUser.getCurrentUser().getObjectId());
                        post.setLikedBy(likedBy);
                        ibLikes.setColorFilter(Color.RED);
                    }
                    else {
                        likedBy.remove(ParseUser.getCurrentUser().getObjectId());
                        post.setLikedBy(likedBy);
                        ibLikes.setColorFilter(Color.DKGRAY);
                    }
                    post.saveInBackground();
                    tvLikes.setText(post.likeCountDisplayText());
                }
            });

            ibComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("post_to_comment_on", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }
    }
}
