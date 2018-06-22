package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class NotificationService extends Service {

    Button button;
    LocationManager locationManager;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";
    String getdistanse, notistate, last_num_rows;
    String url;

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    Database database;
    Cursor cursor;
    String last_num, prev_num;
    int num = 0;

    public NotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        last_num_rows = null;
        getMyCurrentLocation();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        Toast.makeText(this, "Service Starts!", Toast.LENGTH_LONG).show();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please Turn On Your Location!", Toast.LENGTH_SHORT).show();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Notification Is based on Location!", Toast.LENGTH_SHORT).show();
            database = new Database(context);
            cursor = database.ShowData();
            while (cursor.moveToNext()) {
                notistate = cursor.getString(5);
                Toast.makeText(this, notistate, Toast.LENGTH_SHORT).show();
                getdistanse = cursor.getString(4);
                Toast.makeText(this, getdistanse, Toast.LENGTH_SHORT).show();

            }
            if (last_num_rows == null)
                last_num_rows = "0";
            if (Float.parseFloat(getdistanse) == 0.0f)
                getdistanse = "100";
            if (!notistate.equals("no")) {
                Toast.makeText(this, "Notification Is Enabled!", Toast.LENGTH_SHORT).show();
                handler = new Handler();
                Log.d("Lat: ", MyLat + "");
                Log.d("Long: ", MyLong + "");
                runnable = new Runnable() {
                    public void run() {

//                        Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                        handler.postDelayed(runnable, 10000 /*5*60*10000*/); // 5 Minuit
                        while (cursor.moveToNext()) {
                            getdistanse = cursor.getString(4);
                            last_num_rows = cursor.getString(6);
                            notistate = cursor.getString(5);
                        }
                        if (notistate.equals("yes")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gradproject2018.000webhostapp.com/Donation%20System/loc.php",
                                    new Response.Listener<String>() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onResponse(String response) {
//                                        Toast.makeText(context, "J-2", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(context, "J-1", Toast.LENGTH_SHORT).show();

                                            try {
//                                            Toast.makeText(context, "J0", Toast.LENGTH_SHORT).show();
                                                JSONObject jsonObject = new JSONObject(response);
//                                            Toast.makeText(context, "J1", Toast.LENGTH_SHORT).show();
                                                JSONArray jsonArray = jsonObject.getJSONArray("Data");
//                                            Toast.makeText(context, "J2", Toast.LENGTH_SHORT).show();
                                                if (jsonArray.length() <= 1) {
//                                                Toast.makeText(context, "Array Is Empty!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    JSONObject object = jsonArray.getJSONObject(num);
//                                                    Toast.makeText(context, "J3", Toast.LENGTH_SHORT).show();
                                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                                                    mBuilder.setSmallIcon(R.mipmap.logo_notification);
                                                    Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
                                                    mBuilder.setSound(sound);
                                                    mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
                                                    mBuilder.setContentTitle("Hospital " + object.getString("Name"));
//                                                    Toast.makeText(context, "J4", Toast.LENGTH_SHORT).show();

                                                    mBuilder.setContentText(object.getString("content"));
                                                    Intent resultIntent = new Intent(context, Home.class);
//                                                    resultIntent.putExtra("VALUE", "OK");
                                                    resultIntent.putExtra("num_rows", object.getString("IdNotification"));
                                                    last_num = object.getString("IdNotification");

                                                    database.UpdateLastNumRows("1", num + "");
                                                    num++;
                                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                                                    stackBuilder.addParentStack(Home.class);
//                                                    Toast.makeText(context, "J5", Toast.LENGTH_SHORT).show();

                                                    // Adds the Intent that starts the Activity to the top of the stack
                                                    stackBuilder.addNextIntent(resultIntent);
                                                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    mBuilder.setContentIntent(resultPendingIntent);

                                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                    Random random = new Random();
                                                    int notificationID = random.nextInt(10000);
                                                    // notificationID allows you to update the notification later on.
                                                    mNotificationManager.notify(notificationID, mBuilder.build());
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//
                                    if (error instanceof ServerError)
                                        Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                    else if (error instanceof NetworkError)
                                        Toast.makeText(context, "Bad Network", Toast.LENGTH_SHORT).show();
                                    else if (error instanceof TimeoutError)
                                        Toast.makeText(context, "Time out", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("last_num_rows", last_num_rows + "");
                                    hashMap.put("distance", getdistanse + "");
                                    hashMap.put("lat", MyLat + "");
                                    hashMap.put("lon", MyLong + "");
                                    return hashMap;
                                }
                            };
                            Volley.newRequestQueue(context).add(stringRequest);
                        } else {

                        }
                    }
                };


                handler.postDelayed(runnable, 10000);
                prev_num = last_num;

            } else {
                Toast.makeText(this, "Notification Is Not Enabled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(context, "On Start Command", Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service Finished", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started By Donor.", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new Settings_activity.MyLocationListener();


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
            Location loc = getLastKnownLocation(this);
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
            geocoder = new Geocoder(this, Locale.getDefault());
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

//        Toast.makeText(this, "" + MyLat + "\n" +
//                MyLong + "\n StateName " + StateName +
//                "\n CityName " + CityName + "\n CountryName "
//                + CountryName, Toast.LENGTH_LONG).show();
        Log.e("Lat: ", MyLat + "");
        Log.e("Long: ", MyLong + "");
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
