package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.HTTP;
import retrofit2.http.POST;

public class Hospital_post extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent;
    String get_email, get_user;
    Spinner spinner;
    String get_value;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<blood_noti_item> bloodNotiItem;
    Database database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        database = new Database(this);
        spinner = (Spinner) findViewById(R.id.spin_post_hospital);
        recyclerView = (RecyclerView) findViewById(R.id.hosp_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodNotiItem = new ArrayList<>();
        intent = getIntent();
        get_email = intent.getStringExtra("email");
        get_user = intent.getStringExtra("user");

        database = new Database(this);
        cursor = database.ShowData();

        while (cursor.moveToNext()){
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get_value = spinner.getSelectedItem().toString();
//                if (isNetworkConnected()) {
                    if (get_email.isEmpty()) {
                        Snackbar.make(view, "No Session Started Yet!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        LoadRecyclerViewData(get_email, get_value);
                    }
//                } else {
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(Hospital_post.this, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(Hospital_post.this);
//                    }
//                    builder.setTitle("Error Message!")
//                            .setMessage("Make Sure You Are Connected To Wifi!")
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // continue with delete
//                                }
//                            })
//                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // do nothing
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void LoadRecyclerViewData(final String email, final String value) {
        final int size = bloodNotiItem.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                bloodNotiItem.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
        final ProgressDialog progressDialog = new ProgressDialog(Hospital_post.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/GetHospitalPosts.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
//                            String s = URLEncoder.encode(response, "ISO-8859-1");
//                            response = URLDecoder.decode(s, "UTF-8");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("noti_data");
                            if (jsonArray.length() == 0) {

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    blood_noti_item blood_noti = new blood_noti_item(
                                            object.getString("doctorName"),
                                            object.getString("content"),
                                            object.getString("date"),
                                            object.getString("requestState"),
                                            object.getString("Name"),
                                            object.getString("rate"),
                                            object.getString("notificationType")
                                    );
                                    bloodNotiItem.add(blood_noti);
                                }
                                adapter = new Hospital_AllPosts_Adapter(bloodNotiItem, Hospital_post.this);
                                recyclerView.setAdapter(adapter);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Hospital_post.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("type", value);
                hashMap.put("email", email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(Hospital_post.this).add(stringRequest);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent v = new Intent(Hospital_post.this, Settings_activity.class);
            v.putExtra("user", get_user);
            v.putExtra("email", get_email);
            startActivity(v);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Hospital_post.this, Main_Home.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_camera) {
//            Intent intent = new Intent(Hospital_post.this,Home.class);
//            intent.putExtra( "email",get_email );
//            intent.putExtra( "user",get_user );
//            startActivity( intent );
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Hospital_post.this, Profile.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Hospital_post.this, Reports.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.make_notification) {
            if (get_user.equals("hospital")) {
                Intent intent = new Intent(Hospital_post.this, Make_Notification1.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Toast.makeText(Hospital_post.this, "This Option Available For Hospital Only", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Hospital_post.this, About.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Hospital_post.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            database.UpdateData("1", get_email, "0", get_user,"0","no","0");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
