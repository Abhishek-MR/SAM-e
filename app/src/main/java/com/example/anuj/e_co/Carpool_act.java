package com.example.anuj.e_co;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuj.e_co.POJO.*;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class Carpool_act extends FragmentActivity implements OnMapReadyCallback, PlaceSelectionListener {

    public View myContentsView;



    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        public TextView tvTitle;


        @Override
        public View getInfoContents(Marker marker) {

            myContentsView = getLayoutInflater().inflate(R.layout.info_win, null);


            tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            final TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private static final String LOG_TAG = "PlaceSelectionListener";

    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setCountry("IN")
            .build();
    private GoogleMap mMap;
    GPSTracker gps;
    public double latitude, longitude, new_lat, new_lng;
    public LatLng latLng;
    public LatLng my_loc;
    public List<Address> addresses;
    public String address, city, state, country, knownName, postalCode;
    int i = 0, rando, j = 0;
    public double val;
    Marker marker, j1, j2, j3, i1, i2, i3;
    Button mylocation, join, initiate, call, msg, req;
    String ret_dis = "", ret_dur = "";
    Polyline line;
    private static final String TAG = Recycler_act.class.getSimpleName();
    TextView dur, dis, disdur;
    Button navigation;
    CardView cardView;
    public String phnum, mhint, rhint,title;
    int key=0;
    String host = "tcp://m12.cloudmqtt.com:11871";
    // String clientId = "ExampleAndroidClient";
    String topic = "sensor/snd";

    String username = "zyekiwpb";
    String password = "z58Alb-SFL-_";

    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String SEEDS = "seed";
    private static final String KEY = "key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_act);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        editor.putInt(KEY,0).apply();


        cardView = findViewById(R.id.li);
        cardView.setVisibility(View.INVISIBLE);
        dur = findViewById(R.id.duration);
        dis = findViewById(R.id.distance);
        navigation = findViewById(R.id.nav);
        call = findViewById(R.id.call);
        msg = findViewById(R.id.msg);
        req = findViewById(R.id.request);

        call.setVisibility(View.GONE);
        msg.setVisibility(View.GONE);
        req.setVisibility(View.GONE);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search your Destination");
        // autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);
        autocompleteFragment.setFilter(typeFilter);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phnum));
                if (ActivityCompat.checkSelfPermission(Carpool_act.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Toast.makeText(getApplicationContext(),"Calling "+title,Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key=1;
                editor.putInt(KEY,1);
                SharedPreferences xy = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                xy.edit().putInt("key", key).apply();
                String message = "rhint";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(Carpool_act.this,Loader_act.class);
                startActivity(i);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Carpool_act.this,Chat_act.class);
                startActivity(j);
            }
        });


        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), host, clientId);

        options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        try {
            token = client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        try {
            token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                  //  Toast.makeText(getApplicationContext(),"Connection successful",Toast.LENGTH_SHORT).show();
                    subscribtion();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                 //   Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //textView.setText(new String(message.getPayload()));
                String msg = new String(message.getPayload());
                if (msg.contains("rhint")) {
                 //  startNotification();
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        initiate = findViewById(R.id.initiateb);
        initiate.setVisibility(View.INVISIBLE);
        initiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_marker();
                i1.setVisible(false);
                i2.setVisible(false);
                i3.setVisible(false);
                j1.setVisible(true);
                j2.setVisible(true);
                j3.setVisible(true);
            }
        });
        join = (Button) findViewById(R.id.joinb);
        join.setVisibility(View.INVISIBLE);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_marker();
                j1.setVisible(false);
                j2.setVisible(false);
                j3.setVisible(false);
                i1.setVisible(true);
                i2.setVisible(true);
                i3.setVisible(true);
            }
        });
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + new_lat + "," + new_lng));
                startActivity(intent);
            }
        });
        mylocation = (Button) findViewById(R.id.my_loc);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pont_myloc();
            }
        });

        gps = new GPSTracker(Carpool_act.this);

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
            Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();


            // \n is for new line
            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onPlaceSelected(Place place) {
        i++;
        cardView.setVisibility(View.VISIBLE);
        initiate.setVisibility(View.VISIBLE);
        join.setVisibility(View.VISIBLE );
        Log.i(LOG_TAG, "Place Selected: " + place.getName());
        latLng = place.getLatLng();
        if (i>1){
            marker.remove();
            remove_marker();
        }
        build_retrofit_and_get_response("driving");
        marker= mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(knownName + address)
                .snippet(city +", "+state));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        }, 1000);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


    }

    private void place_marker() {
        j++;
        if (j>1){
            remove_marker();
        }

        Random r = new Random();
        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;

        new_lat=latitude+val;
        new_lng=longitude+val;

        j1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .infoWindowAnchor(0.5f, 0.5f)
                .title("Akash")
                .snippet("Maruti WagonR")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        ///mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);


        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;
        new_lat=latitude-val;
        new_lng=longitude+val;


        j2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .anchor(0.5f, 0.5f)
                .title("Abhinav")
                .snippet("Hyundai Creta")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;
        new_lat=latitude-val;
        new_lng=longitude-val;
        j3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .anchor(0.5f, 0.5f)
                .title("Shivam")
                .snippet("Ford Figo")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;
        new_lat=latitude+val;
        new_lng=longitude-val;
        i1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .anchor(0.5f, 0.5f)
                .title("Sasidhar")
                .snippet("Honda City")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;
        new_lat=latitude-val;
        new_lng=longitude-val;
        i2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .anchor(0.5f, 0.5f)
                .title("Somanath")
                .snippet("Maruti Baleno")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        rando = r.nextInt(10 - 1) + 1;
        val=rando*0.001;
        new_lat=latitude-val;
        new_lng=longitude+val;
        build_retrofit_and_get_response("driving");
        i3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(new_lat, new_lng))
                .anchor(0.5f, 0.5f)
                .title("Mohit")
                .snippet("Toyota Etios ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                call.setVisibility(View.VISIBLE);
                msg.setVisibility(View.VISIBLE);
                req.setVisibility(View.VISIBLE);
                title =marker.getTitle();
                if (title.contains("Akash")){
                        phnum = "9999847434";
                        mhint = "mhint";
                        rhint = "rhint";
                }
                else if (title.contains("Abhinav")){
                        phnum= "9742706888";
                    mhint = "mhint";
                    rhint = "rhint";

                }
                else if (title.contains("Shivam")){
                        phnum = "9008944908";
                    mhint = "mhint";
                    rhint = "rhint";
                }
                else if (title.contains("Sasidhar")){
                        phnum = "9643997845";
                    mhint = "mhint";
                    rhint = "rhint";

                }
                else if (title.contains("Somanath")){
                        phnum = "9008087755";
                    mhint = "mhint";
                    rhint = "rhint";
                }
                else {
                    phnum = "7259603949";
                    mhint = "mhint";
                    rhint = "rhint";
                }
            }
        });
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }


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

        mMap.getUiSettings().setAllGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        my_loc = new LatLng(latitude, longitude);
        pont_myloc();
    }

    private void pont_myloc() {
        mMap.addMarker(new MarkerOptions().position(my_loc).title(knownName + address).snippet(city +", "+state));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_loc, 10));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, 16));
            }
        }, 1000);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration("metric", latitude + "," + longitude,latLng.latitude + "," + latLng.longitude, type);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                       ret_dis = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        ret_dur = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        dur.setText(ret_dis);
                        dis.setText(ret_dur);
                        //ret_dis = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        //ret_dur = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();



                        //ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(15)
                                //.color(Color)
                                .color(Color.CYAN)
                                .geodesic(true)
                        );
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }



    private void remove_marker(){
        j1.remove();
        j2.remove();
        j3.remove();
        i1.remove();
        i2.remove();
        i3.remove();
    }

    private void subscribtion(){
        try {
            client.subscribe(topic,0);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }






}
