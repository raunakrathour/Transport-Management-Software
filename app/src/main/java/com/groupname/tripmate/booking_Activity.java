package com.groupname.tripmate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class booking_Activity extends AppCompatActivity implements bookingAdapter.ItemClicked  {



    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    //text view
    //these are text views on bookingDetail_Frag
    TextView fragment_booking_detail_frag_tvbookingName, fragment_booking_detail_frag_tvbookingNumber, fragment_booking_detail_frag_tvTime1,
            fragment_booking_detail_frag_tvFrom1, fragment_booking_detail_frag_tvTo1,tv1,tv2,tv3, fragment_booking_detail_frag_tvDate;
    ImageView booking_logo;

    booking_list_frag list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

//        new generatebooking();
        //booking_logo = findViewById(R.id.booking_logo);

        fragment_booking_detail_frag_tvbookingName = findViewById(R.id.fragment_booking_detail_frag_tvBookingName);
        fragment_booking_detail_frag_tvDate = findViewById(R.id.fragment_booking_detail_frag_tvDate);
        fragment_booking_detail_frag_tvTime1 = findViewById(R.id.fragment_booking_detail_frag_tvTime1);
        fragment_booking_detail_frag_tvFrom1 = findViewById(R.id.fragment_booking_detail_frag_tvFrom1);
        fragment_booking_detail_frag_tvTo1 = findViewById(R.id.fragment_booking_detail_frag_tvTo1);





        // fragmentManager = this.getSupportFragmentManager();
        //  list = (bookingList_frag)fragmentManager.findFragmentById(R.id.activity_schedule_listfrag);
        //FragmentManager manager=this.getSupportFragmentManager();//it handles fragments

       // if (findViewById(R.id.schedule_potrait) != null)//if phone is in potrait
          //control goes to bookingList_frag
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.activity_booking_list_frag))
                    .hide(manager.findFragmentById(R.id.activity_booking_detail_frag))
                    .commit();

    }

    @Override
    public void onItemClicked(final int index) {
        fragment_booking_detail_frag_tvbookingName.setText("Bus Name- "+FirstClass.bookings.get(index).getBusName());
        fragment_booking_detail_frag_tvDate.setText("Date of booking- "+FirstClass.bookings.get(index).getCreated().toLocaleString());
        fragment_booking_detail_frag_tvTime1.setText(FirstClass.bookings.get(index).getTime());
        fragment_booking_detail_frag_tvFrom1.setText(FirstClass.bookings.get(index).getFrom());
        fragment_booking_detail_frag_tvTo1.setText(FirstClass.bookings.get(index).getTo());



       // if (findViewById(R.id.schedule_potrait) != null) { //if phone is in potrait
            //booking_logo.setVisibility(View.VISIBLE);
            FragmentManager manager = this.getSupportFragmentManager();//this is managing the transactions
            manager.beginTransaction()
                    .hide(manager.findFragmentById(R.id.activity_booking_list_frag))
                    .show(manager.findFragmentById(R.id.activity_booking_detail_frag))
                    .addToBackStack(null)//on clicking back button you will land on previous fragment
                    .commit();//this commits the changes

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
