package com.janardhan.blood2life.Main_screen;

/**
 * Created by janardhanyerranagu on 1/17/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.janardhan.blood2life.R;
import com.janardhan.blood2life.pojos.get_users;
import com.janardhan.blood2life.utils.p_MyCustomTextView;

import java.util.ArrayList;


/**
 * Created by janardhan on 10/26/2015.
 */
public class RecyclerAdapter_find_r extends RecyclerView.Adapter<RecyclerAdapter_find_r.CustomViewHolder> implements View.OnClickListener
{

    private final Context mContext;
    ArrayList<get_users> users= new ArrayList<>();


    public RecyclerAdapter_find_r(Context context, ArrayList<get_users> users) {

        this.mContext = context;
        this.users = users;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_f, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final get_users user = users.get(i);
        String name=user.getFullName().toLowerCase();
        String fname= name.substring(0, 1).toUpperCase() + name.substring(1);
        customViewHolder.a_name.setText(fname);
        customViewHolder.call_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+user.getphone_number()));
                mContext.startActivity(intent);
            }
        });

        customViewHolder.message_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body","I need blood urgently.Can you help me?");
                mContext.startActivity(sendIntent);
            }
        });

        customViewHolder.share_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Name: "+user.getFullName()+", PhoneNumber: "+user.getphone_number();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.share_using)));

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

    @Override
    public void onClick(View v) {

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected p_MyCustomTextView a_name;
        protected ImageView call_option;
        protected ImageView message_option;
        protected ImageView share_option;


        public CustomViewHolder(View view) {
            super(view);
            this.a_name = (p_MyCustomTextView) view.findViewById(R.id.name);
            this.call_option = (ImageView) view.findViewById(R.id.call);
            this.message_option = (ImageView) view.findViewById(R.id.msg);
            this.share_option = (ImageView) view.findViewById(R.id.share);


        }
    }


}