package com.example.pavanshah.consumesafe.bus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.GlobalFeedsAdapter;
import com.example.pavanshah.consumesafe.adapters.SubscriptionAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.UserDetails;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Global declarations
    UserDetails userDetails;
    HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
    HashMap<String, Boolean> subscriptions;
    HashMap<String, String> firebaseLabels;
    ListView categorizedFeeds;
    FirebaseUser activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //all declarations
        View header =  navigationView.getHeaderView(0);
        final ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
        final TextView userName = (TextView) header.findViewById(R.id.userName);
        final TextView userEmail = (TextView) header.findViewById(R.id.userEmail);
        final TextView noSubscriptionsMessage = (TextView) findViewById(R.id.noSubscriptionsMessage);
        categorizedFeeds = findViewById(R.id.globalFeeds);

        //Default visibility
        categorizedFeeds.setVisibility(View.GONE);
        noSubscriptionsMessage.setVisibility(View.GONE);

        //Firebase
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        activeUser = mFirebaseAuth.getCurrentUser();
        String userEmailId = activeUser.getEmail();

        //Populate all user details
        JSONObject userDetailsJSON = new JSONObject();
        try {
            userDetailsJSON.put("email", userEmailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/user/getUserInfo", userDetailsJSON,new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {
                JSONObject user = jSONObject.getJSONObject("user");
                userDetails = new Gson().fromJson(user.toString(), UserDetails.class);

                userName.setText(userDetails.getDisplayName());
                userEmail.setText(userDetails.getEmail());
                Picasso.with(getApplicationContext()).load(userDetails.getPhotoUrl()).into(userImage);

                if(userDetails.getSubscribedCategories() == null)
                {
                    //not subscribed to any categories
                    categorizedFeeds.setVisibility(View.GONE);
                    noSubscriptionsMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    noSubscriptionsMessage.setVisibility(View.GONE);
                    categorizedFeeds.setVisibility(View.VISIBLE);
                    populateSubscriptions();
                    personalizeFeeds();
                }
            }
        });

        /*galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
            }
        });*/
    }


    public void populateSubscriptions()
    {
        //fetch all the categories from server
        JSONObject dataJSON = new JSONObject();

        httpRequestHandler.sendHTTPRequest(Request.Method.GET,"/subscription/allCategories", dataJSON, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {
                JSONArray subscriptionArray = jSONObject.getJSONArray("categories");

                subscriptions = new HashMap<>();
                firebaseLabels = new HashMap<>();

                for(int i = 0 ; i < subscriptionArray.length() ; i++)
                {
                    JSONObject singleEntry = (JSONObject) subscriptionArray.get(i);
                    subscriptions.put(singleEntry.getString("Category_Name"), false);
                    firebaseLabels.put(singleEntry.getString("Category_Name"), singleEntry.getString("FirebaseLabel"));
                }

                if(userDetails.getSubscribedCategories() != null)
                {
                    String[] mySubscriptions = userDetails.getSubscribedCategories();

                    for(int j = 0 ; j < mySubscriptions.length ; j++)
                    {
                        subscriptions.put(mySubscriptions[j], true);
                    }

                    Log.d("Subscribe", "My subscriptions "+subscriptions.toString());
                }

                SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(getApplicationContext());
                subscriptionAdapter.datasetchanged(subscriptions);
            }
        });
    }


    public void personalizeFeeds(){
        //fetch categorized feeds
        final ArrayList<FeedsDetails> categorizedFeedsData = new ArrayList<>();
        final GlobalFeedsAdapter globalFeedsAdapter = new GlobalFeedsAdapter(getApplicationContext(), categorizedFeedsData);
        categorizedFeeds.setAdapter(globalFeedsAdapter);

        String userEmailID = activeUser.getEmail();

        final JSONObject dataJSON1 = new JSONObject();
        final JSONObject userJSON = new JSONObject();

        try {
            userJSON.put("email", userEmailID);
            dataJSON1.put("user", userJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/subscription/categorizedFeeds", dataJSON1, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {

                JSONArray allRecalls = jSONObject.getJSONArray("result");

                Log.d("feeds", "length "+allRecalls.length());
                for(int i = 0 ; i < allRecalls.length() ; i++)
                {
                    JSONObject singleCategory = (JSONObject) allRecalls.get(i);
                    JSONArray categoryFeeds = singleCategory.getJSONArray("feedResults");

                    for(int j = 0 ; j < categoryFeeds.length() ; j++)
                    {
                        JSONObject singleObject = (JSONObject) categoryFeeds.get(j);

                        //Replacing string object with JSON array
                        String url = singleObject.getString("ImageURL");
                        JSONArray ImageURL = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("URL", url);
                        ImageURL.put(jsonObject);
                        singleObject.put("ImageURL", ImageURL);

                        FeedsDetails feedsDetails = new Gson().fromJson(categoryFeeds.get(j).toString(), FeedsDetails.class);
                        categorizedFeedsData.add(feedsDetails);

                    }
                }

                globalFeedsAdapter.datasetchanged(categorizedFeedsData);
                Log.d("Product", "data set changed "+categorizedFeedsData.size());

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.register_products :
                Intent registerIntent = new Intent(UserHomeActivity.this, ScanActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.global_feeds :
                Intent globalintent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(globalintent);
                break;

            case R.id.my_subscriptions :
                Intent subscriptionintent = new Intent(UserHomeActivity.this, SubscriptionsActivity.class);
                subscriptionintent.putExtra("subscriptions", subscriptions);
                subscriptionintent.putExtra("firebaseLabels", firebaseLabels);
                startActivity(subscriptionintent);
                break;

            case R.id.register_complaint :
                break;

            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(UserHomeActivity.this, LandingActivity.class);
                startActivity(logout);

            default :
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
