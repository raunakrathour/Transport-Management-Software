package com.groupname.tripmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Booking_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Booking> bookings;
    /*EditText etDname,etDnumber;
    ArrayList<person> people;
    Button btn;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        recyclerView = findViewById(R.id.bookinglist);
//        etDname=findViewById(R.id.etDname);
//        etDnumber=findViewById(R.id.etDnumber);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookings = new ArrayList<Booking>();
        /*Booking b1 = new Booking();
        b1.setBusName("bus_aa");
        b1.setTime("1:00");
        b1.setBookerName(FirstClass.user.getProperty("name").toString());
        bookings.add(b1);
        Booking b2 = new Booking();
        b2.setBusName("bus_aa");
        b2.setTime("1:00");
        b2.setBookerName("jhndoe");
        bookings.add(b2);
        Booking b3 = new Booking();
        b3.setBusName("bus_aa");
        b3.setTime("1:00");
        b3.setBookerName("johdoe");
        bookings.add(b3);
        myAdapter = new BookingAdapter(Booking_Activity.this, bookings);
        recyclerView.setAdapter(myAdapter);
        */
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        String whereclause ="bookerName = '"+ FirstClass.user.getProperty("name").toString()+"'";
        queryBuilder.setWhereClause(whereclause);
        queryBuilder.addSortBy("created");
//        showProgress(true);
        Backendless.Persistence.of(Booking.class).find(queryBuilder, new AsyncCallback<List<Booking>>() {
            @Override
            public void handleResponse(List<Booking> response) {
                bookings = (ArrayList<Booking>) response;
                if(bookings.size()!=0) {
                    myAdapter = new BookingAdapter(Booking_Activity.this, bookings);
                    recyclerView.setAdapter(myAdapter);
                }
                //myadapter = new busAdapter(getActivity(), FirstClass.busses);
//                 showProgress(false);
                // recyclerView.setAdapter(myadapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Booking_Activity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                // showProgress(false);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Booking_Activity.this,MainActivity.class));
    }


}