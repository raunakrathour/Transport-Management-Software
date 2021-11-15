package com.groupname.tripmate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLocationService extends Service {
    private static final String TAG = MyLocationService.class.getSimpleName();
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mlocationCallback;
    private int k=0;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);



        mlocationCallback = new LocationCallback(){//for gettiong location updates
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null)
                {
                    return;
                }
                Log.d("Error","getCurrentLocaion: Error "+ k);
                k++;
                Location location = locationResult.getLastLocation();
               // NotificationHelper notificationHelper = new NotificationHelper(MyLocationService.this);
               // notificationHelper.showNotificationForDriver("Your location is Updating");
                FirstClass.DRIVER_RUNNING_FLAG=1;
                updateBusLocation(DriverActivity.busNumber,location);


                //Toast.makeText(MyLocationService.this,location.getLatitude()+"\n"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                Log.d("Location","onLocationResult: Location is: " + location.getLatitude()+"\n"+location.getLongitude() );
            }
        };

    }

    public MyLocationService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,getNotification());
        getLocationUpdates();
        return START_STICKY;
    }
    private Notification getNotification()
    {
        /*NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),
                App.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("TripMate")
                .setContentText("Location Service is running in the background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);*/
        Intent notificationIntent = new Intent(this, DriverActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notificationBuilder = new NotificationCompat.Builder(this,FirstClass.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Your are Sharing location with tripmate")
                .setContentText("Click here to enter and end trip")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(5)
                .setContentIntent(pendingIntent)
                .build();

        return notificationBuilder;

    }



    private void getLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(200);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            stopForeground(true);
            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest,mlocationCallback, Looper.myLooper());
    }





    public void updateBusLocation(String busNo, Location location)
    {
        String whereClause = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            whereClause = String.join("", "number = ", "'", busNo, "'");
        }
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause( whereClause );
        final String s = "{\"type\": \"Point\", \"coordinates\""+":"+" [ "+Double.toString(location.getLongitude())+", "+ Double.toString(location.getLatitude())+"]"+ "}";
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
                        mp.put("isRunning", 1);
                        mp.put("driver",FirstClass.user.getProperty("name").toString());
                        mp.put("bearing",bearing);
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("good");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //endTrip(DriverActivity.busNumber,null);
        stopForeground(true);
       // endTrip(DriverActivity.busNumber,null);
        System.out.println("goodoo");

        mLocationClient.removeLocationUpdates(mlocationCallback);

    }
}
