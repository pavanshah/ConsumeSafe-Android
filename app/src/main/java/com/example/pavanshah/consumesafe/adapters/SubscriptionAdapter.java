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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pavan on 3/23/2018.
 */

public class SubscriptionAdapter extends BaseAdapter  {

    private static LayoutInflater inflater=null;
    Context context;
    private static List<SubscriptionDetails> resultList = new ArrayList<>();

    public SubscriptionAdapter(Context context) {
        this.context = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public SubscriptionAdapter(Context context,  HashMap<String, Boolean> subscriptions) {
        this.context=context;

        resultList.clear();

        Iterator it = subscriptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

            SubscriptionDetails subscriptionDetails = new SubscriptionDetails(pair.getKey().toString(), (Boolean) pair.getValue());
            resultList.add(subscriptionDetails);
            it.remove(); // avoids a ConcurrentModificationException
        }

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class SubscriptionHolder {
        protected TextView Catagory;
        protected CheckBox Subscribed;
    }

    public void datasetchanged(HashMap<String, Boolean> subscriptions) {

        resultList.clear();

        Iterator it = subscriptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

            SubscriptionDetails subscriptionDetails = new SubscriptionDetails(pair.getKey().toString(), (Boolean) pair.getValue());
            resultList.add(subscriptionDetails);
            it.remove(); // avoids a ConcurrentModificationException
        }

        Log.d("Product", "Data set changed");
        notifyDataSetChanged();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final SubscriptionAdapter.SubscriptionHolder holder = new SubscriptionAdapter.SubscriptionHolder();

        View rowView;

        rowView = inflater.inflate(R.layout.list_subscription, null);

        holder.Catagory = (TextView) rowView.findViewById(R.id.Catagory);

        holder.Subscribed = (CheckBox) rowView.findViewById(R.id.Subscribed);

        final SubscriptionDetails subscriptionDetails = resultList.get(i);

        holder.Catagory.setText(subscriptionDetails.getCatagory());
        holder.Subscribed.setChecked(subscriptionDetails.getSubscribed());
        holder.Subscribed.setTag(i);

        holder.Subscribed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int index = (int) compoundButton.getTag();
                SubscriptionDetails subscriptionDetails = resultList.get(index);
                subscriptionDetails.setSubscribed(b);
                resultList.add(index, subscriptionDetails);
            }
        });

        return rowView;
    }

    public static List<SubscriptionDetails> getResultList() {
        return resultList;
    }
}
