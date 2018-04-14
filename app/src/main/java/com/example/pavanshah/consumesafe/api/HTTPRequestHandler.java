package com.example.pavanshah.consumesafe.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPRequestHandler extends Application {
    private static HTTPRequestHandler instance;
    private static Context mContext;
    //String fcmPath = "https://fcm.googleapis.com/fcm/send";
    JSONObject finalResult = new JSONObject();
    private RequestQueue mRequestQueue = null;
    String serverPath = "http://consumesafe-dev.herokuapp.com";

    public interface VolleyCallback {
        void onSuccess(JSONObject jSONObject) throws JSONException;
    }

    class C03791 implements RetryPolicy {
        C03791() {
        }

        public int getCurrentTimeout() {
            return 50000;
        }

        public int getCurrentRetryCount() {
            return 50000;
        }

        public void retry(VolleyError error) throws VolleyError {
        }
    }

    public static HTTPRequestHandler getInstance() {
        return instance;
    }

    public static Context getMyContext() {
        return mContext;
    }

    public RequestQueue getRequestQueue() {
        Log.d("SPECIAL", "queue " + this.mRequestQueue);
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getMyContext());
        }

        return mRequestQueue;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("SPECIAL", "Initialized all");
        instance = this;
        mContext = getApplicationContext();
    }

    public void sendHTTPRequest(int method, String customurl, JSONObject body, final VolleyCallback callback) {

        String url = this.serverPath + customurl;;
        RequestQueue queue = getRequestQueue();
        Log.d("SPECIAL", "url " + url);
        Log.d("SPECIAL", "params " + body);

        queue.add(new JsonObjectRequest(method, url, body, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                finalResult = response;
                Log.d("SPECIAL", "result " + finalResult);
                try {
                    callback.onSuccess(finalResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("SPECIAL", "Inside error2" + error.getMessage());
                VolleyLog.e("SPECIAL", error.getMessage());
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    switch (response.statusCode) {
                        case 400:
                            try {
                                HTTPRequestHandler.this.finalResult.put("status", "400");
                                callback.onSuccess(HTTPRequestHandler.this.finalResult);
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        default:
                            return;
                    }
                }
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "key=AAAAL2WcTL4:APA91bHLDe05quDjejKmDUklly-jHPeZVLab39ZGCvgvX2YNUxmO4ipIjR-WYNudYT0VwTB4t6pmuvD719Yw6TWeAsNW3UBucXQUiOQrBI34ZRpJYPid1CH6qdYEjh26vxFf-6V4BNt7");
                return params;
            }
        }.setRetryPolicy(new C03791()));
    }
}
