package com.example.pavanshah.consumesafe.bus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.GlobalFeedsAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 123;
    FirebaseUser activeUser;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Choose authentication providers
        /*final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                //new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
        );*/


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //logout request
        if(intent.hasExtra("logout"))
        {
            FirebaseAuth.getInstance().signOut();

            // Google sign out

            mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(LoginActivity.this, LandingActivity.class);
                            startActivity(intent1);
                        }
                    });
        }

        //All declarations
        SignInButton loginButton = findViewById(R.id.loginButton);
        loginButton.setSize(SignInButton.SIZE_WIDE);
        ListView globalFeeds = (ListView) findViewById(R.id.globalFeeds);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        activeUser = mFirebaseAuth.getCurrentUser();
        loginButton.setVisibility(activeUser == null ? View.VISIBLE : View.GONE);
        final ProgressBar loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        loading_spinner.setVisibility(View.VISIBLE);

        //All listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);*/

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        //Populate global feeds

        final JSONObject dataJSON = new JSONObject();
        final ArrayList<FeedsDetails> globalFeedsData = new ArrayList<>();
        final GlobalFeedsAdapter globalFeedsAdapter = new GlobalFeedsAdapter(getApplicationContext(), globalFeedsData);
        globalFeeds.setAdapter(globalFeedsAdapter);

        HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
        httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/globalFeed/fetch", dataJSON, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {

                JSONArray allRecalls = jSONObject.getJSONArray("result");

                loading_spinner.setVisibility(View.GONE);

                Log.d("feeds", "length "+allRecalls.length());
                for(int i = 0 ; i < allRecalls.length() ; i++)
                {
                    FeedsDetails feedsDetails = new Gson().fromJson(allRecalls.get(i).toString(), FeedsDetails.class);
                    globalFeedsData.add(feedsDetails);
                }

                globalFeedsAdapter.datasetchanged(globalFeedsData);
                Log.d("Product", "data set changed "+globalFeedsData.size());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately

                Log.d("login", "Google sign in failed", e);

            }

        }
    }


    // [START auth_with_google]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("login", "firebaseAuthWithGoogle:" + acct.getId());

        loginProgress = ProgressDialog.show(this, "Google Sign In",
                "Sign In in progress", true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d("login", "signInWithCredential:success");

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            //check if user already exists in database
                            JSONObject dataJSON = new JSONObject();
                            JSONObject userJSON = new JSONObject();
                            JSONArray providerData = new JSONArray();
                            JSONObject singleProvider = new JSONObject();

                            try {
                                userJSON.put("email", user.getEmail());
                                userJSON.put("displayName", user.getDisplayName());
                                userJSON.put("photoURL", user.getPhotoUrl());

                                singleProvider.put("providerId", user.getProviderId());
                                providerData.put(singleProvider);

                                userJSON.put("providerData", providerData);
                                userJSON.put("deviceID", FirebaseInstanceId.getInstance().getToken());

                                dataJSON.put("user", userJSON);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
                            httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/user/authenticate", dataJSON, new HTTPRequestHandler.VolleyCallback() {

                                @Override
                                public void onSuccess(JSONObject jSONObject) throws JSONException {
                                    Log.d("login", jSONObject.get("success")+" "+jSONObject.get("message"));

                                    if(jSONObject.getBoolean("isNewUser"))
                                    {
                                        //FirebaseMessaging.getInstance().subscribeToTopic("Global");
                                    }

                                    FirebaseMessaging.getInstance().subscribeToTopic("Global");
                                    Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            });

          //                  updateUI(user);

                        } else {

                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();

                            Log.w("login", "signInWithCredential:failure", task.getException());

            //                updateUI(null);

                        }

                        loginProgress.dismiss();

                    }
                });
    }

    // [END auth_with_google]


}
