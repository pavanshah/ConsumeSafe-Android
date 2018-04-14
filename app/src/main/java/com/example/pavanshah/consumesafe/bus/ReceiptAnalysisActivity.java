package com.example.pavanshah.consumesafe.bus;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.ProductListAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.ReceiptDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptAnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_analysis);

        Intent intent = getIntent();
        final ArrayList<ReceiptDetails> result = (ArrayList<ReceiptDetails>) intent.getSerializableExtra("result");
        final ArrayList<String> subscriptionCategories = (ArrayList<String>) intent.getSerializableExtra("subscriptions");

        //All declarations
        FloatingActionButton addNewProduct = (FloatingActionButton) findViewById(R.id.addNewProduct);
        Button saveProducts = (Button) findViewById(R.id.saveProducts);

        //Define add product dialogbox params
        final Dialog dialog = new Dialog(ReceiptAnalysisActivity.this);
        dialog.setTitle("Add Missing Product");
        dialog.setContentView(R.layout.adddialog);

        // set the custom dialog components - text, image and button
        EditText productNameET = (EditText) dialog.findViewById(R.id.productNameET);
        final Spinner ProductCategory = (Spinner) dialog.findViewById(R.id.ProductCategory);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirmAdd);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelAdd);

        // if button is clicked, close the custom dialog
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //All listeners
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

            }
        });

        HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();

        //fetch all the categories from server
        JSONObject dataJSON = new JSONObject();
        //final ArrayList<String> subscriptionCategories = new ArrayList<>();
        final ProductListAdapter productListAdapter = new ProductListAdapter(getApplicationContext(), result, subscriptionCategories);

        /*httpRequestHandler.sendHTTPRequest(Request.Method.GET,"/subscription/allCategories", dataJSON, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {
                JSONArray subscriptionArray = jSONObject.getJSONArray("categories");

                for(int i = 0 ; i < subscriptionArray.length() ; i++)
                {
                    JSONObject singleEntry = (JSONObject) subscriptionArray.get(i);
                    subscriptionCategories.add(singleEntry.getString("Category_Name"));
                }*/

                productListAdapter.datasetchanged(result, subscriptionCategories);

                String [] paths = subscriptionCategories.toArray(new String[subscriptionCategories.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ProductCategory.setAdapter(adapter);

                ProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        switch (position) {
                            case 0:
                                // Whatever you want to happen when the first item gets selected
                                break;
                            case 1:
                                // Whatever you want to happen when the second item gets selected
                                break;
                            case 2:
                                // Whatever you want to happen when the thrid item gets selected
                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

         //   }
       // });

        ListView productList = findViewById(R.id.productList);
        productList.setAdapter(productListAdapter);
    }
}
