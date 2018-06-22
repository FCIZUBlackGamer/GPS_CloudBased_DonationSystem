package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by fci on 14/01/18.
 */

public class donor_frag extends Fragment {
    EditText name, email, pass, con_pass, age;
    Button save, getloc;
    Bundle bundle;
    Activity context;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.donor_signup, container, false);
        context = getActivity();
        name = (EditText) v.findViewById(R.id.don_name);
        email = (EditText) v.findViewById(R.id.don_email);
        con_pass = (EditText) v.findViewById(R.id.don_con_pass);
        age = (EditText) v.findViewById(R.id.don_age);
        pass = (EditText) v.findViewById(R.id.don_pass);
        getloc = (Button) v.findViewById(R.id.don_get_loc);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentLocation();
            }
        });
        save = (Button) getActivity().findViewById(R.id.don_save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create an Intent object

                if (con_pass.getText().toString().equals(pass.getText().toString()) && !String.valueOf(MyLat).equals("null") && !String.valueOf(MyLong).equals("null")) {
                    Intent intent = new Intent(context, Sign_up.class);
                    //add data to the Intent object
                    intent.putExtra("Don_name", name.getText().toString());
                    intent.putExtra("Don_email", email.getText().toString());
                    intent.putExtra("Don_pass", pass.getText().toString());
                    intent.putExtra("Don_age", age.getText().toString());
                    // Location
                    intent.putExtra("Don_lat", MyLat + "");
                    intent.putExtra("Don_lan", MyLong + "");
                    //start the second activity
                    startActivity(intent);


                } else {
                    Toast.makeText(getActivity(), "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @SuppressLint("MissingPermission")
    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new donor_frag.MyLocationListener();


        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


        if (network_enabled && location == null) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }


        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc = getLastKnownLocation(context);
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            StateName = addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.


          /*  Toast.makeText(this," StateName " + StateName+"\n"+
                    " CityName " + CityName+"\n"+
                    " CountryName " + CountryName,Toast.LENGTH_LONG).show();
                    */
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "" + MyLat + "\n" +
                MyLong + "\n StateName " + StateName +
                "\n CityName " + CityName + "\n CountryName "
                + CountryName, Toast.LENGTH_LONG).show();
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
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation(s);
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
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }
}
