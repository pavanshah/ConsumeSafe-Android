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
import android.widget.LinearLayout;
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
    private static List<SubscriptionDetails> resultList = new ArrayList<>();

    public SubscriptionAdapter(List<SubscriptionDetails> subscriptionList) {
        this.subscriptionList = subscriptionList;
        resultList = subscriptionList;
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

        if(i%2 == 0)
        {
            SubscriptionHolder.firstItem.setVisibility(View.VISIBLE);
            SubscriptionHolder.secondItem.setVisibility(View.GONE);
            SubscriptionHolder.Catagory.setText(subscriptionDetails.getCatagory());
            SubscriptionHolder.Subscribed.setChecked(subscriptionDetails.getSubscribed());
            SubscriptionHolder.Subscribed.setTag(i);
        }
        else
        {
            SubscriptionHolder.firstItem.setVisibility(View.GONE);
            SubscriptionHolder.secondItem.setVisibility(View.VISIBLE);
            SubscriptionHolder.Catagory2.setText(subscriptionDetails.getCatagory());
            SubscriptionHolder.Subscribed2.setChecked(subscriptionDetails.getSubscribed());
            SubscriptionHolder.Subscribed2.setTag(i);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("Subscribe", "Arraylist here "+subscriptionList.size());
        return subscriptionList.size();
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {

        protected static TextView Catagory;
        protected static CheckBox Subscribed;

        protected static TextView Catagory2;
        protected static CheckBox Subscribed2;

        protected static LinearLayout firstItem;
        protected static LinearLayout secondItem;

        public SubscriptionHolder(View itemView) {
            super(itemView);

            Catagory =  (TextView) itemView.findViewById(R.id.Catagory);
            Subscribed = (CheckBox) itemView.findViewById(R.id.Subscribed);
            Catagory2 =  (TextView) itemView.findViewById(R.id.Catagory2);
            Subscribed2 = (CheckBox) itemView.findViewById(R.id.Subscribed2);

            firstItem = (LinearLayout) itemView.findViewById(R.id.firstItem);
            secondItem = (LinearLayout) itemView.findViewById(R.id.secondItem);

            Subscribed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox thisCheckBox = (CheckBox) view;
                    Boolean result = thisCheckBox.isChecked();
                    int position = (int) thisCheckBox.getTag();

                    Log.d("Subscribe", "Button "+thisCheckBox.getTag());
                    Log.d("Subscribe", "Status "+result);

                    resultList.get(position).setSubscribed(result);
                }
            });


            Subscribed2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox thisCheckBox = (CheckBox) view;
                    Boolean result = thisCheckBox.isChecked();
                    int position = (int) thisCheckBox.getTag();

                    Log.d("Subscribe", "Button "+thisCheckBox.getTag());
                    Log.d("Subscribe", "Status "+result);

                    resultList.get(position).setSubscribed(result);
                }
            });

        }

    }


    public static List<SubscriptionDetails> getSubscriptionList() {
        return resultList;
    }

}
