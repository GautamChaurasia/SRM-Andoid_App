package com.example.ezee;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAVgQqIbw:APA91bGDvJyMSKLz2bkIAsv-BBPw07bkZw4OLtVQg6Q32cvUFw_40H3f39UpD32zYCvA3qvSDJtwJhRD6TNR1Fgwpx3-XomrccBL3BOB5qK_oPX7UEMEWBUMzRXjF3N2K8ykpEbRbC85";

    public static void pushNotification(Context context, String token, String title, String message){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);

            JSONObject json = new JSONObject();
            json.put("to", token);
            json.put("priority", "high");
            json.put("data", notification);

            JsonObjectRequest jsonReq = new JsonObjectRequest(BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };

            queue.add(jsonReq);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
