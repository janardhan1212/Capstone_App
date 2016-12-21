/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.janardhan.blood2life.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.utils.GlideUtil;
import com.janardhan.blood2life.utils.p_MyCustomTextView_mbold;
import com.janardhan.blood2life.utils.p_MyCustomTextView_regular;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private static final int POST_TEXT_MAX_LINES = 6;
    private final View mView;
    public DatabaseReference mPostRef;
    public ValueEventListener mPostListener;
    p_MyCustomTextView_regular mpho_no;
    p_MyCustomTextView_regular mMsg;
    private PostClickListener mListener;
    private ImageView mPhotoView;
    private p_MyCustomTextView_mbold mAuthor;
    private p_MyCustomTextView_regular mLocation;
    private TextView req_bld;
    private p_MyCustomTextView_regular mTimestampView;


    public PostViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mPhotoView = (ImageView) itemView.findViewById(R.id.profilePic);

        mAuthor = (p_MyCustomTextView_mbold) itemView.findViewById(R.id.name);
        mLocation = (p_MyCustomTextView_regular) itemView.findViewById(R.id.loc);
        mTimestampView = (p_MyCustomTextView_regular) itemView.findViewById(R.id.timestamp);
        mpho_no = (p_MyCustomTextView_regular) itemView.findViewById(R.id.phno);
        req_bld = (TextView) itemView.findViewById(R.id.bld_grp);
        mMsg = (p_MyCustomTextView_regular) itemView.findViewById(R.id.msg);

        itemView.findViewById(R.id.comment_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showComments();
            }
        });
        itemView.findViewById(R.id.share_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.share_post();
            }
        });
        itemView.findViewById(R.id.call_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.call_post();

            }
        });
        itemView.findViewById(R.id.ll_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.post_click();

            }
        });
    }


    public void setIcon(String url, final String authorId) {


        GlideUtil.loadProfileIcon(url, mPhotoView);
    }


    public void setText(final String name, String loc, String phno, String bld_grp, String msg) {
        mLocation.setText(loc);
        mAuthor.setText(name);
        mpho_no.setText(phno);
        req_bld.setText(bld_grp);
        mMsg.setText(msg);

    }

    public void setTimestamp(String timestamp) {

      /*  try {
            long created = Long.parseLong(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm");
            String dateString = formatter.format(new Date(created * 1000L));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date past = format.parse(dateString);
            Date now = new Date();
            String sec=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago";
            mTimestampView.setText(sec);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        mTimestampView.setText(timestamp);
    }


    public void setPostClickListener(PostClickListener listener) {
        mListener = listener;
        Log.d("Clicked", "I hurts me!!!!!!");
    }


    public interface PostClickListener {
        void showComments();

        void share_post();

        void call_post();

        void post_click();
    }
}