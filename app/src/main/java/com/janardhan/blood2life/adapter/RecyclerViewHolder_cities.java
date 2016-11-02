package com.janardhan.blood2life.adapter;

/**
 * Created by janardhanyerranagu on 1/17/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.janardhan.blood2life.R;
import com.janardhan.blood2life.form_submit;


/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerViewHolder_cities extends RecyclerView.ViewHolder {


    TextView tv1;
    String[] names;
    String state;
    private String user_id,email_id,user_name;

    public RecyclerViewHolder_cities(final View itemView, final String[] names, final String state, final String user_id, final String email_id, final String user_name) {
        super(itemView);
        final Activity activity = (Activity) itemView.getContext();

        tv1 = (TextView) itemView.findViewById(R.id.list_title);
        this.names = names;
        this.state = state;
        this.user_id = user_id;
        this.email_id = email_id;
        this.user_name = user_name;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = getPosition();
                Intent i = new Intent(v.getContext(), form_submit.class);
                i.putExtra("state", state);
                i.putExtra("city", names[pos]);
                i.putExtra("user_id",user_id);
                i.putExtra("email_id",email_id);
                i.putExtra("user_name",user_name);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                //Toast.makeText(v.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(v.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
