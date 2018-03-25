package com.example.pavanshah.consumesafe.bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.SubscriptionAdapter;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;

import java.util.ArrayList;

public class SubscriptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        //All declarations
        final ArrayList<SubscriptionDetails> subscriptionData = new ArrayList<>();
        SubscriptionDetails subscriptionDetails1 = new SubscriptionDetails();
        subscriptionDetails1.setCatagory("Baby Products");
        subscriptionDetails1.setSubscribed(true);

        SubscriptionDetails subscriptionDetails2 = new SubscriptionDetails();
        subscriptionDetails2.setCatagory("Healthcare");
        subscriptionDetails2.setSubscribed(false);

        SubscriptionDetails subscriptionDetails3 = new SubscriptionDetails();
        subscriptionDetails3.setCatagory("Personal Care");
        subscriptionDetails3.setSubscribed(true);

        subscriptionData.add(subscriptionDetails1);
        subscriptionData.add(subscriptionDetails2);
        subscriptionData.add(subscriptionDetails3);

        RecyclerView subscriptionList = (RecyclerView) findViewById(R.id.subscriptionList);
        subscriptionList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //Populate subscription page
        final SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(getApplicationContext(), subscriptionData);
        subscriptionList.setAdapter(subscriptionAdapter);
        subscriptionList.setLayoutManager(llm);

    }
}
