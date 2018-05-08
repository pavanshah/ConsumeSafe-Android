package com.example.pavanshah.consumesafe.bus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.pavanshah.consumesafe.R;

public class RegisterComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);

        String cpscFormURL = "https://www.saferproducts.gov/CPSRMSPublic/Incidents/ReportIncident.aspx";

        WebView cpscWebsite = findViewById(R.id.cpscForm);
        cpscWebsite.getSettings().setJavaScriptEnabled(true);
        cpscWebsite.getSettings().setPluginState(WebSettings.PluginState.ON);
        cpscWebsite.getSettings().setUserAgentString("Android");
        cpscWebsite.getSettings().setAllowFileAccess(true);
        cpscWebsite.setInitialScale(1);
        cpscWebsite.getSettings().setUseWideViewPort(true);
        cpscWebsite.getSettings().setLoadWithOverviewMode(true);
        cpscWebsite.getSettings().setBuiltInZoomControls(true);
        cpscWebsite.getSettings().setSupportZoom(true);
        cpscWebsite.loadUrl(cpscFormURL);
    }
}
