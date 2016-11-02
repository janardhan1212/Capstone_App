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
 * Created by janardhan on 10/26/2015.
 */
public class RecyclerAdapter_cities extends  RecyclerView.Adapter<RecyclerViewHolder_cities> {


    //String [] name={"Androidwarriors","Stackoverflow","Developer Android","AndroidHive",
           // "Slidenerd","TheNewBoston","Truiton","HmkCode","JavaTpoint","Javapeper"};
    String[] name;
    Context context;
    String state;
    LayoutInflater inflater;
    private String user_id,email_id,user_name;
    public RecyclerAdapter_cities(Context context, String[] name, String state, String user_id, String email_id, String user_name) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.name = name;
        this.state=state;
        this.user_name=user_name;
        this.email_id=email_id;
        this.user_id=user_id;
    }
    @Override
    public RecyclerViewHolder_cities onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list, parent, false);


        RecyclerViewHolder_cities viewHolder=new RecyclerViewHolder_cities(v,name,state,user_id,email_id,user_name);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder_cities holder, int position) {


        holder.tv1.setText(name[position]);


    }







    @Override
    public int getItemCount() {
        return name.length;
    }
}