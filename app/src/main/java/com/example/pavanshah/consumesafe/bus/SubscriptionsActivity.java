package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.SubscriptionAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        Intent intent = getIntent();
        HashMap<String, Boolean> subscriptions = (HashMap<String, Boolean>)intent.getSerializableExtra("subscriptions");

        ListView subscriptionList = findViewById(R.id.subscriptionList);

        //Populate subscription page
        final SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(getApplicationContext(), subscriptions);
        subscriptionList.setAdapter(subscriptionAdapter);
    }

    @Override
    public void onBackPressed() {

         List resultList = (ArrayList<SubscriptionDetails>) SubscriptionAdapter.getResultList();

         for(int i = 0 ; i < resultList.size() ; i++)
         {
             SubscriptionDetails subscriptionDetails = (SubscriptionDetails) resultList.get(i);
             Log.d("BACK", subscriptionDetails.getCatagory()+" "+subscriptionDetails.getSubscribed());
         }
        //Make server call with List

        Intent intent = new Intent(SubscriptionsActivity.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
