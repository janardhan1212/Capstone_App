package com.janardhan.blood2life;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.janardhan.blood2life.Helpers.DataProviderContract;
import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.Helpers.SessionManager;
import com.janardhan.blood2life.Main_screen.NewMainActivity;
import com.janardhan.blood2life.pojos.User_s;
import com.janardhan.blood2life.utils.CirclePageIndicator;
import com.janardhan.blood2life.utils.ColorShades;

import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_CITY;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_EMAIL;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_GROUP;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_NAME;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_PHNO;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_STATE;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_TOKEN;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_UID;
import static com.janardhan.blood2life.Helpers.DataProviderContract.KEY_pic_url;


public class slides extends AppCompatActivity {
    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private static final String TAG = "FacebookLogin";
    private Context mContext;
    private boolean isSliderAnimation = false;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (session.isLoggedIn()) {

            // User is already logged in. Take him to main activity
            Intent intent = new Intent(slides.this, NewMainActivity.class);
            startActivity(intent);
            finish();

        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
     //   p_MyCustomTextView_bold logintx = (p_MyCustomTextView_bold) findViewById(R.id.logintx);
        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        viewPager.setPageTransformer(true, new CustomPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.landing_backgrond);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);


                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());

            }

            public void onPageSelected(int position) {

            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        final ViewGroup mGroup = (ViewGroup) findViewById(R.id.group);
        mDatabase = FirebaseDatabase.getInstance().getReference();
     //   findViewById(R.id.button_facebook_signout).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });

    }

    /**
     * Sets the alpha for the view. The alpha will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view  - view to which alpha to be applied.
     * @param alpha - alpha value.
     */
    private void setAlpha(View view, float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets the translationX for the view. The translation value will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view         - view to which alpha to be applied.
     * @param translationX - translationX value.
     */
    private void setTranslationX(View view, float translationX) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setTranslationX(translationX);
        }
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_start_add_listener]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(slides.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }//else {
                         //   updateUI(user);
                       // }


                        // [START_EXCLUDE]
                      //  hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END on_stop_remove_listener]

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(final FirebaseUser user) {

        if (user != null) {
//            mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
           // mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            mDatabase.child("mobile_users").child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            // dataSnapshot.

                            if(dataSnapshot.getValue()!=null)
                            {
                                Log.w(TAG, "old_user");
                                User_s user_p = dataSnapshot.getValue(User_s.class);
                                ContentValues values = new ContentValues();
                                values.put(KEY_UID, user.getUid());
                                values.put(KEY_NAME, user_p.getFullName());
                                values.put(KEY_EMAIL, user_p.getEmail_id()); // Email
                                values.put(KEY_PHNO, user_p.getphone_number()); // Name
                                values.put(KEY_GROUP, user_p.getGroup()); // Email
                                values.put(KEY_CITY, user_p.getCity());
                                values.put(KEY_STATE, user_p.getState()); // Email
                                values.put(KEY_pic_url, user_p.getProfile_pic());
                                values.put(KEY_TOKEN, user_p.getRefreshedToken());

                                getContentResolver().insert(DataProviderContract.CONTENT_URI, values);

                                db.addUser(user.getUid(),user_p.getFullName(), user_p.getEmail_id(), user_p.getphone_number(), user_p.getGroup(), user_p.getCity(),user_p.getState(),user_p.getRefreshedToken(),user_p.getProfile_pic());
                                session.setLogin(true);
                                Intent i = new Intent(slides.this, NewMainActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }
                            else {
                                Log.w(TAG, "new_user");
                                Intent i = new Intent(slides.this, get_states.class);
                                i.putExtra("user_id",user.getUid());
                                i.putExtra("email_id",user.getEmail());
                                i.putExtra("user_name",user.getDisplayName());
                               // i.putExtra("sex",user.)
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                //writeNewUser(user.getUid(),user.getEmail());
                            }
                            hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            hideProgressDialog();
                        }
                    });
            // writeNewUser(user.getUid(),user.getEmail());
         //   findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
          //  findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
         //   mStatusTextView.setText(R.string.signed_out);
         //   mDetailTextView.setText(null);

           // findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
            //findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
    }
    // [END auth_with_facebook]

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_facebook_signout:
                signOut();
                break;
        }
    }*/
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];


            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);


            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);


            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }


}
