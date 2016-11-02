package com.janardhan.blood2life.Main_screen;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.R;
import com.janardhan.blood2life.pojos.get_users;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Find_fragement extends Fragment {


    private String[] cties_s;
    private SQLiteHandler db;
    private String city;

    private RecyclerView users_rv;
    private RecyclerAdapter_find_r adapter;
    private TextView nodata;
    private String b_group;
    private String citiy2;

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private Button location;
    private Button blood_group;
    private ProgressView loding;
    private String TAG="this_activity";

    public Find_fragement() {
        // Required empty public constructor
    }

    String[] states;
    String[] arr_bgroup = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ThemeManager.init(getActivity(), 1, 0, null);
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_find, container, false);
        nodata = (TextView) rootview.findViewById(R.id.nodata);
        users_rv = (RecyclerView) rootview.findViewById(R.id.users_rv);
         loding=(ProgressView) rootview.findViewById(R.id.sin_p_cir);
         location = (Button) rootview.findViewById(R.id.location);
         blood_group = (Button) rootview.findViewById(R.id.blood_group);
        states = getActivity().getResources().getStringArray(R.array.states);
        db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();
        citiy2 = user.get("city");
        b_group = user.get("bgroup");
        String str = b_group.replaceAll("pos", "+").replaceAll("neg", "-");
        location.setText(citiy2);
        blood_group.setText(str);

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference();
              ;//orderByChild("city").equalTo(citiy2);


        new setting_ui().execute(citiy2,b_group);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        String state = getSelectedValue().toString();
                        set_city(state);
                        //  Toast.makeText(getActivity(), "You have selected " + getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).items(states, 0)
                        .title("Select State")
                        .positiveAction("OK")
                        .negativeAction("CANCEL");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
            }
        });
        blood_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users_rv.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                nodata.setText("Loading...");
                Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight)
                {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        String b_groupp = getSelectedValue().toString();
                        String str = b_groupp.replaceAll("\\+", "pos").replaceAll("\\-", "neg");
                        blood_group.setText(b_groupp);
                        new setting_ui().execute(citiy2, str);
                        //  Toast.makeText(getActivity(), "You have selected " + getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).items(arr_bgroup, 0)
                        .title("Select Blood Group")
                        .positiveAction("OK")
                        .negativeAction("CANCEL");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
            }
        });
        getProfileInformation();
        return rootview;
    }
    public void getProfileInformation() {
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Log.e("profilePicUrl",profilePicUrl);
                                    // set profile image to imageview using Picasso or Native methods
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();}
        private  class setting_ui extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (nodata.getVisibility() == View.GONE) {
                users_rv.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                nodata.setText("Loading...");
            }

          //  users_rv.setVisibility(View.GONE);
           // nodata.setVisibility(View.VISIBLE);
           // nodata.setText("Loading...");
          //  loding.setVisibility(View.VISIBLE);
           // loding.start();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String s_city=params[0];
            final String s_bgroup=params[1];
//            Log.d("city",s_city);
            //Log.d("bgroup",s_bgroup);
            Log.d("Selected",s_city+","+s_bgroup);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
           // Query recentPostsQuery = mPostReference.child("users")
                  //  .limitToFirst(100);
           Query queryRef = mPostReference.child("users").orderByChild("city").startAt(s_city).endAt(s_city);//)equalTo(s_city);
            Log.e("internal started","yes");


            queryRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("response",snapshot.toString());

                    if(snapshot.getValue()!=null) {
                        if (snapshot.getChildrenCount() > 0) {
                            ArrayList<get_users> users_arrlist = new ArrayList<>();
                            String cities = null;
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                get_users user_list = postSnapshot.getValue(get_users.class);

                                users_arrlist.add(user_list);
                            }
                            if (nodata.getVisibility() == View.VISIBLE) {
                                users_rv.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                            }
                            ArrayList<get_users> new_list = new ArrayList<get_users>();
                            ArrayList<String> ph_no = new ArrayList<>();
                            for (int i = 0; i < users_arrlist.size(); i++) {
                                get_users use = users_arrlist.get(i);
                                String pno = use.getphone_number();


                                String bgrp = use.getGroup();

                                if (bgrp.equals(s_bgroup) && !ph_no.contains(pno)) {
                                    ph_no.add(pno);
                                    new_list.add(use);
                                }
                            }
                            if (new_list.size() != 0) {
                                if (nodata.getVisibility() == View.VISIBLE) {
                                    users_rv.setVisibility(View.VISIBLE);
                                    nodata.setVisibility(View.GONE);
                                }
                                users_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new RecyclerAdapter_find_r(getActivity(), new_list);
                                users_rv.setAdapter(adapter);
                            } else {
                                if (nodata.getVisibility() == View.GONE) {

                                    nodata.setVisibility(View.VISIBLE);
                                    nodata.setText("No Results fround");
                                    users_rv.setVisibility(View.GONE);
                                    loding.setVisibility(View.GONE);
                                    loding.stop();
                                }
                            }
                        } else {
                            if (nodata.getVisibility() == View.GONE) {

                                nodata.setVisibility(View.VISIBLE);
                                nodata.setText("No Results fround");
                                users_rv.setVisibility(View.GONE);
                                loding.setVisibility(View.GONE);
                                loding.stop();
                            }

                        }
                    }else {
                        //if (nodata.getVisibility() == View.GONE) {

                            nodata.setVisibility(View.VISIBLE);
                            nodata.setText("No Results fround");
                            users_rv.setVisibility(View.GONE);
                            loding.setVisibility(View.GONE);
                            loding.stop();
                       // }

                    }
                    // do some stuff once
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),
                            "Something Went Worng",
                            Toast.LENGTH_LONG).show();

                }


            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }



    public void set_city(String state)
    {
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
                citiy2 = getSelectedValue().toString();
                location.setText(citiy2);
                if (nodata.getVisibility() == View.GONE) {
                    nodata.setVisibility(View.VISIBLE);

                    users_rv.setVisibility(View.GONE);
                    loding.setVisibility(View.GONE);
                }
                nodata.setText("Please Select blood group");
                //set_ui(citiy2);
               // Toast.makeText(getActivity(), "You have selected " + getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).items(cties_s, 0)
                .title("Select City in " + state)
                .positiveAction("OK")
                .negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }
    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        mPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("entered","ok");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        mPostReference.addValueEventListener(mPostListener);
    }

    public String loadJSONFromAsset(String state) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(state + ".json");
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
