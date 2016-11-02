package com.janardhan.blood2life.adapter;

/**
 * Created by janardhanyerranagu on 1/17/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janardhan.blood2life.R;


/**
 * Created by kundan on 10/26/2015.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {


    //String [] name={"Androidwarriors","Stackoverflow","Developer Android","AndroidHive",
           // "Slidenerd","TheNewBoston","Truiton","HmkCode","JavaTpoint","Javapeper"};
    String[] name;
    Context context;
    LayoutInflater inflater;
    String user_id,email_id,user_name;
    public RecyclerAdapter(Context context, String user_id, String email_id, String user_name) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        name = context.getResources().getStringArray(R.array.states);
        this.user_name=user_name;
        this.user_id=user_id;
        this.email_id=email_id;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list, parent, false);


        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v,name,user_name,user_id,email_id);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {


        holder.tv1.setText(name[position]);


    }







    @Override
    public int getItemCount() {
        return name.length;
    }
}