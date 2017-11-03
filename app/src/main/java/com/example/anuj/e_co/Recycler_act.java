package com.example.anuj.e_co;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Recycler_act extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    public String address, city, state, country, knownName, postalCode, distance;
    public List<Address> addresses;
    LatLng myloc;
    Button Rb, cal;
    int i = 0, rando, j = 0;
    public double latitude, longitude, new_lat, new_lng, calc_dist, val;
    public LatLng latLng;
    Marker marker, j1, j2, j3, i1, i2, i3;
    private static final String TAG = Recycler_act.class.getSimpleName();
    float[] result = new float[10];
    ImageView call;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_act);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final View bottomSheet = findViewById(R.id.bottom_sheet);

        call = findViewById(R.id.call);
        text = findViewById(R.id.txt);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9999847434"));
                if (ActivityCompat.checkSelfPermission(Recycler_act.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i);
            }
        });
        Rb = findViewById(R.id.recyb) ;
        Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    j++;
                    if (j>1){
                        j1.remove();
                        j2.remove();
                        j3.remove();
                        i1.remove();
                        i2.remove();
                        i3.remove();
                    }
                    Random r = new Random();
                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;

                    new_lat=latitude+val;
                    new_lng=longitude+val;
                    // cal_dist(new_lat,new_lng);
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    // calc_dist=distance(latitude,longitude,new_lat,new_lng,n);

                    j1 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .infoWindowAnchor(0.5f, 0.5f)
                            .title("Akash")
                            .snippet(distance+" km away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    ///mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (marker.getTitle().contains("Akash")){
                            bottomSheet.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Hello AK",Toast.LENGTH_SHORT).show();
                        }
                        else
                        if (marker.getTitle().contains("Abhinav")){
                            bottomSheet.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(),"Hello Abhinav",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;
                    new_lat=latitude-val;
                    new_lng=longitude+val;
                    // cal_dist(new_lat,new_lng);
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    // calc_dist=distance(latitude,longitude,new_lat,new_lng,k);

                    j2 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .anchor(0.5f, 0.5f)
                            .title("Abhinav")
                            .snippet(distance +" kms away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;
                    new_lat=latitude-val;
                    new_lng=longitude-val;
                    // cal_dist(new_lat,new_lng);
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    //calc_dist=distance(latitude,longitude,new_lat,new_lng,k);
                    j3 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .anchor(0.5f, 0.5f)
                            .title("Shivam")
                            .snippet(distance+" kms away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;
                    new_lat=latitude+val;
                    new_lng=longitude-val;
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    i1 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .anchor(0.5f, 0.5f)
                            .title("Sasidhar")
                            .snippet(distance+" km away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;
                    new_lat=latitude-val;
                    new_lng=longitude-val;
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    i2 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .anchor(0.5f, 0.5f)
                            .title("Somanath")
                            .snippet(Double.toString(calc_dist)+" km away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    rando = r.nextInt(10 - 1) + 1;
                    val=rando*0.001;
                    new_lat=latitude-val;
                    new_lng=longitude+val;
                    Location.distanceBetween(latitude,longitude,new_lat,new_lng,result);
                    i3 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(new_lat, new_lng))
                            .anchor(0.5f, 0.5f)
                            .title("Mohit")
                            .snippet(Double.toString(calc_dist)+" km away")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));



                }

        });

        gps = new GPSTracker(Recycler_act.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

             latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Geocoder geocoder;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
            // \n is for new line
           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
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

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        // Add a marker in Sydney and move the camera
        myloc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myloc).title(address).snippet(city +", "+state));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 10));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myloc, 16));
            }
        }, 1000);
    }
}
