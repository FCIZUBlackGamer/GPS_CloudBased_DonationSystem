package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by fci on 17/01/18.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class Make_Blood_noti extends Fragment {
    EditText docname,cont;
    Spinner spinner;
    Button button1;
    Bundle bundle;
    Activity context;
    String url;
    String getemail;
    String emailkey, contentkey, doctornamekey, reqstatekey, citykey;
    Cursor cursor;
    Database database;

    private static final int REQUEST_LOCATION = 1;
    Button button;
    private boolean gps_enabled=false;
    private boolean network_enabled=false;
    Location location;

    Double MyLat, MyLong;
    String CityName="";
    String StateName="";
    String CountryName="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make_blood_noti,container,false);
        context = getActivity();
        database = new Database(context);
        return v;
    }

    public void onStart(){
        super.onStart();
        button1=(Button)getActivity().findViewById(R.id.bloodpost);
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                docname = (EditText)context.findViewById(R.id.blooddoc);
                cont = (EditText)context.findViewById(R.id.bloodcon);
                spinner = (Spinner) context.findViewById(R.id.bloodsp);

                cursor = database.ShowData();
                if (cursor.getCount()==0)
                {
                    Toast.makeText(getActivity(),"No Email Found",Toast.LENGTH_SHORT).show();
                }else {

                    while (cursor.moveToNext()) {
                        getemail = cursor.getString( 1 );

                        getMyCurrentLocation();
                        url = "http://momenshaheen.16mb.com/InsertBloodNotification.php";
                        citykey = "city_name";
                        emailkey = "email";
                        contentkey = "content";
                        doctornamekey = "doctorname";
                        reqstatekey = "reqstate";
                        // StateName getemail cont.getText().toString() docname.getText().toString() spinner.getSelectedItem().toString()
                        final ProgressDialog progressDialog = new ProgressDialog( context );
                        progressDialog.setMessage( "Updating Donation Period ... " );
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest( Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {

                                        progressDialog.dismiss();
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText( context, response, Toast.LENGTH_LONG ).show();
                                            }
                                        } );

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText( context, error.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap hashMap = new HashMap();
                                hashMap.put( citykey, StateName );
                                hashMap.put( emailkey, getemail );
                                hashMap.put( contentkey, cont.getText().toString() );
                                hashMap.put( doctornamekey, docname.getText().toString() );
                                hashMap.put( reqstatekey, spinner.getSelectedItem().toString() );
                                return hashMap;
                            }
                        };
                        Volley.newRequestQueue( context ).add( stringRequest );
                    }
                }

            }

        });
    }
    @SuppressLint("MissingPermission")
    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new Make_Blood_noti.MyLocationListener();


        try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

        if(gps_enabled){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if(gps_enabled){
            location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


        if(network_enabled && location==null){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }


        if(network_enabled && location==null)    {
            location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc= getLastKnownLocation(getActivity());
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try
        {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            StateName= addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.


          /*  Toast.makeText(this," StateName " + StateName+"\n"+
                    " CityName " + CityName+"\n"+
                    " CountryName " + CountryName,Toast.LENGTH_LONG).show();
                    */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(),""+MyLat+"\n"+
                MyLong+"\n StateName " + StateName +
                "\n CityName " + CityName +"\n CountryName "
                + CountryName,Toast.LENGTH_LONG).show();
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

    public static Location getLastKnownLocation(Context context)
    {
        Location location = null;
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager)context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do
        {
            //System.out.println("---------------------------------------------------------------------");
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }

}
