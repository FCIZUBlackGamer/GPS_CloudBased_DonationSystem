package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fci on 06/02/18.
 */

public class RegistrationServices extends IntentService {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    public RegistrationServices() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken("221676839457", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (!preferences.getBoolean("token_sent", false))
                sendTokenToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Registration Service", "Error :get Token Failed !");
        }

    }
    private void sendTokenToServer(final String token) {
        String ADD_TOKEN_URL = "http://developerhendy.16mb.com/addnewtoken.php";
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int responseCode = Integer.parseInt(response);
                if (responseCode == 1) {
                    prefEditor.putBoolean("token_sent", true).apply();
                    Log.e("Registration Service", "Response : Send Token Success");

                } else {
                    prefEditor.putBoolean("token_sent", false).apply();
                    Log.e("Registration Service", "Response : Send Token Failed");


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prefEditor.putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;

            }
        };

        Volley.newRequestQueue(this).add(request);

    }
}
