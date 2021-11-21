package com.groupname.tripmate;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class bookingAdapter extends RecyclerView.Adapter<bookingAdapter.viewHolder>
        {

            public interface ItemClicked{
                void onItemClicked(int index);

            }
            private ArrayList<Booking>bookings;
            bookingAdapter.ItemClicked activity;
            public bookingAdapter(Context context, ArrayList<Booking> list)
            {
                bookings = list;
                activity = (bookingAdapter.ItemClicked)context;
            }
            public class viewHolder extends RecyclerView.ViewHolder
            {
                TextView tvbooking_card_design1,tvbooking_card_design2;

                public viewHolder(@NonNull View itemView) {
                    super(itemView);
                    tvbooking_card_design1 = itemView.findViewById(R.id.tvBooking_card_design1);
                    tvbooking_card_design2 = itemView.findViewById(R.id.tvBooking_card_design2);


                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onItemClicked(bookings.indexOf((Booking)v.getTag()));
                        }
                    });
                }
            }
            @NonNull
            @Override
            public bookingAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card_design,parent,false);//attach card to recyclyler view list
                return new bookingAdapter.viewHolder(v);
            }
            //
            @Override
            public void onBindViewHolder(@NonNull bookingAdapter.viewHolder holder, int position) {
                holder.itemView.setTag(bookings.get(position));
                holder.tvbooking_card_design1.setText(bookings.get(position).getBusName());
                holder.tvbooking_card_design2.setText("Date of booking- "+bookings.get(position).getCreated().toLocaleString());



            }

            @Override
            public int getItemCount() {
                return bookings.size();
            }
}
