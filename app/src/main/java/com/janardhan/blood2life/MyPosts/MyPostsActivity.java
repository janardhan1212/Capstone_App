package com.janardhan.blood2life.MyPosts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.janardhan.blood2life.CommentsActivity;
import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.pojos.Post;

import java.util.HashMap;
import java.util.Map;

public class MyPostsActivity extends AppCompatActivity implements MyPostsFragment.OnPostSelectedListener, View.OnClickListener {

    private DatabaseReference mDatabase;
    private SQLiteHandler db;
    HashMap<String, String> user_;
    private static final String TAG = "MyPost Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = new SQLiteHandler(getApplicationContext());
        user_ = db.getUserDetails();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPostComment(String postKey) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.POST_KEY_EXTRA, postKey);
        startActivity(intent);
    }

    @Override
    public void onPostShare(String postKey) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, postKey);

        startActivity(Intent.createChooser(share, "Share Feed"));
    }

    @Override
    public void onPostCall(Post postKey, String inPostKey, LinearLayout status_ll) {
        Log.e("post-cick", "Entered into post");
        final String userId = getUid();


    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

