package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;

public class LandingActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private TextView textView;
    private ImageView imageView;
    private TextView consumeSafeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Intent firebaseIntent = getIntent();
        consumeSafeTV = (TextView) findViewById(R.id.consumesafe_textView);
        textView = (TextView) findViewById(R.id.landing_textView);
        imageView = (ImageView) findViewById(R.id.landing_imageView);
        Animation myAnimation= AnimationUtils.loadAnimation(this,R.anim.myanimation);
        consumeSafeTV.startAnimation(myAnimation);
        textView.startAnimation(myAnimation);
        imageView.startAnimation(myAnimation);
        mFirebaseAuth = FirebaseAuth.getInstance();

        if(firebaseIntent.getExtras() != null && firebaseIntent.getExtras().containsKey("RecallID"))
        {
            Bundle firebaseMessage = firebaseIntent.getExtras();
            Log.d("FCM", "Message here "+firebaseMessage.get("RecallID"));

            String RecallID = firebaseMessage.get("RecallID").toString();
            Intent detailsIntent = new Intent(LandingActivity.this, DetailsActivity.class);
            detailsIntent.putExtra("RecallID", RecallID);
            startActivity(detailsIntent);
        }
        else
        {
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
                        intent = new Intent(LandingActivity.this, UserHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    else
                    {
                        intent = new Intent(LandingActivity.this, FeatureActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }

                    startActivity(intent);
                }
            }.start();
        }

    }
}
