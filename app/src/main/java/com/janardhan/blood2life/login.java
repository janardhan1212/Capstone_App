package com.janardhan.blood2life;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.Helpers.SessionManager;
import com.janardhan.blood2life.Main_screen.NewMainActivity;
import com.janardhan.blood2life.pojos.get_users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.EditText;

public class login extends AppCompatActivity {


    private RelativeLayout main_content;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog PD;
    private com.rey.material.widget.ProgressView mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.activity_login);
        //Firebase.setAndroidContext(this);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        // Send data to Parse.com for verification

        mProgressView = (com.rey.material.widget.ProgressView) findViewById(R.id.login_progress);
        main_content = (RelativeLayout) findViewById(R.id.main_rl);
        final EditText phno = (EditText) findViewById(R.id.phono2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sub_fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phnotx = phno.getText().toString();
                if (phnotx.length()>0)
                {
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    main_content.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.VISIBLE);
                    mProgressView.start();
                    //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                   // String url = "https://blood-to-life.firebaseio.com/user/user";
                   // Firebase ref = new Firebase(url);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("user");
                    // Firebase ref = new Firebase("https://blood-to-life.firebaseio.com/user");
                    DatabaseReference ref = myRef.child("user");
                    Query queryRef2 = ref.orderByChild("phone_number").startAt(phnotx).endAt(phnotx);
                    queryRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0)
                            {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    get_users post = userSnapshot.getValue(get_users.class);
                                    session.setLogin(true);
                                    Intent intent = new Intent(login.this, NewMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {
                                main_content.setVisibility(View.VISIBLE);
                                mProgressView.setVisibility(View.GONE);
                                mProgressView.stop();
                                Toast.makeText(getApplicationContext(),
                                        R.string.no_nt_exit,
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.enter_phone,
                            Toast.LENGTH_LONG).show();
                }
                showProgress(false);

            }
        });
        showProgress(false);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            main_content.setVisibility(show ? View.GONE : View.VISIBLE);
            main_content.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    main_content.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            main_content.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
