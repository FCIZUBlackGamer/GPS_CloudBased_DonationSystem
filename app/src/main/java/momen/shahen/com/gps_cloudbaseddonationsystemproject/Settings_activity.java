package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fci on 14/01/18.
 */

public class Settings_activity extends Activity implements View.OnClickListener {
    private static final int REQUEST_LOCATION = 1;
    Button button;
    LocationManager locationManager;
    String lattitude, longitude;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";
    Database database;
    Cursor cursor;
    String getemail, gettype;
    String url;

    Intent intent;
    String get_email, get_user;
    Button change_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.setting );
        ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION );
        database = new Database( this );
        intent = getIntent();
        change_pass = (Button) findViewById( R.id.change_pass );
        get_email = intent.getStringExtra( "email" );
        get_user = intent.getStringExtra( "user" );

        button = (Button) findViewById( R.id.button_location );
        change_pass.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout1 = new LinearLayout( Settings_activity.this );
                final EditText pass = new EditText( Settings_activity.this );
                final EditText new_pass = new EditText( Settings_activity.this );
                pass.setHint( "New Password!" );
                pass.setTextColor( R.color.colorPrimaryDark );
                pass.setHintTextColor( R.color.colorPrimaryDark );
                linearLayout1.setOrientation( LinearLayout.VERTICAL );

                new_pass.setHint( "Confirm Password!" );
                new_pass.setTextColor( R.color.colorPrimaryDark );
                new_pass.setHintTextColor( R.color.colorPrimaryDark );
                if (pass.getParent() != null)
                    ((ViewGroup) pass.getParent()).removeView( pass ); // <- fix
                linearLayout1.addView( pass );

                if (new_pass.getParent() != null)
                    ((ViewGroup) new_pass.getParent()).removeView( new_pass ); // <- fix
                linearLayout1.addView( new_pass );
                linearLayout1.setBackgroundColor( R.color.back );
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder( Settings_activity.this, R.style.myDialog );
                } else {
                    builder = new AlertDialog.Builder( Settings_activity.this );
                }
                builder.setTitle( "Confirm Message!" )
                        .setMessage( "Change Password!" )
                        .setView( linearLayout1 )
                        .setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (pass.getText().toString().equals( new_pass.getText().toString() ) && pass.getText().toString().length() >= 8) {
                                    final ProgressDialog progressDialog = new ProgressDialog( Settings_activity.this );
                                    progressDialog.setMessage( "Updating Password ... " );
                                    progressDialog.show();
                                    StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/UpdatePassword.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(final String response) {

                                                    progressDialog.dismiss();
                                                    runOnUiThread( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText( Settings_activity.this, response, Toast.LENGTH_LONG ).show();
                                                        }
                                                    } );

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText( Settings_activity.this, error.getMessage(), Toast.LENGTH_SHORT ).show();
                                        }
                                    } ) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap hashMap = new HashMap();
                                            hashMap.put( "email", get_email );
                                            hashMap.put( "pass", pass.getText().toString() );
                                            hashMap.put( "type", get_user );
                                            return hashMap;
                                        }
                                    };
                                    Volley.newRequestQueue( Settings_activity.this ).add( stringRequest );
                                } else {
                                    Toast.makeText( Settings_activity.this, "Password Doesn't Match", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } )
                        .setNegativeButton( android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        } )
                        .setIcon( android.R.drawable.ic_dialog_alert )
                        .show();
            }
        } );

        button.setOnClickListener( this );

    }

    @Override
    public void onClick(View v) {
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        assert locationManager != null;
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            Toast.makeText( this, "Please Turn On Your Location!", Toast.LENGTH_SHORT ).show();

        } else if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            //getLocation();
            cursor = database.ShowData();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    getemail = cursor.getString( 1 );
                    gettype = cursor.getString( 3 );
                    getMyCurrentLocation();
                    if (get_user.equals( "donar" )) {
                        StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/UpdateDonnerLocation.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText( Settings_activity.this, response, Toast.LENGTH_LONG ).show();

                                            }
                                        } );
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText( Settings_activity.this, error.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap hashMap = new HashMap();
                                hashMap.put( "email", get_email );
                                hashMap.put( "lan", MyLong + "" );
                                hashMap.put( "lat", MyLat + "" );
                                return hashMap;
                            }
                        };
                        Volley.newRequestQueue( Settings_activity.this ).add( stringRequest );
                    } else if (get_user.equals( "hospital" )) {
                        StringRequest stringRequest = new StringRequest( Request.Method.POST, "http://momenshaheen.16mb.com/UpdateHospitalLocation.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText( Settings_activity.this, response, Toast.LENGTH_LONG ).show();

                                            }
                                        } );
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText( Settings_activity.this, error.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap hashMap = new HashMap();
                                hashMap.put( "email", get_email );
                                hashMap.put( "lan", MyLong + "" );
                                hashMap.put( "lat", MyLat + "" );
                                return hashMap;
                            }
                        };
                        Volley.newRequestQueue( Settings_activity.this ).add( stringRequest );
                    } else {
                        Toast.makeText( this, "No Email", Toast.LENGTH_SHORT ).show();
                    }
                }
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( Settings_activity.this, Main_Home.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra( "user", get_user );
        intent.putExtra( "email", get_email );
        startActivity( intent );
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission( Settings_activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                ( Settings_activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( Settings_activity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION );

        } else {
            Location location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

            Location location1 = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );

            Location location2 = locationManager.getLastKnownLocation( LocationManager.PASSIVE_PROVIDER );

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf( latti );
                longitude = String.valueOf( longi );

                Toast.makeText( this, "Your current location is" + "\n" + "Latitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG ).show();

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf( latti );
                longitude = String.valueOf( longi );

                Toast.makeText( this, "Your current location is" + "\n" + "Latitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG ).show();


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf( latti );
                longitude = String.valueOf( longi );


                Toast.makeText( this, "Your current location is" + "\n" + "Latitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG ).show();

            } else {

                Toast.makeText( this, "Unable to Trace your location", Toast.LENGTH_SHORT ).show();

            }


        }
    }

    @SuppressLint("MissingPermission")
    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        LocationListener locListener = new MyLocationListener();


        try {
            gps_enabled = locManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

        if (gps_enabled) {
            locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locListener );

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );


        }


        if (network_enabled && location == null) {
            locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locListener );

        }


        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc = getLastKnownLocation( this );
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates( locListener ); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder( this, Locale.getDefault() );
            addresses = geocoder.getFromLocation( MyLat, MyLong, 1 );

            StateName = addresses.get( 0 ).getAdminArea();
            CityName = addresses.get( 0 ).getLocality();
            CountryName = addresses.get( 0 ).getCountryName();
            // you can get more details other than this . like country code, state code, etc.


          /*  Toast.makeText(this," StateName " + StateName+"\n"+
                    " CityName " + CityName+"\n"+
                    " CountryName " + CountryName,Toast.LENGTH_LONG).show();
                    */
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText( this, "" + MyLat + "\n" +
                MyLong + "\n StateName " + StateName +
                "\n CityName " + CityName + "\n CountryName "
                + CountryName, Toast.LENGTH_LONG ).show();
        Log.e( "Lat: ", MyLat + "" );
        Log.e( "Long: ", MyLong + "" );
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context) {
        Location location = null;
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager) context.getSystemService( "location" );
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled( s ))
                continue;
            // System.out.println("provider ===> "+s);
            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation( s );
            if (location1 == null)
                continue;
            if (location != null) {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if (f >= f1) {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if (l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled( s );
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }

}
