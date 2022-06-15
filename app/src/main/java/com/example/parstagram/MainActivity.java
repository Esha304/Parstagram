package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 396;
    EditText etcaption;
    Button btncapture;
    Button btnSubmit;
    ImageView ivImage;
    Button logoutbtn;
    private File photoFile;
    public String photoFileName = "photo.jpg"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etcaption = findViewById(R.id.etcaption);
        btncapture = findViewById(R.id.btnCapture);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivImage = findViewById(R.id.ivImage);
        logoutbtn = (Button) findViewById(R.id.menulogoutbtn);

        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        queryPosts();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etcaption.getText().toString();
                if(caption.isEmpty()){
                    Toast.makeText(MainActivity.this, "Caption cannot be empty!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile==null || ivImage.getDrawable()==null){
                    Toast.makeText(MainActivity.this, "There is no image!!  ", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(caption, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        //create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);
        //wrap File object into a content provider
        //required for API >= 24
        //see https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        //if you call startActivityForResult() using an intent that no app can handle, your app will crash.
        //so as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            //start the image capture intent to take photo
            //noinspection deprecation
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //RESIZE BITMAP, see section below
                //load the taken image into a preview
                ivImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Error taking picture!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        //get safe storage directory for photos
        //use `getExternalFilesDir` on Context to access package-specific directories.
        //this way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        //create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create directory");
           }
        //return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    private void savePost(String caption, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setCaption(caption);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e );
                    Toast.makeText(MainActivity.this , "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, " Successfully saved post");
                etcaption.setText("");
                ivImage.setImageResource(0);
            }
        });
    }


    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting  posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getCaption() + ", Image: " + post.getImage()  + ", username: " + post.getUser().getUsername());
                }
                //allPosts.addAll(posts);
                //swipeContainer.setRefreshing(false);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    public void onLogoutButton(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}