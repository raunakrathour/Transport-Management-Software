package com.groupname.tripmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.Point;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.core.content.ContextCompat;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private boolean isPermissionRequestGranted;
    public static final int PERMISSION_REQUEST_CODE = 9007;
    public static final int GPS_REQUEST_CODE = 9009;
    double LAT =25.43244764680198;
    double LNG = 81.76918306499049;
    Button buttonCurrentLocation;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private TextView tv1;

    Button buttonStartTracking;
    private LocationCallback mlocationCallback;//for regular location update
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "F", Toast.LENGTH_SHORT).show();
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE},6669);
            }

        }
        //buttonStartTracking = findViewById(R.id.btn2_activity_map);
        tv1 = findViewById(R.id.tv1_map_Activity);
        setContentView(R.layout.activity_map);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        initGoogleMap();

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);



        mlocationCallback = new LocationCallback(){//for gettiong location updates
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null)
                {
                    return;
                }
                System.out.println("test6");
                Location location = locationResult.getLastLocation();

                String whereClause = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    whereClause = String.join("", "isRunning = 1");
                }
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause( whereClause );


                //List<Map> foundContacts = Backendless.Data.of("bus").find(queryBuilder);



               // System.out.println("test7");




// Asynchronous API:
// ***********************************************************
              Backendless.Data.of( "bus" ).find( queryBuilder,
                        new AsyncCallback<List<Map>>(){
                            @Override
                            public void handleResponse( List<Map> foundContacts ) {
                              //  System.out.println("test8");
                                // every loaded object from the "Contact" table is now an individual java.util.Map
                                //Map<String, Object> mp = foundContacts.get(0);
                                Toast.makeText( MapActivity.this,"Number of running buses "+foundContacts.size(),Toast.LENGTH_SHORT).show();


                                removeMarkers();
                                addMarker(location.getLatitude(),location.getLongitude(),location.getBearing(),"Your current location","man");
                               // System.out.println("test10");

                               for(int i=0;i<foundContacts.size();i++)
                                {
                                    System.out.println("test "+i);

                                    Point point = (Point) foundContacts.get(i).get("position");
                                    if(point==null)
                                        continue;
                                    double lat=point.getLatitude();
                                    double log = point.getLongitude();
                                    String s = (String) foundContacts.get(i).get("name");



                                    float bearing= (float) 90.0;
                                    if(foundContacts.get(i).get("bearing")!=null)
                                    {




                                        float f = Float.parseFloat(foundContacts.get(i).get("bearing").toString());
                                        bearing=f;


                                    }

                                    addMarker(lat,log,bearing,s,"bus");
                                }
                              //  System.out.println("test11");




                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }


                        });


               // Toast.makeText( MapActivity.this,"Updating locations",Toast.LENGTH_SHORT).show();
               // Log.d("Location","onLocationResult: Location is: " + location.getLatitude()+"\n"+location.getLongitude() );
            }
        };
       /* buttonStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationUpdates();
            }
        });*/
        buttonCurrentLocation = findViewById(R.id.button1);
        buttonCurrentLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                getCurrentLocation();
               // gotoLocation(LAT,LNG);


            }
        });


        //buttonCurrentLocation.setOnClickListener(this::startLocationService);








    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationHelper notificationHelper = new NotificationHelper(MapActivity.this);
        notificationHelper.showNotificationForDriver("You are viewing live buses location");

        getLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MapDebug","OnMapReady: map is showing on the screen");
        mGoogleMap = googleMap;

        //addMarker(LAT,LNG,"IIIT ALLAHABAD");
         //mGoogleMap.setMyLocationEnabled(true);
        //setting initial location
        gotoLocation(LAT,LNG,"no");
    }


    //adding marker
    private void addMarker(double lat,double log,float bearing,String mtitle,String type)
    {

        MarkerOptions markerOptions = new MarkerOptions()
                .title(mtitle)
                .position(new LatLng(lat ,log));


        if(type.equals("bus"))
        {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_on_map));
            markerOptions.rotation(bearing);
        }

        mGoogleMap.addMarker(markerOptions);

    }
    private void removeMarkers()
    {
        mGoogleMap.clear();


    }


    //for getting current device location
    private void getCurrentLocation()
    {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude(),"mylocation");
                addMarker(location.getLatitude(),location.getLongitude(),location.getBearing(),"Your Current Location","man");
            }
            else
            {
                Log.d("Error","getCurrentLocaion: Error"+ task.getException().getMessage());
            }


        });
    }

    //goto particular location
    private void gotoLocation(double lat,double lng,String s)
    {
        CameraUpdate cameraUpdate;
        LatLng latLng = new LatLng(lat,lng);
        if(s=="mylocation")
        {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
        }
        else
        {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,6);
        }

        mGoogleMap.animateCamera(cameraUpdate);
        //various features
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
    }
