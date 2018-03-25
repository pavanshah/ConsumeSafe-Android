package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
