package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {


    Button donn, hosp;
    TextView signup;
    EditText email, pass;
    Database database;
    Cursor cursor;
    CheckBox remember_me;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        database = new Database(this);

        email = (EditText) findViewById(R.id.loginemail);
        pass = (EditText) findViewById(R.id.loginpass);
        remember_me = (CheckBox) findViewById(R.id.remember_me);

        //database.InsertData(" ", " "," ");
        cursor = database.ShowData();
        Log.e("NUMBER", cursor.getCount() + "");
        if (cursor.getCount() == 0) {
            database.InsertData(" ", "0", "");
            Log.e("INSERT", "ROW INSERTED");
//        }else if (cursor.getCount()>1){
//            for (int i=2;i<cursor.getCount();i++) {
//                database.DeleteData(i+"");
//            }
        } else if (cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                Log.e("GO", "ROW FOUND" + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
                if (cursor.getString(2).equals("1")) {
                    Log.e("GO", "ROW FOUND");
                    String email = cursor.getString(1);
                    String type = cursor.getString(3);
                    Log.e("GO", "ROW FOUND" + email + type);
                    intent = new Intent(MainActivity.this, Main_Home.class);
                    intent.putExtra("email", email);
                    intent.putExtra("user", type);
                    startActivity(intent);
                }
            }
        }

        intent = new Intent(MainActivity.this, Main_Home.class);
        donn = (Button) findViewById(R.id.donne);
        donn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String type = "donor";
                final String au = email.getText().toString();
                final String pos = pass.getText().toString();
                final String emailkey = "email";
                final String passkey = "pass";
                final String typekey = "type";
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Connecting ...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/Login.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        intent.putExtra("email", au);
                                        intent.putExtra("user", type);
                                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                        if (response.equals("Welcome Home!")) {
                                            Log.e("CHECKED", "Remember me");
                                            database.UpdateData("1", au, "1", type, "0", "yes", "0");
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, "Can't Connect to Server!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put(emailkey, au);
                        hashMap.put(passkey, pos);
                        hashMap.put(typekey, type);
                        return hashMap;
                    }
                };
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            }
        });
        hosp = (Button) findViewById(R.id.hos);
        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String type = "hospital";
                final String au = email.getText().toString();
                final String pos = pass.getText().toString();
                final String emailkey = "email";
                final String passkey = "pass";
                final String typekey = "type";
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Connecting ...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/Login.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        intent.putExtra("email", au);
                                        intent.putExtra("user", type);
                                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                        if (response.equals("Welcome Home!")) {
                                                Log.e("CHECKED", "Remember me");
                                                database.UpdateData("1", au, "1", type, "0", "yes", "0");
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put(emailkey, au);
                        hashMap.put(passkey, pos);
                        hashMap.put(typekey, type);
                        return hashMap;
                    }
                };
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);

            }
        });
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Sign_up.class));
            }
        });
    }
}
