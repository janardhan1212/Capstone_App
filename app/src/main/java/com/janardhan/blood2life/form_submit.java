package com.janardhan.blood2life;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.janardhan.blood2life.Helpers.SQLiteHandler;
import com.janardhan.blood2life.Helpers.SessionManager;
import com.janardhan.blood2life.Main_screen.Ma_screen_sample;
import com.janardhan.blood2life.Main_screen.NewMainActivity;
import com.janardhan.blood2life.pojos.User_s;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;

import org.json.JSONObject;

public class form_submit extends AppCompatActivity {
    String state, city, nametx, emailtx, phone, group;
    EditText name, email, phno;
    private ProgressView pv_circular_inout;
    private SQLiteHandler db;
    private SessionManager session;
    private String user_id,email_id,user_name;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    String pro_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.init(this, 1, 0, null);
        setContentView(R.layout.activity_form_submit);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        // Firebase.setAndroidContext(this);
        pv_circular_inout = (ProgressView) findViewById(R.id.sin_p_cir);
//        Parse.initialize(this, app_config.APPLICATION_ID,app_config.CLIENT_KEY);
        final Spinner spn_label = (Spinner) findViewById(R.id.spinner_label);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phno = (EditText) findViewById(R.id.phno);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        String[] items = {"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spn_label.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            state = bundle.getString("state");
            city = bundle.getString("city");
            user_id=bundle.getString("user_id");
            email_id=bundle.getString("email_id");
            user_name=bundle.getString("user_name");
        }
        name.setText(user_name);
        email.setText(email_id);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                nametx = name.getText().toString();
                emailtx = email.getText().toString();
                phone = phno.getText().toString();
                group = spn_label.getSelectedItem().toString();
                String op = null;
                if (group.contains("+")) {
                    op = group.replace("+", "pos");
                } else {
                    op = group.replace("-", "neg");
                }
                if (nametx != "" && emailtx != "" && phone != "" && group != "Select Blood Group") {
                    showProgressDialog();
                    writeNewUser(user_id,nametx,emailtx,phone,group);
                    hideProgressDialog();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.before_sign,
                            Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
                hideProgressDialog();
            }

        });
        getProfileInformation();
    }
    public void getProfileInformation() {
        final String[] profilePicUrl = new String[1];
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
                                    pro_url = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Log.e("profilePicUrl", profilePicUrl[0]);
                                    // set profile image to imageview using Picasso or Native methods
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();

    }
    private void writeNewUser(String user__id, final String name_tx, final String email_tx, final String phone_tx2, final String group_tx) {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

       User_s user = new User_s(name_tx,email_tx,phone_tx2,pro_url
               ,group_tx,state,city,"mobile","Male",refreshedToken);

        mDatabase.child("mobile_users").child(user__id).setValue(user).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(form_submit.this, R.string.went_worng,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseMessaging.getInstance().subscribeToTopic(city);

                    db.addUser(FirebaseInstanceId.getInstance().getId(),name_tx, email_tx, phone_tx2, group_tx, city,state,refreshedToken,pro_url);
                    session.setLogin(true);
                    //tag_mobile(phone, city);
                    Intent intent = new Intent(form_submit.this, NewMainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            R.string.login_success,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
   /* private void tag_mobile(String phone2, String city2) {

        Pushbots.sharedInstance().setAlias(phone2);
        Pushbots.sharedInstance().tag(city2);
    }*/

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


}
