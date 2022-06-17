package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.parstagram.R;
import com.example.parstagram.Comment;
import com.example.parstagram.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.parceler.Parcels;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private EditText writeComment;
    private Button btnComment;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post_to_comment_on"));

        writeComment = findViewById(R.id.writeComment);
        btnComment = findViewById(R.id.btnComment);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setAuthor(ParseUser.getCurrentUser());
                comment.setBody(writeComment.getText().toString());
                comment.setPost(post);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving");
                            //Toast.makeText(CommentActivity.this, "Error while commenting!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Saved comment");

                        Intent i = new Intent();
                        i.putExtra("new_comment", comment);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
            }
        });
    }
}