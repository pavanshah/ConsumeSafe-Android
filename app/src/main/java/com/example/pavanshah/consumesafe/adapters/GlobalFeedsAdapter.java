package com.example.pavanshah.consumesafe.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.SubscriptionDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavan on 3/23/2018.
 */

public class GlobalFeedsAdapter extends RecyclerView.Adapter<GlobalFeedsAdapter.FeedsHolder>  {

    private List<FeedsDetails> feedsList;

    public GlobalFeedsAdapter(List<FeedsDetails> feedsList) {
        this.feedsList = feedsList;
    }

    @Override
    public FeedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_feeds, parent, false);

        return new FeedsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedsHolder holder, int i) {

        FeedsDetails feedsDetails = feedsList.get(i);
        Picasso.with(holder.itemView.getContext()).load(feedsDetails.getImageURL()).resize(100, 100).into(FeedsHolder.ProductImage);
        FeedsHolder.ProductName.setText(feedsDetails.getProductName());
        FeedsHolder.NewsTitle.setText(feedsDetails.getNewsTitle());

    }

    @Override
    public int getItemCount() {
        return feedsList.size();
    }

    public static class FeedsHolder extends RecyclerView.ViewHolder {

        protected static ImageView ProductImage;
        protected static TextView ProductName;
        protected static TextView NewsTitle;

        public FeedsHolder(View itemView) {
            super(itemView);

            ProductImage =  (ImageView) itemView.findViewById(R.id.ProductImage);
            ProductName = (TextView) itemView.findViewById(R.id.ProductName);
            NewsTitle = (TextView) itemView.findViewById(R.id.NewsTitle);

        }
    }
}
