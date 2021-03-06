package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.FeedsDetails;
import com.example.pavanshah.consumesafe.model.ImageURL;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    ArrayList<String> sampleImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //All declarations
        final TextView productName = (TextView) findViewById(R.id.productName);
        final TextView newsTitle = (TextView) findViewById(R.id.newsTitle);
        final TextView newsBody = (TextView) findViewById(R.id.newsBody);
        final TextView recallDate = (TextView) findViewById(R.id.recallDate);
        final CarouselView carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setImageListener(imageListener);
        final TextView hazard = (TextView) findViewById(R.id.hazard);
        final TextView remedy = (TextView) findViewById(R.id.remedy);
        final TextView retailer = (TextView) findViewById(R.id.retailer);
        final TextView manufacturer = (TextView) findViewById(R.id.manufacturer);
        final TextView manufacturerCountries  = (TextView) findViewById(R.id.manufacturerCountries);
        final TextView consumerContact = (TextView) findViewById(R.id.consumerContact);
        sampleImages = new ArrayList<String>();

        Intent intent = getIntent();
        String RecallID = intent.getStringExtra("RecallID");

        //Fetch all details for a product
        final JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("RecallID", RecallID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
        httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/globalFeed/fetchDetails", dataJSON, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {

                FeedsDetails feedsDetails = new Gson().fromJson(jSONObject.toString(), FeedsDetails.class);
                Log.d("Details", "name "+feedsDetails.getProductName());
                Log.d("Details", "Remedy "+feedsDetails.getRemedy());

                ImageURL[] imageURL = feedsDetails.getImageURL();

                if(imageURL.length == 0)
                {
                    sampleImages.add("https://firebasestorage.googleapis.com/v0/b/consumesafefirebase.appspot.com/o/scannedReceipts%2Fimage-not-available-icon.jpg?alt=media&token=21184842-fb51-42c7-bc46-751bd9d3ae23");
                }
                else
                {
                    for(int i = 0 ; i < imageURL.length ; i++)
                    {
                        sampleImages.add(imageURL[i].getURL());
                    }
                }

                carouselView.setPageCount(sampleImages.size());
                productName.setText(feedsDetails.getProductName());
                newsTitle.setText(feedsDetails.getNewsTitle());
                newsBody.setText(feedsDetails.getNewsBody());
                recallDate.setText(feedsDetails.getRecallDate());
                hazard.setText(feedsDetails.getHazard());
                remedy.setText(feedsDetails.getRemedy());
                retailer.setText(feedsDetails.getRetailer());
                manufacturer.setText(feedsDetails.getManufacturer());
                manufacturerCountries.setText(feedsDetails.getManufacturerCountries());
                consumerContact.setText(feedsDetails.getConsumerContact());
            }
        });

    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            //imageView.setImageResource();
            Picasso.with(getApplicationContext()).load(sampleImages.get(position)).fit().into(imageView);
        }
    };

}
