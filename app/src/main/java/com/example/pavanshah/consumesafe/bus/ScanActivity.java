package com.example.pavanshah.consumesafe.bus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.example.pavanshah.consumesafe.model.ReceiptDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScanActivity extends AppCompatActivity {

    private int IMAGE_SUCCESS_CODE = 111;
    private String mCurrentPhotoPath;
    ProgressDialog progressDialog;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
    ArrayList<String> subscriptionCategories;
    Spinner ProductCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //All declarations
        Button cameraButton = (Button) findViewById(R.id.scanReceipt);
        Button addProductManually = (Button) findViewById(R.id.addProductManually);
        progressDialog = new ProgressDialog(ScanActivity.this);
        subscriptionCategories  = new ArrayList<>();

        //find all categories for dropdown
        findAllSubscriptionCategories();

        //Define add product dialogbox params
        final Dialog dialog = new Dialog(ScanActivity.this);
        dialog.setTitle("Add Product Manually");
        dialog.setContentView(R.layout.adddialog);

        // set the custom dialog components - text, image and button
        EditText productNameET = (EditText) dialog.findViewById(R.id.productNameET);
        ProductCategory = (Spinner) dialog.findViewById(R.id.ProductCategory);
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

        //Listeners
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(ScanActivity.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

                Log.d("IMAGECAPTURE", "scanReceipt");
                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    if (createImageFile() != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(ScanActivity.this, "com.example.pavanshah.consumesafe.provider", createImageFile());
                        } catch (IOException e) {
                            Log.d("IMAGECAPTURE", "error2");
                            e.printStackTrace();
                        }
                        takePictureIntent.putExtra("output", photoURI);
                        startActivityForResult(takePictureIntent, IMAGE_SUCCESS_CODE);
                    }
                } catch (IOException ex) {
                    Log.d("IMAGECAPTURE", "error1 ");
                    ex.printStackTrace();
                }

            }
        });


        addProductManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "Permission denied to Read/Write your External storage", Toast.LENGTH_SHORT).show();
                    return;
                }

                return;
            default:
                return;
        }
    }

    private File createImageFile() throws IOException {
        Log.d("IMAGECAPTURE", "createFile");
        File image = File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera"));
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == this.IMAGE_SUCCESS_CODE && resultCode == -1) {
            progressDialog.setTitle("Image is uploading...");
            progressDialog.show();
            Toast.makeText(getApplicationContext(), "Capture success", Toast.LENGTH_SHORT).show();
            final Uri imageUri = Uri.parse(this.mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            this.storageRef.child("scannedReceipts").child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri imageURL = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    progressDialog.setTitle("Scanning your receipt now...");

                    JSONObject dataJSON = new JSONObject();
                    JSONObject tempJSON = new JSONObject();

                    String url = imageURL.toString();
                    //url = "https://firebasestorage.googleapis.com/v0/b/consumesafefirebase.appspot.com/o/scannedReceipts%2FWhatsApp%20Image%202018-04-07%20at%2010.41.49%20PM.jpeg?alt=media&token=54688001-518e-4f4a-9358-ed8977af93fc";

                    try {
                        tempJSON.put("imageURL", url);
                        dataJSON.put("data", tempJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Sending URL to server
                    httpRequestHandler.sendHTTPRequest(Request.Method.POST, "/scanReceipt", dataJSON, new HTTPRequestHandler.VolleyCallback(){

                        @Override
                        public void onSuccess(JSONObject jSONObject) throws JSONException {
                            progressDialog.dismiss();
                            JSONArray result = jSONObject.getJSONArray("result");
                            ArrayList<ReceiptDetails> receiptDetailsArray = new ArrayList<>();

                            for(int i = 0 ; i < result.length() ; i++)
                            {
                                ReceiptDetails receiptDetails = new Gson().fromJson(result.get(i).toString(), ReceiptDetails.class);
                                receiptDetailsArray.add(receiptDetails);
                            }

                            Intent intent = new Intent(ScanActivity.this, ReceiptAnalysisActivity.class);
                            intent.putExtra("result", receiptDetailsArray);
                            intent.putExtra("subscriptions", subscriptionCategories);
                            startActivity(intent);
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void findAllSubscriptionCategories()
    {
        JSONObject dataJSON = new JSONObject();

        httpRequestHandler.sendHTTPRequest(Request.Method.GET,"/subscription/allCategories", dataJSON, new HTTPRequestHandler.VolleyCallback() {

            @Override
            public void onSuccess(JSONObject jSONObject) throws JSONException {
                JSONArray subscriptionArray = jSONObject.getJSONArray("categories");

                for(int i = 0 ; i < subscriptionArray.length() ; i++)
                {
                    JSONObject singleEntry = (JSONObject) subscriptionArray.get(i);
                    subscriptionCategories.add(singleEntry.getString("Category_Name"));
                }

                String[] paths = subscriptionCategories.toArray(new String[subscriptionCategories.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ProductCategory.setAdapter(adapter);

            }
        });
    }
}