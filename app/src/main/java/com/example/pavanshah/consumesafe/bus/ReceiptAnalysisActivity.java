package com.example.pavanshah.consumesafe.bus;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.adapters.ProductListAdapter;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.ReceiptDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptAnalysisActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_analysis);

        Intent intent = getIntent();
        final ArrayList<ReceiptDetails> result = (ArrayList<ReceiptDetails>) intent.getSerializableExtra("result");
        final ArrayList<String> subscriptionCategories = (ArrayList<String>) intent.getSerializableExtra("subscriptions");
        final ProductListAdapter productListAdapter = new ProductListAdapter(getApplicationContext(), result, subscriptionCategories);

        //All declarations
        FloatingActionButton addNewProduct = (FloatingActionButton) findViewById(R.id.addNewProduct);
        Button saveProducts = (Button) findViewById(R.id.saveProducts);
        progressDialog = new ProgressDialog(ReceiptAnalysisActivity.this);

        //Define add product dialogbox params
        final Dialog dialog = new Dialog(ReceiptAnalysisActivity.this);
        dialog.setTitle("Add Missing Product");
        dialog.setContentView(R.layout.adddialog);

        // set the custom dialog components - text, image and button
        final EditText productNameET = (EditText) dialog.findViewById(R.id.productNameET);
        final Spinner ProductCategory = (Spinner) dialog.findViewById(R.id.ProductCategory);
        Button confirmButton = (Button) dialog.findViewById(R.id.confirmAdd);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelAdd);

        // if button is clicked, close the custom dialog
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameET.getText().toString();
                String productCategory = ProductCategory.getSelectedItem().toString();
                String upcApi = null;
                String image = "https://firebasestorage.googleapis.com/v0/b/consumesafefirebase.appspot.com/o/scannedReceipts%2Fimage-not-available-icon.jpg?alt=media&token=21184842-fb51-42c7-bc46-751bd9d3ae23";

                ReceiptDetails receiptDetails = new ReceiptDetails(upcApi, productName, image, productCategory);

                productListAdapter.itemAdded(receiptDetails);

                productNameET.setText("");

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

        ListView productList = findViewById(R.id.productList);
        productList.setAdapter(productListAdapter);

        saveProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ReceiptDetails> result = ProductListAdapter.getResultList();

                Log.d("Receipt", "before sending "+result);

                JSONObject finalObject = new JSONObject();
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                String userEmailId = mFirebaseAuth.getCurrentUser().getEmail();
                String deviceId = FirebaseInstanceId.getInstance().getToken();

                JSONArray productArray = new JSONArray();
                for (int i = 0 ; i < result.size() ; i++)
                {
                    JSONObject tempJSON = new JSONObject();
                    try {
                        tempJSON.put("upcApi", result.get(i).getUpcApi());
                        tempJSON.put("productName", result.get(i).getProductName());
                        tempJSON.put("image", result.get(i).getImage());
                        tempJSON.put("productCategory", result.get(i).getProductCategory());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    productArray.put(tempJSON);
                }

                try {
                    finalObject.put("email", userEmailId);
                    finalObject.put("deviceId", deviceId);
                    finalObject.put("productArray", productArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //sending to server
                Log.d("CHECK", "finalObject "+finalObject.toString());

                progressDialog.setTitle("Saving your purchase history...");
                progressDialog.show();

                HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
                httpRequestHandler.sendHTTPRequest(Request.Method.POST,"/monitorProduct/saveUserProduct", finalObject, new HTTPRequestHandler.VolleyCallback() {

                    @Override
                    public void onSuccess(JSONObject jSONObject) throws JSONException {
                        Log.d("CHECK", "final status "+jSONObject.getString("status")+" Message "+jSONObject.getString("Message"));
                        Toast.makeText(getApplicationContext(), "Products saved successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(ReceiptAnalysisActivity.this, UserHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
