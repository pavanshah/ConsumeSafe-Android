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
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        final JSONObject dataJSON = new JSONObject();
        final ArrayList<FeedsDetails> globalFeedsData = new ArrayList<>();
        final GlobalFeedsAdapter globalFeedsAdapter = new GlobalFeedsAdapter(globalFeedsData);;
        globalFeeds.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        globalFeeds.setAdapter(globalFeedsAdapter);
        globalFeeds.setLayoutManager(llm);

        HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
        httpRequestHandler.sendHTTPRequest("/globalFeed/fetch", dataJSON, "consumesafeserver", new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {

                JSONArray allRecalls = jSONObject.getJSONArray("result");

                Log.d("feeds", "length "+allRecalls.length());
                for(int i = 0 ; i < allRecalls.length() ; i++)
                {
                    FeedsDetails feedsDetails = new Gson().fromJson(allRecalls.get(i).toString(), FeedsDetails.class);
                    globalFeedsData.add(feedsDetails);
                }

                globalFeedsAdapter.notifyDataSetChanged();
                Log.d("feeds", "data set changed");

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

                //Subscribe to global feeds
                FirebaseMessaging.getInstance().subscribeToTopic("Global");

                Log.d("PAVAN", "UserDetails Object "+ userDetailsObject.toString());

                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
