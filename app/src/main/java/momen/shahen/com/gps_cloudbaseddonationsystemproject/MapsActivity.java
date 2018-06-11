package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String c_name, lng, lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lng = getIntent().getStringExtra("Lang");
        c_name = getIntent().getStringExtra("name");
        lat = getIntent().getStringExtra("Lat");
        //Toast.makeText(this,c_name+"\n"+lat+"\n"+lng,Toast.LENGTH_SHORT).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lng = getIntent().getStringExtra("Lang");
        c_name = getIntent().getStringExtra("name");
        lat = getIntent().getStringExtra("Lat");
        Toast.makeText(this,c_name+"\n"+lat+"\n"+lng,Toast.LENGTH_SHORT).show();

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        //mMap.addMarker(new MarkerOptions().position(sydney).title(c_name));
        ///mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lng), Double.parseDouble(lat)))
                .title(c_name).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lng), Double.parseDouble(lat)), 8));
    }
}
