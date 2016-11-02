package com.janardhan.blood2life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.janardhan.blood2life.adapter.RecyclerAdapter;
import com.janardhan.blood2life.utils.SimpleDividerItemDecoration;

public class get_states extends AppCompatActivity {
    RecyclerView recyclerView;
    private String user_id,email_id,user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_states);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select State");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            user_id=bundle.getString("user_id");
            email_id=bundle.getString("email_id");
            user_name=bundle.getString("user_name");
            //  DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("Lmems_list");
            //Lmems_list = getIntent().getParcelableArrayListExtra("Lmems_list");
            //dw.getmems_list();
        }
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);


        RecyclerAdapter adapter=new RecyclerAdapter(this,user_id,email_id,user_name);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }
}
