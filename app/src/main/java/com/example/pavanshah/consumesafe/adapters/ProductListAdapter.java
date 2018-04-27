package com.example.pavanshah.consumesafe.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.bus.DetailsActivity;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.ImageURL;
import com.example.pavanshah.consumesafe.model.ReceiptDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pavan on 3/23/2018.
 */

public class ProductListAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;

    static ArrayList<ReceiptDetails> resultList = new ArrayList<>();

    ArrayList<String> subscriptionCategories = new ArrayList<>();

    HashMap<Integer, Integer> mapRowSpinnerPos = new HashMap<>();

    Context context;

    public ProductListAdapter(Context context) {

        this.context = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public ProductListAdapter(Context context, ArrayList<ReceiptDetails> arrayList, ArrayList<String> subscriptionCategories) {

        this.context=context;
        resultList=arrayList;
        this.subscriptionCategories = subscriptionCategories;
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
        Spinner Category;
    }

    public void datasetchanged(ArrayList<ReceiptDetails> arrayList, ArrayList<String> subscriptionCategories) {

        resultList = arrayList;
        this.subscriptionCategories = subscriptionCategories;
        Log.d("Product", "Data set changed");
        notifyDataSetChanged();
    }


    public void itemAdded(ReceiptDetails newItem) {

        resultList.add(newItem);
        Log.d("Receipt", "new item added");
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ProductListAdapter.FeedsHolder holder = new ProductListAdapter.FeedsHolder();

        View rowView;

        rowView = inflater.inflate(R.layout.list_products, null);

        holder.ProductImage = (ImageView) rowView.findViewById(R.id.ProductImage);

        holder.ProductName = (TextView) rowView.findViewById(R.id.ProductName);

        holder.Category = (Spinner) rowView.findViewById(R.id.Category);

        final ReceiptDetails receiptDetails = resultList.get(i);

        String imageURL = receiptDetails.getImage();
        Picasso.with(context).load(imageURL).resize(100, 100).into(holder.ProductImage);

        holder.ProductName.setText(receiptDetails.getProductName());

        String [] paths = subscriptionCategories.toArray(new String[subscriptionCategories.size()]);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.Category.setAdapter(adapter);
        holder.Category.setTag(i);

        if (mapRowSpinnerPos.containsKey(i)) {
            holder.Category.setSelection(mapRowSpinnerPos.get(i));
        }

        holder.Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                int index = (int) adapterView.getTag();

                Log.d("Receipt", "on update index "+index);

                ReceiptDetails receiptDetails1 = resultList.get(index);
                receiptDetails1.setProductCategory(adapterView.getSelectedItem().toString());

                mapRowSpinnerPos.put(index, position);

                Log.d("Receipt", "on update receiptdetails "+receiptDetails1.getProductName()+" "+receiptDetails1.getProductCategory());

                resultList.set(index, receiptDetails1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rowView;
    }

    public static ArrayList<ReceiptDetails> getResultList() {
        return resultList;
    }
}