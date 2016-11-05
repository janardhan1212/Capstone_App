package com.janardhan.blood2life.MyPosts;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.janardhan.blood2life.BaseFragment;
import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.pojos.Post;
import com.janardhan.blood2life.utils.FirebaseUtil;
import com.janardhan.blood2life.utils.p_MyCustomTextView_mbold;

import java.util.HashMap;
import java.util.Map;

import static com.janardhan.blood2life.utils.FirebaseUtil.getCurrentUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostsFragment extends BaseFragment {

    public static final String TAG = "PostsFragment";
    private static final String KEY_LAYOUT_POSITION = "layoutPosition";
    private int mRecyclerViewPosition = 0;
    private OnPostSelectedListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<MyPostViewHolder> mAdapter;
    private SQLiteHandler db;
    private HashMap<String, String> user_;
    private p_MyCustomTextView_mbold mempty_view;
    private DatabaseReference mDatabase;

    public MyPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_my_posts, container, false);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
        mempty_view = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.empty_view);
        db = new SQLiteHandler(getActivity());
        user_ = db.getUserDetails();
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        showProgressDialog();
        mRecyclerView.setLayoutManager(linearLayoutManager);

      /*  if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mRecyclerViewPosition = (int) savedInstanceState
                    .getSerializable(KEY_LAYOUT_POSITION);
            mRecyclerView.scrollToPosition(mRecyclerViewPosition);
            // TODO: RecyclerView only restores position properly for some tabs.
        }
        Log.d(TAG, "Restoring recycler view position (all): " + mRecyclerViewPosition);*/
        final Query allPostsQuery = FirebaseUtil.getMyPostsRef().child(getCurrentUserId());
        allPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    mAdapter = getFirebaseRecyclerAdapter(allPostsQuery);

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mempty_view.setVisibility(View.GONE);
                    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            // TODO: Refresh feed view.
                        }
                    });

                    mRecyclerView.setAdapter(mAdapter);
                    hideProgressDialog();
                } else {
                    hideProgressDialog();
                    mRecyclerView.setVisibility(View.GONE);
                    mempty_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                hideProgressDialog();
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        });


    }

    private FirebaseRecyclerAdapter<Post, MyPostViewHolder> getFirebaseRecyclerAdapter(Query query) {
        return new FirebaseRecyclerAdapter<Post, MyPostViewHolder>(
                Post.class, R.layout.my_request_feed, MyPostViewHolder.class, query) {
            @Override
            public void populateViewHolder(final MyPostViewHolder MyPostViewHolder,
                                           final Post post, final int position) {
                final DatabaseReference postRef = getRef(position);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                final String postKey = postRef.getKey();
                setupPost(MyPostViewHolder, post, position, postKey);

            }

            @Override
            public void onViewRecycled(MyPostViewHolder holder) {
                super.onViewRecycled(holder);
//                FirebaseUtil.getLikesRef().child(holder.mPostKey).removeEventListener(holder.mLikeListener);
            }
        };
    }

    private void setupPost(final MyPostViewHolder MyPostViewHolder, final Post post, final int position, final String inPostKey) {
        final Map<String, Object> postValues = post.toMap();
        String loc = postValues.get("city") + "," + postValues.get("state") + "," + postValues.get("hospital_address");
        MyPostViewHolder.setText(postValues.get("name").toString(), loc, postValues.get("phone_number").toString(), postValues.get("blood_group").toString(), "hi",postValues.get("received").toString());
        // MyPostViewHolder.setTimestamp(DateUtils.getRelativeTimeSpanString((long) postValues.get("time_stamp")).toString());
        final String postKey;
        final String share_text = "I need " + postValues.get("blood_group") + "blood group urgently at " + loc + "," + "/n ~ " + postValues.get("name") + "," + postValues.get("phone_number").toString();
        if (mAdapter instanceof FirebaseRecyclerAdapter) {
            postKey = ((FirebaseRecyclerAdapter) mAdapter).getRef(position).getKey();
        } else {
            postKey = inPostKey;
        }


        MyPostViewHolder.setTimestamp(DateUtils.getRelativeTimeSpanString(
                Long.parseLong(postValues.get("time_stamp").toString())).toString());
        MyPostViewHolder.setIcon(postValues.get("profile_pic").toString(), postValues.get("uid").toString());
        MyPostViewHolder.setPostClickListener(new MyPostViewHolder.PostClickListener()
        {
            @Override
            public void showComments() {
                Log.d(TAG, "Comment position: " + position);
                mListener.onPostComment(postKey);
            }

            @Override
            public void share_post() {
                Log.d(TAG, "Share position: " + position);
                mListener.onPostShare(share_text);
            }

            @Override
            public void submit_status(LinearLayout status_ll) {
                Log.d(TAG, "Call position: " + position);
                boolean s = writeNewPost(getUid(), post, inPostKey);
                MyPostViewHolder.setBg();



            }

        });
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    private boolean writeNewPost(String userId, Post post, String inPostKey) {
        final boolean[] stat = {false};
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("received", "T");

        mDatabase.child("user-posts").child(userId).child(inPostKey).updateChildren(taskMap);
        mDatabase.child("active_posts").child(inPostKey).removeValue();


        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/inactive_posts/" + inPostKey, postValues);
        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User post updated.");
                            stat[0] = true;
                        }
                    }
                });
        return stat[0];
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostSelectedListener) {
            mListener = (OnPostSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostSelectedListener");
        }
    }
    // Method to share either text or URL.

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPostSelectedListener {
        void onPostComment(String postKey);

        void onPostShare(String postKey);

        void onPostCall(Post postKey, String inPostKey, LinearLayout status_ll);
    }
}
