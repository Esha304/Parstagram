package com.example.parstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parstagram.R;
import com.example.parstagram.Comment;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private static final String TAG = "CommentsAdapter";
    private final Context context;
    private final List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView showAuthor;
        TextView showComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showAuthor = itemView.findViewById(R.id.showAuthor);
            showComment = itemView.findViewById(R.id.showComment);
        }

        public void bind(Comment comment) {
            showAuthor.setText(comment.getAuthor().getUsername());
            showComment.setText(comment.getBody());
        }
    }
}

