package com.janardhan.blood2life;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.janardhan.blood2life.pojos.Post;
import com.janardhan.blood2life.utils.FirebaseUtil;
import com.janardhan.blood2life.utils.GlideUtil;
import com.janardhan.blood2life.utils.p_MyCustomTextView_mbold;
import com.janardhan.blood2life.utils.p_MyCustomTextView_regular;

import java.util.Map;

import static com.janardhan.blood2life.R.id.phno;

public class PostDetailsActivity extends AppCompatActivity {
    public static final String POST_KEY_EXTRA = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        final p_MyCustomTextView_mbold mAuthor = (p_MyCustomTextView_mbold) findViewById(R.id.name);
        final p_MyCustomTextView_regular timestamp = (p_MyCustomTextView_regular) findViewById(R.id.timestamp);
        final TextView req_bld = (TextView) findViewById(R.id.bld_grp);
        p_MyCustomTextView_mbold status = (p_MyCustomTextView_mbold) findViewById(R.id.status);
        final p_MyCustomTextView_regular mpho_no = (p_MyCustomTextView_regular) findViewById(phno);
        final p_MyCustomTextView_regular mLocation = (p_MyCustomTextView_regular) findViewById(R.id.loc);
        final p_MyCustomTextView_regular mMsg = (p_MyCustomTextView_regular) findViewById(R.id.msg);
        String postKey = getIntent().getStringExtra(POST_KEY_EXTRA);
        if (postKey == null) {
            onBackPressed();
        }
        final DatabaseReference postRef = FirebaseUtil.getPostsRef().child(postKey);

        postRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Post post = dataSnapshot.getValue(Post.class);
                        //  Post post= (Post) dataSnapshot.getValue();
                        String name = post.name;
                        Log.d("name", name);
                        final Map<String, Object> postValues = post.toMap();
                        String loc = postValues.get("city") + "," + postValues.get("state") + "," + postValues.get("hospital_address");
                        mLocation.setText(loc);
                        mAuthor.setText(name);
                        mpho_no.setText(postValues.get("phone_number").toString());
                        req_bld.setText(postValues.get("blood_group").toString());
                        timestamp.setText(DateUtils.getRelativeTimeSpanString((long) postValues.get("time_stamp")).toString());
                        GlideUtil.loadProfileIcon(postValues.get("profile_pic").toString(), profilePic);
                        // mMsg.setText(msg);
                        //   postViewHolder.setText(postValues.get("name").toString(), loc, postValues.get("phone_number").toString(), postValues.get("blood_group").toString(), "hi");
                        // postViewHolder.setTimestamp(DateUtils.getRelativeTimeSpanString((long) postValues.get("time_stamp")).toString());
                        final String postKey;
                        final String share_text = "I need " + postValues.get("blood_group") + "blood group urgently at " + loc + "," + "/n ~ " + postValues.get("name") + "," + postValues.get("phone_number").toString();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.comments_fragment, CommentsFragment.newInstance(postKey))
                .commit();
    }

}
