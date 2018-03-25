package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mFirebaseAuth = FirebaseAuth.getInstance();

        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {

                FirebaseUser activeUser = mFirebaseAuth.getCurrentUser();
                Intent intent;

                if(activeUser != null)
                {
                    UserDetails userDetailsObject = new UserDetails();
                    userDetailsObject.setDisplayName(userDetailsObject.getDisplayName());
                    userDetailsObject.setEmail(userDetailsObject.getEmail());
                    userDetailsObject.setPhoneNumber(userDetailsObject.getPhoneNumber());
                    userDetailsObject.setPhotoUrl(userDetailsObject.getPhotoUrl());

                    intent = new Intent(LandingActivity.this, UserHomeActivity.class);
                }
                else
                {
                    intent = new Intent(LandingActivity.this, FeatureActivity.class);
                }

                startActivity(intent);
            }
        }.start();

    }
}
