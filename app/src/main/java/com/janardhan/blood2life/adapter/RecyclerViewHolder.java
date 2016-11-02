package com.janardhan.blood2life.adapter;

/**
 * Created by janardhanyerranagu on 1/17/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.janardhan.blood2life.R;
import com.janardhan.blood2life.get_cities;


/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    TextView tv1;
    String[] names;

    public RecyclerViewHolder(final View itemView, final String[] names, final String user_name, final String user_id, final String email_id) {
        super(itemView);
        final Activity activity = (Activity) itemView.getContext();

        tv1 = (TextView) itemView.findViewById(R.id.list_title);
        this.names = names;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = getPosition();
                Intent i = new Intent(v.getContext(), get_cities.class);
                i.putExtra("state", names[pos]);
                i.putExtra("user_id",user_id);
                i.putExtra("email_id",email_id);
                i.putExtra("user_name",user_name);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                Toast.makeText(v.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
