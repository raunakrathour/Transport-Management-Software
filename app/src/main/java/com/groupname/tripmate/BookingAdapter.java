package com.groupname.tripmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    public interface ItemClicked{
        void onItemClicked(int index);

    }
    private ArrayList<Booking> bookings;
//    personAdapter.ItemClicked activity;
    public BookingAdapter(Context context, ArrayList<Booking> list)
    {
        bookings = list;
//        activity = (personAdapter.ItemClicked) context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView bookings1;
        TextView tv11,tv12;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookings1 = itemView.findViewById(R.id.ivBooking);
            tv11 =   itemView.findViewById(R.id.tvBookBusName);
            tv12 =    itemView.findViewById(R.id.tvBookTime);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(bookings.indexOf((person)v.getTag()));
                }
            });*/
        }
    }
    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item,parent,false);
        return new BookingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(bookings.get(position));
        holder.tv11.setText(bookings.get(position).getBusName());
        holder.tv12.setText(bookings.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