//getting location update on regular interval

    private void getLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(200);
       /* HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        // Now get the Looper from the HandlerThread
        // NOTE: This call will block until the HandlerThread gets control and initializes its Looper
        Looper looper = handlerThread.getLooper();*/
        // Request location updates to be called back on the HandlerThread
        //locationManager.requestLocationUpdates(provider, minTime, minDistance, listener, looper);
        System.out.println("test9");

        mLocationClient.requestLocationUpdates(locationRequest,mlocationCallback, null);
    }


    //Initializing googlemap Fragment
    private void initGoogleMap()
    {



        if(isGPSEnabled()) {



        }
        if (!isServiceOk()) {

            requestLocationPermission();
        }

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container,supportMapFragment)
                .commit();
        supportMapFragment.getMapAsync(this);
        Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();


    }
    //checking is device gps is enbled or not
    private boolean isGPSEnabled()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabled)
        {
            return true;
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("GPS permission is required for proper working of this app. Please Enable GPS.")
                    .setPositiveButton("Yes",((dialogInterface,i)->{
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent,GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }
        return false;
    }

    //checking do we have location permission or not
    private boolean isServiceOk()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        else
        {
            isPermissionRequestGranted = true;
            return true;
        }
    }
    //request for location permission
    private void requestLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "F", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            }
        }

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSION_REQUEST_CODE &&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            isPermissionRequestGranted = true;
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GPS_REQUEST_CODE)
        {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(providerEnabled)
            {
                Toast.makeText(this,"GPS is Enabled",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"GPS not enabled. Unable to show user location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mlocationCallback!=null)
        {
            //if we need to pause location updates uncomment this
            mLocationClient.removeLocationUpdates(mlocationCallback);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void endTrip(String busNo, Location location)
    {
        //remove everything
        String whereClause = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            whereClause = String.join("", "number = ", "'", busNo, "'");
        }
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );
        final String s = "";//"{\"type\": \"Point\", \"coordinates\""+":"+" [ "+Double.toString(location.getLatitude())+", "+ Double.toString(location.getLongitude())+"]"+ "}";
        final double bearing = location.getBearing();

// Asynchronous API:
// ***********************************************************
        Backendless.Data.of( "bus" ).find( queryBuilder,
                new AsyncCallback<List<Map>>(){
                    @Override
                    public void handleResponse( List<Map> foundContacts ) {
                        // every loaded object from the "Contact" table is now an individual java.util.Map
                        Map<String, Object> mp = foundContacts.get(0);


                        mp.put( "position", s );
                        mp.put("isRunning", 0);
                        mp.put("driver","default");
                        mp.put("bearing","");
                        Backendless.Data.of("bus").save(mp, new AsyncCallback<Map>() {
                            @Override
                            public void handleResponse(Map response) {
                                // Contact objecthas been updated
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                // an error has occurred, the error code can be retrieved with fault.getCode()
                            }

                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }


                });
    }
}
