package com.example.pavanshah.consumesafe.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.bus.DetailsActivity;
import com.example.pavanshah.consumesafe.bus.LandingActivity;
import com.example.pavanshah.consumesafe.bus.LoginActivity;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pavan on 3/23/2018.
 */

public class GlobalFeedsAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;

    ArrayList<FeedsDetails> resultList = new ArrayList<>();

    Context context;

    public GlobalFeedsAdapter(Context context) {

        this.context = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public GlobalFeedsAdapter(Context context,  ArrayList<FeedsDetails> arrayList) {

        this.context=context;
        resultList=arrayList;
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

    public class FeedsHolder {

        ImageView ProductImage;
        TextView ProductName;
        TextView NewsTitle;
    }

    public void datasetchanged(ArrayList<FeedsDetails> arrayList) {

        resultList = arrayList;

        Log.d("Product", "Data set changed");

        notifyDataSetChanged();

    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final GlobalFeedsAdapter.FeedsHolder holder = new GlobalFeedsAdapter.FeedsHolder();

        View rowView;

        rowView = inflater.inflate(R.layout.list_feeds, null);

        holder.ProductImage = (ImageView) rowView.findViewById(R.id.ProductImage);

        holder.ProductName = (TextView) rowView.findViewById(R.id.ProductName);

        holder.NewsTitle = (TextView) rowView.findViewById(R.id.NewsTitle);

        final FeedsDetails feedsDetails = resultList.get(i);

        //Picasso.with(context).load(feedsDetails.getImageURL()).resize(100, 100).into(holder.ProductImage);
        holder.ProductName.setText(feedsDetails.getProductName());
        holder.NewsTitle.setText(feedsDetails.getNewsTitle());
        rowView.setTag(feedsDetails.getRecallID());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String RecallID = view.getTag().toString();

                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra("RecallID", RecallID);
                context.startActivity(detailsIntent);
            }
        });

        return rowView;

    }
}