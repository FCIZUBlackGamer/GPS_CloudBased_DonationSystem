package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.app.AliasActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Complain extends Activity {
    Intent intent;
    String get_email, get_user;
    EditText message;
    Button send;
    Database database;
    Cursor cursor;
    String get_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain);
        intent = getIntent();
        database = new Database(this);
        cursor = database.ShowData();

        send = findViewById(R.id.send);
        message = findViewById(R.id.message);
        get_email = intent.getStringExtra("email");
        get_user = intent.getStringExtra("user");
        while (cursor.moveToNext()) {
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);
        }
        Toast.makeText(Complain.this, get_email, Toast.LENGTH_LONG).show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_message = message.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/MakeComplain.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(Complain.this, response, Toast.LENGTH_LONG).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Complain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put("email", get_email);
                        hashMap.put("message", get_message);
                        hashMap.put("type", get_user);
                        return hashMap;
                    }
                };
                Volley.newRequestQueue(Complain.this).add(stringRequest);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Complain.this, Main_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("email", get_email);
        intent.putExtra("user", get_user);
        stopService(new Intent( Complain.this, AliasActivity.class ));
        startActivity(intent);
    }
}
