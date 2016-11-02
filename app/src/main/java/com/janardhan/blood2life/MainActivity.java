package com.janardhan.blood2life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.main);
       /* FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))

                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());*/
    }
}
