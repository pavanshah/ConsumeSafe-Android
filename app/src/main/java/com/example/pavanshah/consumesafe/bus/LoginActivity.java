package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.GlobalFeedsAdapter;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Choose authentication providers
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                //new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
        );

        //All declarations
        Button loginButton = (Button) findViewById(R.id.loginButton);
        RecyclerView globalFeeds = (RecyclerView) findViewById(R.id.globalFeeds);


        //All listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);

            }
        });


        //Populate global feeds
        ArrayList<FeedsDetails> globalFeedsData = new ArrayList<>();

        FeedsDetails feedsDetails1 = new FeedsDetails();
        feedsDetails1.setImageURL("https://www.cpsc.gov/s3fs-public/pic11.png");
        feedsDetails1.setProductName("Nook toddler beds");
        feedsDetails1.setNewsTitle("The Land of Nod Recalls Toddler Beds Due to Entrapment Hazard (Recall Alert)");

        FeedsDetails feedsDetails2 = new FeedsDetails();
        feedsDetails2.setImageURL("https://www.cpsc.gov/s3fs-public/Évolur Sleep Ultra Crib and Toddler Bed Mattress (Model 849).jpg");
        feedsDetails2.setProductName("Crib and toddler bed mattresses");
        feedsDetails2.setNewsTitle("Dream On Me Recalls Crib & Toddler Bed Mattresses Due to Violation of Federal Mattress Flammability Standard");

        FeedsDetails feedsDetails3 = new FeedsDetails();
        feedsDetails3.setImageURL("https://www.cpsc.gov/s3fs-public/Firm foam crib and toddler bed mattress in white print.jpg");
        feedsDetails3.setProductName("Maui Ocean Center Toddler Drinking Cups");
        feedsDetails3.setNewsTitle("CPSC, Charles Products Announce Recall of Toddler Drinking Cups");

        globalFeedsData.add(feedsDetails1);
        globalFeedsData.add(feedsDetails2);
        globalFeedsData.add(feedsDetails3);


        globalFeeds.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //Populate subscription page
        final GlobalFeedsAdapter globalFeedsAdapter = new GlobalFeedsAdapter(globalFeedsData);
        globalFeeds.setAdapter(globalFeedsAdapter);
        globalFeeds.setLayoutManager(llm);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserDetails userDetailsObject = new UserDetails();
                userDetailsObject.setDisplayName(user.getDisplayName());
                userDetailsObject.setEmail(user.getEmail());
                userDetailsObject.setPhoneNumber(user.getPhoneNumber());
                userDetailsObject.setPhotoUrl(user.getPhotoUrl());

                Log.d("PAVAN", "UserDetails Object "+ userDetailsObject.toString());

                Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                startActivity(intent);

            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
