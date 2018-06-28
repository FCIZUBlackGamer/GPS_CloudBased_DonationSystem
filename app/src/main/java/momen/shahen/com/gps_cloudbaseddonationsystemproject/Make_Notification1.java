package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.app.AliasActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Make_Notification1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String text;
    Spinner spinner;
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;
    byte[] paramtersbyt;
    String connectionparamters;
    String url;
    String getemail;
    String emailkey, contentkey, doctornamekey, reqstatekey, sponserkey;
    Bundle bundle;
    ImageView report;
    ConnectivityManager connManager;
    NetworkInfo mWifi;

    Intent intent;
    String get_email, get_user;
    EditText get_donor_email_to_update;
    String string_get_donor_email_to_update;

    Database database;
    Cursor cursor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_noti1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        get_donor_email_to_update = new EditText(this);
        get_donor_email_to_update.setHint("Enter Donor Email");
        get_donor_email_to_update.setBackgroundColor(getColor(R.color.colorTextPrimary));
        get_donor_email_to_update.setTextColor(getColor(R.color.black));
        intent = getIntent();
        get_email = intent.getStringExtra("email");
        get_user = intent.getStringExtra("user");

        database = new Database(this);
        cursor = database.ShowData();

        while (cursor.moveToNext()){
            get_email = cursor.getString(1);
            get_user = cursor.getString(3);

        }

        report = (ImageView) findViewById(R.id.add_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Make_Notification1.this, Send_report.class));
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Make_Notification1.this);
                builder.setTitle("Update Donation Period For Donor")
                        .setView(get_donor_email_to_update)
                        .setMessage("Make Sure That Email Is Correct")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                string_get_donor_email_to_update = get_donor_email_to_update.getText().toString();

                                final ProgressDialog progressDialog = new ProgressDialog(Make_Notification1.this);
                                progressDialog.setMessage("Updating Donation Period ... ");
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://momenshaheen.16mb.com/UpdateLastDonationTime.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(final String response) {

                                                progressDialog.dismiss();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Make_Notification1.this, response, Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Make_Notification1.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("email", string_get_donor_email_to_update);
                                        return hashMap;
                                    }
                                };
                                Volley.newRequestQueue(Make_Notification1.this).add(stringRequest);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do Noting
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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

    public void onStart() {
        super.onStart();
//        getemail = getIntent().getStringExtra("Email_login");
//        Bundle extras = getIntent().getExtras();
//        if (extras != null || getemail != null) {
//            String value = getIntent().getStringExtra("Email_login");
//            //Toast.makeText(Make_Notification1.this,value+"   "+getemail , Toast.LENGTH_LONG).show();
//        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner.getSelectedItem().toString();

                if (text.equals("blood") || text.equals("دم")) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_Blood_noti();
                    transaction.replace(R.id.noti_type, newFragment).commit();

                } else if (text.equals("money") || text.equals("نقود")) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_money_noti();
                    transaction.replace(R.id.noti_type, newFragment).commit();

                } else if (text.equals("other") || text.equals("أخرى")) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager = getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = new Make_Point_noti();
                    transaction.replace(R.id.noti_type, newFragment).commit();

                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getStringExtra("Blood_DocName") != null) {
            Toast.makeText(Make_Notification1.this, getIntent().getStringExtra("Blood_DocName")
                    + getIntent().getStringExtra("Blood_Content") +
                    getIntent().getStringExtra("Blood_SelectedItem"), Toast.LENGTH_SHORT).show();

            Toast.makeText(Make_Notification1.this, getemail, Toast.LENGTH_LONG).show();
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//            if (mWifi.isConnected()) {
                url = "https://gradproject2018.000webhostapp.com/Donation%20System/InsertBloodNotification.php";
                emailkey = "email=";
                contentkey = "&content=";
                doctornamekey = "&doctorname=";
                reqstatekey = "&reqstate=";
                try {
                    connectionparamters = emailkey + URLEncoder.encode(get_email, "UTF-8") + contentkey
                            + URLEncoder.encode(getIntent().getStringExtra("Blood_Content"), "UTF-8") +
                            doctornamekey + URLEncoder.encode(getIntent().getStringExtra("Blood_DocName"),
                            "UTF-8") + reqstatekey + URLEncoder.encode(getIntent().getStringExtra("Blood_SelectedItem"), "UTF-8");

                    paramtersbyt = connectionparamters.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Runnable runnable = new Runnable() {
                    public void run() {
                        try {

                            URL insertUserUrl = new URL(url);
                            HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
                            insertConnection.setRequestMethod("POST");
                            insertConnection.getOutputStream().write(paramtersbyt);
                            InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
                            BufferedReader resultReader = new BufferedReader(resultStreamReader);
                            final String result = resultReader.readLine();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
//            } else {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(this);
//                }
//                builder.setTitle("Error Message!")
//                        .setMessage("Make Sure You Are Connected To Wifi!")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
        } else if (getIntent().getStringExtra("Money_Content") != null) {
            Toast.makeText(Make_Notification1.this, getIntent().getStringExtra("Money_Content")
                    , Toast.LENGTH_SHORT).show();

//            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            assert connManager != null;
//            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//            if (mWifi.isConnected()) {
                url = "https://gradproject2018.000webhostapp.com/Donation%20System/InsertMoneyNotification.php";
                emailkey = "email=";
                contentkey = "&content=";
                try {
                    connectionparamters = emailkey + URLEncoder.encode(get_email, "UTF-8") + contentkey
                            + URLEncoder.encode(getIntent().getStringExtra("Money_Content"), "UTF-8");
                    paramtersbyt = connectionparamters.getBytes("UTF-8");

                    paramtersbyt = connectionparamters.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Runnable runnable = new Runnable() {
                    public void run() {
                        try {

                            URL insertUserUrl = new URL(url);
                            HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
                            insertConnection.setRequestMethod("POST");
                            insertConnection.getOutputStream().write(paramtersbyt);
                            InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
                            BufferedReader resultReader = new BufferedReader(resultStreamReader);
                            final String result = resultReader.readLine();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
//            } else {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(this);
//                }
//                builder.setTitle("Error Message!")
//                        .setMessage("Make Sure You Are Connected To Wifi!")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
        } else if (getIntent().getStringExtra("Point_Sponser") != null) {
            Toast.makeText(Make_Notification1.this, getIntent().getStringExtra("Point_Sponser")
                            + getIntent().getStringExtra("Point_Content")
                    , Toast.LENGTH_SHORT).show();

//            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            assert connManager != null;
//            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//            if (mWifi.isConnected()) {
                url = "https://gradproject2018.000webhostapp.com/Donation%20System/InsertOtherNotification.php";
                emailkey = "email=";
//                sponserkey = "&sponser=";
                contentkey = "&content=";
                try {
                    connectionparamters = emailkey + URLEncoder.encode(get_email, "UTF-8") /*+
                            sponserkey + URLEncoder.encode(getIntent().getStringExtra("Point_Sponser"), "UTF-8") */+ contentkey
                            + URLEncoder.encode(getIntent().getStringExtra("Point_Content"), "UTF-8");
                    paramtersbyt = connectionparamters.getBytes("UTF-8");

//                    paramtersbyt = connectionparamters.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Runnable runnable = new Runnable() {
                    public void run() {
                        try {

                            URL insertUserUrl = new URL(url);
                            HttpURLConnection insertConnection = (HttpURLConnection) insertUserUrl.openConnection();
                            insertConnection.setRequestMethod("POST");
                            insertConnection.getOutputStream().write(paramtersbyt);
                            InputStreamReader resultStreamReader = new InputStreamReader(insertConnection.getInputStream());
                            BufferedReader resultReader = new BufferedReader(resultStreamReader);
                            final String result = resultReader.readLine();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Make_Notification1.this, result, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
//            } else {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(this);
//                }
//                builder.setTitle("Error Message!")
//                        .setMessage("Make Sure You Are Connected To Wifi!")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
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
            startActivity(new Intent(Make_Notification1.this, Settings_activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Make_Notification1.this, Main_Home.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_camera) {
            if (get_user.equals("donor")) {
                Intent intent = new Intent(Make_Notification1.this, Home.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Make_Notification1.this, Hospital_post.class);
                intent.putExtra("email", get_email);
                intent.putExtra("user", get_user);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Make_Notification1.this, Profile.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Make_Notification1.this, Reports.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.make_notification) {
//            Intent intent = new Intent(Profile.this,Make_Notification1.class);
//            intent.putExtra( "email",get_email );
//            intent.putExtra( "user",get_user );
//            startActivity( intent );
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Make_Notification1.this, About.class);
            intent.putExtra("email", get_email);
            intent.putExtra("user", get_user);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Make_Notification1.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            stopService(new Intent( Make_Notification1.this, NotificationService.class ));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
