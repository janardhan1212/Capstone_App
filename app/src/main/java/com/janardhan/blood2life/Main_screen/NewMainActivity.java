package com.janardhan.blood2life.Main_screen;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.janardhan.blood2life.BaseActivity;
import com.janardhan.blood2life.CommentsActivity;
import com.janardhan.blood2life.Helpers.DataProviderContract;
import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.Helpers.SessionManager;
import com.janardhan.blood2life.MyPosts.MyPostsActivity;
import com.janardhan.blood2life.PostDetailsActivity;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.Submit_post.submit_post;
import com.janardhan.blood2life.slides;

import java.util.HashMap;

public class NewMainActivity extends BaseActivity implements PostFragment.OnPostSelectedListener, View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "Main Activity";
    private static final int URL_LOADER = 0;
    private static final String[] PROJECTION =
            {
                    DataProviderContract._ID,
                    DataProviderContract.KEY_NAME
            };
    private SessionManager session;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SQLiteHandler db;
    private HashMap<String, String> user_;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private Animation fab_open;
    private Animation fab_close;
    private Animation rotate_forward;
    private Animation rotate_backward;
    private Boolean isFabOpen = false;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = new SQLiteHandler(getApplicationContext());
        user_ = db.getUserDetails();
        FirebaseMessaging.getInstance().subscribeToTopic(user_.get("state").replace(" ", "_"));
        FirebaseMessaging.getInstance().subscribeToTopic(user_.get("city").replace(" ", "_"));
        FirebaseMessaging.getInstance().subscribeToTopic(user_.get("uid"));


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        // Pushbots.sharedInstance().init(this);
        // session manager
        //user_ = db.getUserDetails();
        session = new SessionManager(getApplicationContext());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, submit_post.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, MyPostsActivity.class);
                startActivity(intent);

              //  Toast.makeText(NewMainActivity.this, "My Posts", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ma_screen_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logoutUser();
            return true;
        }


        return super.onOptionsItemSelected(item);
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
    public void onPostCall(String postKey) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(postKey));
        startActivity(intent);

    }

    @Override
    public void onPostClick(String postKey) {
        Intent intent = new Intent(NewMainActivity.this, PostDetailsActivity.class);
        intent.putExtra("post_key", postKey);
        startActivity(intent);
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:

                Log.d("Raj", "Fab 2");
                break;
        }
    }


    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        String city = user_.get("city");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(city);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), slides.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        /*
         * Takes action based on the ID of the Loader that's being created
         */
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,                                     // Context
                        DataProviderContract.PICTUREURL_TABLE_CONTENTURI,  // Table to query
                        PROJECTION,                                        // Projection to return
                        null,                                              // No selection clause
                        null,                                              // No selection arguments
                        null                                               // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        user_name = data.getColumnName(1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        user_name = null;
    }
}
