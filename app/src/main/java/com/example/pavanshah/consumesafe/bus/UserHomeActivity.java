package com.example.pavanshah.consumesafe.bus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.api.HTTPRequestHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Global declarations
    private String mCurrentPhotoPath;
    private int IMAGE_SUCCESS_CODE = 111;
    private int PICK_IMAGE_CODE = 1;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //all declarations
        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        Button galleryButton = (Button) findViewById(R.id.galleryButton);
        progressDialog = new ProgressDialog(UserHomeActivity.this);

        //Firebase
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser activeUser = mFirebaseAuth.getCurrentUser();

        View header =  navigationView.getHeaderView(0);
        ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
        TextView userName = (TextView) header.findViewById(R.id.userName);
        TextView userEmail = (TextView) header.findViewById(R.id.userEmail);

        userName.setText(activeUser.getDisplayName());
        userEmail.setText(activeUser.getEmail());
        Picasso.with(getApplicationContext()).load(activeUser.getPhotoUrl().toString()).into(userImage);


        //all listeners
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(UserHomeActivity.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

                Log.d("IMAGECAPTURE", "scanReceipt");
                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    if (createImageFile() != null) {
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(UserHomeActivity.this, "com.example.pavanshah.consumesafe.provider", createImageFile());
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


        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "Permission denied to Read/Write your External storage", Toast.LENGTH_SHORT).show();
                    System.exit(0);
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
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            Toast.makeText(getApplicationContext(), "Capture success", Toast.LENGTH_SHORT).show();
            final Uri imageUri = Uri.parse(this.mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            this.storageRef.child("scannedReceipts").child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri imageURL = taskSnapshot.getDownloadUrl();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                    JSONObject dataJSON = new JSONObject();

                    final String url = imageURL.toString();

                    try {
                        dataJSON.put("url", url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Sending URL to server
                    HTTPRequestHandler httpRequestHandler = HTTPRequestHandler.getInstance();
                    httpRequestHandler.sendHTTPRequest("/api/storage/scannedImg", dataJSON, "consumesafeserver", new HTTPRequestHandler.VolleyCallback(){

                        @Override
                        public void onSuccess(JSONObject jSONObject) throws JSONException {
                            Toast.makeText(getApplicationContext(), "Sent to server successfully", Toast.LENGTH_SHORT).show();
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
        else if(requestCode == this.PICK_IMAGE_CODE && resultCode == -1)
        {
            Log.d("Gallery", "data "+data.getExtras());
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_subscriptions) {

            Intent intent = new Intent(UserHomeActivity.this, SubscriptionsActivity.class);
            startActivity(intent);

        } else if (id == R.id.register_complaint) {

        } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserHomeActivity.this, LandingActivity.class);
                startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
