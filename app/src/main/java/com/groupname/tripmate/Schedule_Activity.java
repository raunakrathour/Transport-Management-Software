package com.groupname.tripmate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Schedule_Activity extends AppCompatActivity implements busAdapter.ItemClicked {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    //text view
    //these are text views on busDetail_Frag
    TextView fragment_bus_detail_frag_tvBusName, fragment_bus_detail_frag_tvBusNumber, fragment_bus_detail_frag_tvTime1,
            fragment_bus_detail_frag_tvFrom1, fragment_bus_detail_frag_tvTo1;
    ImageView bus_logo, ivDelete, ivAdd;

    busList_frag list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

//        new generateBus();
        bus_logo = findViewById(R.id.bus_logo);
        ivDelete = findViewById(R.id.ivDelete);
        ivAdd = findViewById(R.id.ivAdd);
        fragment_bus_detail_frag_tvBusName = findViewById(R.id.fragment_bus_detail_frag_tvBusName);
        fragment_bus_detail_frag_tvBusNumber = findViewById(R.id.fragment_bus_detail_frag_tvBusNumber);
        fragment_bus_detail_frag_tvTime1 = findViewById(R.id.fragment_bus_detail_frag_tvTime1);
        fragment_bus_detail_frag_tvFrom1 = findViewById(R.id.fragment_bus_detail_frag_tvFrom1);
        fragment_bus_detail_frag_tvTo1 = findViewById(R.id.fragment_bus_detail_frag_tvTo1);

        ivAdd.setVisibility(View.GONE);
        if(FirstClass.user.getProperty("isAdmin").equals("0")) {
            ivDelete.setVisibility(View.GONE);
        }
        else
        {
            ivDelete.setVisibility(View.VISIBLE);
        }

        if(getIntent().hasExtra("activity") && getIntent().getStringExtra("activity").equals("homeFragment")) {
            ivAdd.setVisibility(View.VISIBLE);
        }

        if(getIntent().hasExtra("activity") && getIntent().getStringExtra("activity").equals("homeFragment")&&FirstClass.user.getProperty("isAdmin").equals("1")) {
            ivDelete.setVisibility(View.GONE);
        }

        // fragmentManager = this.getSupportFragmentManager();
        //  list = (busList_frag)fragmentManager.findFragmentById(R.id.activity_schedule_listfrag);
        //FragmentManager manager=this.getSupportFragmentManager();//it handles fragments
        if (findViewById(R.id.schedule_landscape) != null)//if the phone is in landscape mode
        {
            bus_logo.setVisibility(View.GONE);
            FragmentManager manager = this.getSupportFragmentManager();//it handles fragments
            //control goes to busList_frag
            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.activity_schedule_listfrag))//if phone is in landscape //show both the fragments
                    .show(manager.findFragmentById(R.id.activity_schedule_detail_frag))
                    .commit();

            onItemClicked(0);
        }
        if (findViewById(R.id.schedule_potrait) != null)//if phone is in potrait
        {   //control goes to busList_frag
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.activity_schedule_listfrag))
                    .hide(manager.findFragmentById(R.id.activity_schedule_detail_frag))
                    .commit();
        }
    }

    @Override
    public void onItemClicked(final int index) {
        fragment_bus_detail_frag_tvBusName.setText(FirstClass.busses.get(index).getName());
        fragment_bus_detail_frag_tvBusNumber.setText(FirstClass.busses.get(index).getNumber());
        fragment_bus_detail_frag_tvTime1.setText(FirstClass.busses.get(index).getTime());
        fragment_bus_detail_frag_tvFrom1.setText(FirstClass.busses.get(index).getFrom());
        fragment_bus_detail_frag_tvTo1.setText(FirstClass.busses.get(index).getTo());

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Schedule_Activity.this);
                dialog.setMessage("Are you sure you want to delete this bus?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Deleting the bus");

                        Backendless.Persistence.of(bus.class).remove(FirstClass.busses.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                FirstClass.busses.remove(index);
                                Toast.makeText(Schedule_Activity.this, "Bus successfully deleted", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                Schedule_Activity.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(Schedule_Activity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Schedule_Activity.this);
                dialog.setMessage("Are you sure you want to confirm this booking?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Adding the booking");

                        Booking booking = new Booking();
                        booking.setBookerName(FirstClass.user.getProperty("name").toString());
                        booking.setBusName(FirstClass.busses.get(index).getName());
                        booking.setTime(FirstClass.busses.get(index).getTime());

                        Backendless.Persistence.save(booking, new AsyncCallback<Booking>() {
                            @Override
                            public void handleResponse(Booking response) {
                                Toast.makeText(Schedule_Activity.this, "Booking added successfully", Toast.LENGTH_SHORT).show();
                                Schedule_Activity.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(Schedule_Activity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        if (findViewById(R.id.schedule_potrait) != null) { //if phone is in potrait
            bus_logo.setVisibility(View.VISIBLE);
            FragmentManager manager = this.getSupportFragmentManager();//this is managing the transactions
            manager.beginTransaction()
                    .hide(manager.findFragmentById(R.id.activity_schedule_listfrag))
                    .show(manager.findFragmentById(R.id.activity_schedule_detail_frag))
                    .addToBackStack(null)//on clicking back button you will land on previous fragment
                    .commit();//this commits the changes
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
//to make phone works on layout as well as potrait mode we have to make another layout file ie layout-land
//click on layout->new->directory->set name as "layout-land"
//change activity_main id as "potrait_layout"
//copy activity_main -> right click on layout->paste->select directory layout-land
//change land\activity_main id as "land_layout"
//after that see what i have done here
