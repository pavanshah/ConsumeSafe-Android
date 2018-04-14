package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.SubscriptionAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscriptionsActivity extends AppCompatActivity {

    //Global declarations
    final ArrayList<SubscriptionDetails> subscriptionData = new ArrayList<>();
    HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        Intent intent = getIntent();
        HashMap<String, Boolean> subscriptions = (HashMap<String, Boolean>)intent.getSerializableExtra("subscriptions");
        HashMap<String, String> firebaseLabels = (HashMap<String, String>)intent.getSerializableExtra("firebaseLabels");

        ListView subscriptionList = findViewById(R.id.subscriptionList);
        Button saveChanges = findViewById(R.id.saveChanges);

        //Populate subscription page
        final SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(getApplicationContext());
        subscriptionList.setAdapter(subscriptionAdapter);

        //Save Changes
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List resultList = (ArrayList<SubscriptionDetails>) SubscriptionAdapter.getResultList();
                JSONArray mJSONArray = new JSONArray();

                for(int i = 0 ; i < resultList.size() ; i++)
                {
                    SubscriptionDetails subscriptionDetails = (SubscriptionDetails) resultList.get(i);

                    if(subscriptionDetails.getSubscribed())
                    {
                        //Subscribe to the category
                        //Log.d("CHECK", "Details "+subscriptionDetails.getFirebaseLabel() +" "+subscriptionDetails.getCatagory());
                        //FirebaseMessaging.getInstance().subscribeToTopic(subscriptionDetails.getFirebaseLabel());
                    }
                    else
                    {
                        //Unsubscribe from the category
                        //Log.d("CHECK", "Details "+subscriptionDetails.getFirebaseLabel() +" "+subscriptionDetails.getCatagory());
                        //FirebaseMessaging.getInstance().unsubscribeFromTopic(subscriptionDetails.getFirebaseLabel());
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(subscriptionDetails.getCatagory(), subscriptionDetails.getSubscribed());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mJSONArray.put(jsonObject);
                }

                //Make server call with List
                JSONObject subscriptionJSON = new JSONObject();
                JSONObject userJSON = new JSONObject();

                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser activeUser = mFirebaseAuth.getCurrentUser();
                String userEmailId = activeUser.getEmail();

                try {
                    userJSON.put("email", userEmailId);
                    subscriptionJSON.put("user", userJSON);
                    subscriptionJSON.put("subscription", mJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/subscription/subscribe", subscriptionJSON,new HTTPRequestHandler.VolleyCallback() {

                    @Override
                    public void onSuccess(JSONObject jSONObject) throws JSONException {
                        Toast.makeText(getApplicationContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SubscriptionsActivity.this, UserHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SubscriptionsActivity.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
