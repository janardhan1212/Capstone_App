package com.janardhan.blood2life.Submit_post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.fcm.NotifConfig;
import com.janardhan.blood2life.pojos.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class submit_post extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String state;
    private String sel_state;
    private String f_st, f_ct;
    private String[] cties_s;
    private String[] arraySpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] states;
    private String loc;
    private Spinner loc_spiner;
    private String phno;
    private SQLiteHandler db;
    HashMap<String, String> user_;
    private String citiy2;

    private static final String TAG = "Submit Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = new SQLiteHandler(getApplicationContext());
        user_ = db.getUserDetails();
        citiy2 = user_.get("city");
        phno = user_.get("phno");
        state = user_.get("state");
        arraySpinner = new String[]{
                "My Location" + "( " + citiy2 + " )", "Change Location"
        };
        final ProgressView sin_p_cir = (ProgressView) findViewById(R.id.sin_p_cir);
        states = getResources().getStringArray(R.array.states);
        FloatingActionButton post = (FloatingActionButton) findViewById(R.id.fab_post);
        final Spinner bld_spinner = (Spinner) findViewById(R.id.bld_spiner);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText phno = (EditText) findViewById(R.id.phno);
        final EditText adress = (EditText) findViewById(R.id.adress);
        loc_spiner = (Spinner) findViewById(R.id.loc_spiner);
        name.setText(user_.get("name"));
        phno.setText(user_.get("phno"));
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loc_spiner.setAdapter(spinnerAdapter);
        loc = "My Location" + "( " + citiy2 + " )";
        spinnerAdapter.add(loc);
        spinnerAdapter.add("Change Location");
        spinnerAdapter.notifyDataSetChanged();
        loc_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String item = spinnerAdapter.getItem(position);
                if (item.equals("Change Location")) {
                    Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            String state = getSelectedValue().toString();
                            sel_state = state;
                            set_city(state);
                            //  Toast.makeText(getActivity(), "You have selected " + getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                            super.onPositiveActionClicked(fragment);
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            Toast.makeText(submit_post.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            super.onNegativeActionClicked(fragment);
                        }
                    };

                    ((SimpleDialog.Builder) builder).items(states, 0)
                            .title("Select State")
                            .positiveAction("OK")
                            .negativeAction("CANCEL");
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getSupportFragmentManager(), null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sin_p_cir.setVisibility(View.VISIBLE);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                int i = loc_spiner.getSelectedItemPosition();
                String sel_stat, sel_city;
                if (arraySpinner.length <= 2) {
                    sel_stat = state;
                    sel_city = citiy2;
                } else {
                    sel_stat = sel_state;
                    sel_city = f_ct;
                }
                f_st = sel_stat;
                f_ct = sel_city;
                Log.e("post-cick", "Entered into post" + sel_stat);
                final String userId = getUid();
                writeNewPost(userId, user_.get("name"), phno.getText().toString(), bld_spinner.getSelectedItem().toString(), f_ct, f_st, adress.getText().toString());

               /* Query queryRef = mDatabase.child("users").orderByChild("state").startAt(sel_stat).endAt(sel_stat);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot snapshot)
                    {

                        ArrayList<get_users> new_list = new ArrayList<>();
                        Log.e("response", snapshot.toString());

                        if (snapshot.getValue() != null)
                        {
                            if (snapshot.getChildrenCount() > 0)
                            {
                                ArrayList<get_users> users_arrlist = new ArrayList<>();
                                final String cities = null;
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    get_users user_list = postSnapshot.getValue(get_users.class);
                                    users_arrlist.add(user_list);
                                }
                                //ArrayList<String> ph_no = new ArrayList<>();
                                for (int i = 0; i < users_arrlist.size(); i++) {
                                    get_users use = users_arrlist.get(i);
                                    String from = use.getFrom();

                                    if (!from.equals("website")) {
                                        new_list.add(use);
                                    }
                                }
                                Log.e("count",String.valueOf(new_list.size()));
                                final String userId = getUid();
                                Query queryRef = mDatabase.child("users").child(userId);
                                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        Log.e("Entered","ok");
                                        // Get user value
                                        User user = dataSnapshot.getValue(User.class);

                                        // [START_EXCLUDE]
                                        if (user == null) {
                                            // User is null, error out
                                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                                            Toast.makeText(submit_post.this,
                                                    "Error: could not fetch user.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Write new post
                                            writeNewPost(userId,name.getText().toString(),phno.getText().toString(),bld_spinner.getSelectedItem().toString(),cities,adress.getText().toString());
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                    }
                                });

                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(submit_post.this,
                                "Something Went Worng",
                                Toast.LENGTH_LONG).show();

                    }


                });*/

                // submit();
            }


        });
        sin_p_cir.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String loadJSONFromAsset(String state) {
        String json = null;
        try {
            InputStream is = getAssets().open(state + ".json");
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String uname, String phno, String selectedItem, String l_city, String l_state, String address) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("active_posts").push().getKey();
        Long timeStamp = new Long(new Timestamp(new Date().getTime()).getTime());

        Post post = new Post(userId, uname, phno, selectedItem, l_city, l_state, address, "T", "F",timeStamp.toString(),user_.get("profile_pic_url"));

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/active_posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User post updated.");
                            FirebaseMessaging fm = FirebaseMessaging.getInstance();

                            fm.send(new RemoteMessage.Builder(NotifConfig.SENDERID + "@gcm.googleapis.com")
                                   // .setMessageId(Integer.toString(msgId.incrementAndGet()))
                                    .addData("my_message", "Hello World")
                                    .addData("my_action","SAY_HELLO")
                                   // .addData("my_action","SAY_HELLO")
                                    .build());
                            onBackPressed();
                        }
                    }
                });
    }

    public void set_city(String state) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(state));
            JSONArray m_jArry = obj.getJSONArray("results");
            ArrayList<String> cities = new ArrayList<String>();
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("name"));
                String city = jo_inside.getString("name");

                cities.add(city);
            }
            cties_s = new String[cities.size()];
            cties_s = cities.toArray(cties_s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                String citiy_changed = getSelectedValue().toString();
                f_ct = citiy_changed;
                arraySpinner = new String[]{

                        citiy_changed, "My Location" + "( " + citiy2 + " )", "Change Location"
                };

                spinnerAdapter.clear();
                spinnerAdapter.add(citiy_changed);
                spinnerAdapter.add(loc);
                spinnerAdapter.add("Change Location");
                spinnerAdapter.notifyDataSetChanged();
                loc_spiner.setAdapter(spinnerAdapter);
                //set_ui(citiy2);
                // Toast.makeText(getActivity(), "You have selected " + getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                Toast.makeText(submit_post.this, "Cancelled", Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).items(cties_s, 0)
                .title("Select City in " + state)
                .positiveAction("OK")
                .negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

}
