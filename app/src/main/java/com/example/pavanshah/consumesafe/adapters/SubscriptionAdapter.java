package com.example.pavanshah.consumesafe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavan on 3/23/2018.
 */

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionHolder>  {

    private List<SubscriptionDetails> subscriptionList;
    private Context context;
    private List<SubscriptionDetails> resultList;

    public SubscriptionAdapter(Context context, List<SubscriptionDetails> subscriptionList) {
        this.context = context;
        this.subscriptionList = subscriptionList;

        Log.d("Subscribe", "Arraylist "+this.subscriptionList.size());
    }

    @Override
    public SubscriptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_subscription, parent, false);

        return new SubscriptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubscriptionHolder holder, int i) {
        SubscriptionDetails subscriptionDetails = subscriptionList.get(i);
        SubscriptionHolder.Catagory.setText(subscriptionDetails.getCatagory());
        SubscriptionHolder.Subscribed.setChecked(subscriptionDetails.getSubscribed());
    }

    @Override
    public int getItemCount() {
        Log.d("Subscribe", "Arraylist here "+subscriptionList.size());
        return subscriptionList.size();
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {

        protected static TextView Catagory;
        protected static CheckBox Subscribed;

        public SubscriptionHolder(View itemView) {
            super(itemView);

            Catagory =  (TextView) itemView.findViewById(R.id.Catagory);
            Subscribed = (CheckBox) itemView.findViewById(R.id.Subscribed);

            Subscribed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                }
            });

        }

    }

}
