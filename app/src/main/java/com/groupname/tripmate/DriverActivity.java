package com.groupname.tripmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.Point;

import java.util.List;
import java.util.Map;

public class DriverActivity extends AppCompatActivity {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btn1;
    Button btn2;
    TextView tv1,tv2;
    EditText et1;
    int flag = 0;
    public static int fg;
    public static String busNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataExistInTable("bus","NULL");
        setContentView(R.layout.activity_driver);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        btn1 = findViewById(R.id.activity_driver_btn1);
        btn2 = findViewById(R.id.activity_driver_btn2);
        btn1.setVisibility(View.GONE);

        tv1 = findViewById(R.id.activity_driver_tv1);
        tv2 = findViewById(R.id.activity_driver_tv2);

        tv1.setVisibility(View.GONE);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et1.getText().toString().isEmpty()) {
                    Toast.makeText(DriverActivity.this, "Please enter the BusNumber", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String str = et1.getText().toString().toUpperCase();
                    if(FirstClass.busTable==null||FirstClass.busTable.size()==0)
                    {

                        System.out.println("temp1");
                        Toast.makeText(DriverActivity.this,"buses not loaded",Toast.LENGTH_SHORT);
                    }
                    else {
                        int tempflag=0;

                        for (int i = 0; i < FirstClass.busTable.size(); i++) {
                            if(FirstClass.busTable.get(i).get("number").equals(str))
                            {
                                tempflag=1;
                                break;
                            }


                        }

                        if(tempflag==0)
                        {
                            tv2.setTextColor(getColor(R.color.red));
                            tv2.setText("Bus number incorrect! Please enter correct bus number");
                            Toast.makeText(DriverActivity.this,"Please Enter Correct Bus Number",Toast.LENGTH_SHORT);

                        }
                        else
                        {
                            tv2.setText("Enter bus number to start trip");
                            tv2.setTextColor(getColor(R.color.white));
                            System.out.println("temp4");
                            btn1.setVisibility(View.VISIBLE);
                            btn2.setVisibility(View.GONE);
                            et1.setVisibility(View.GONE);
                            tv2.setText("Bus No "+str);
                            tv1.setVisibility(View.VISIBLE);

                        }
                    }


                }



            }
        });
        et1 = findViewById(R.id.activity_driver_eT1);
        if (FirstClass.DRIVER_RUNNING_FLAG == 1) {
            tv1.setText("Trip Started");
            tv1.setTextColor(getResources().getColor(R.color.green));
            btn1.setText("End Trip");

        } else {
            tv1.setText("Trip Not Started");
            tv1.setTextColor(getResources().getColor(R.color.red));
            btn1.setText("Start Trip");
        }

        if(isGPSEnabled()) {



        }


        requestLocationPermission();



        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (FirstClass.DRIVER_RUNNING_FLAG == 0) {

                            busNumber = et1.getText().toString().toUpperCase();
                            tv1.setText("Trip Started");
                            tv1.setTextColor(getResources().getColor(R.color.green));
                            btn1.setText("End Trip");
                            FirstClass.DRIVER_RUNNING_FLAG = 1;
                            startLocationService(view);

                } else {
                    et1.setVisibility(View.VISIBLE);
                    tv1.setText("Trip Not Started");
                    tv2.setText("Enter bus number to start trip");
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.GONE);
                    tv1.setTextColor(getResources().getColor(R.color.red));
                    btn1.setText("Start Trip");
                    FirstClass.DRIVER_RUNNING_FLAG = 0;
                    endTrip(busNumber,null);


                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv2.setText("Enter bus number to start trip");
        if(FirstClass.DRIVER_RUNNING_FLAG==1)
        {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.GONE);
            et1.setVisibility(View.GONE);
            tv2.setText("Bus No "+busNumber);
            btn1.setText("End Trip");
            tv1.setText("Trip Started");
            tv1.setTextColor(getColor(R.color.green));
            tv1.setVisibility(View.VISIBLE);

        }

    }

    //start background Location Updates
    private void startLocationService(View view) {
        Intent intent = new Intent(this, MyLocationService.class);
        ContextCompat.startForegroundService(this, intent);
        Toast.makeText(this, "Location Sharing Started", Toast.LENGTH_SHORT).show();
    }

    //end backgroup location Updates
    private void stopLocationService() {
        Intent intent = new Intent(this, MyLocationService.class);
        stopService(intent);
        Toast.makeText(this, "Location Sharing Stopped", Toast.LENGTH_SHORT).show();

    }

    public void isDataExistInTable(String tableName, final String data) {

        String whereClause = null;
        DriverActivity.fg = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            whereClause = String.join("", "number != ", "'", data, "'");
        }
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);





// Asynchronous API:
// ***********************************************************
        Backendless.Data.of("bus").find(queryBuilder,
                new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> foundContacts) {
                        FirstClass.busTable = foundContacts;

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(DriverActivity.this,fault.getMessage(),Toast.LENGTH_SHORT);

                    }


                });





    }
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
                        startActivityForResult(intent,5001);
                    }))
                    .setCancelable(false)
                    .show();

        }
        return false;
    }
    private void requestLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "F", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1001);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1001 &&grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            //isPermissionRequestGranted = true;
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
        if(requestCode==5001)
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
        //final double bearing = location.getBearing();
        System.out.println("test0");

// Asynchronous API:
// ***********************************************************
        Backendless.Data.of( "bus" ).find( queryBuilder,
                new AsyncCallback<List<Map>>(){
                    @Override
                    public void handleResponse( List<Map> foundContacts ) {
                        // every loaded object from the "Contact" table is now an individual java.util.Map
                        Map<String, Object> mp = foundContacts.get(0);

                        System.out.println("test1");
                        Point p=null;


                        mp.put( "position", p );
                        mp.put("isRunning", 0);
                        mp.put("driver","default");
                        mp.put("bearing",null);
                        Backendless.Data.of("bus").save(mp, new AsyncCallback<Map>() {
                            @Override
                            public void handleResponse(Map response) {
                                // Contact objecthas been updated
                                System.out.println("test2");
                                stopLocationService();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                System.out.println("test3"+fault.getMessage()+" "+fault.getCode());

                                // an error has occurred, the error code can be retrieved with fault.getCode()
                            }

                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        System.out.println("test4");
                    }


                });
    }


}



