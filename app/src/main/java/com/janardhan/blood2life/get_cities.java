package com.janardhan.blood2life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.janardhan.blood2life.adapter.RecyclerAdapter_cities;
import com.janardhan.blood2life.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class get_cities extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] cities_a;
    String state;
    private String user_id,email_id,user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cities);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.select_city);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            state=bundle.getString("state");
            user_id=bundle.getString("user_id");
            email_id=bundle.getString("email_id");
            user_name=bundle.getString("user_name");
            //  DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("Lmems_list");
            //Lmems_list = getIntent().getParcelableArrayListExtra("Lmems_list");
            //dw.getmems_list();
        }
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("results");
            ArrayList<String> cities=new ArrayList<String>();
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("name"));
                String city = jo_inside.getString("name");

                cities.add(city);
            }
            String[] cties_s = new String[cities.size()];
            cties_s = cities.toArray(cties_s);
            RecyclerAdapter_cities adapter=new RecyclerAdapter_cities(this,cties_s,state,user_id,email_id,user_name);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open(state+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}

