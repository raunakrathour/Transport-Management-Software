package com.groupname.tripmate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class busAdapter extends RecyclerView.Adapter<busAdapter.viewHolder>
{
    public interface ItemClicked{
        void onItemClicked(int index);

    }
    private ArrayList<bus>buses;
    ItemClicked activity;
    public busAdapter(Context context, ArrayList<bus> list)
    {
        buses = list;
        activity = (ItemClicked)context;
    }
    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView tvBus_card_design1,tvBus_card_design2;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvBus_card_design1 = itemView.findViewById(R.id.tvBus_card_design1);
            tvBus_card_design2 = itemView.findViewById(R.id.tvBus_card_design2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(buses.indexOf((bus)v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public busAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_card_design,parent,false);//attach card to recyclyler view list
        return new viewHolder(v);
    }
//
    @Override
    public void onBindViewHolder(@NonNull busAdapter.viewHolder holder, int position) {
        holder.itemView.setTag(buses.get(position));
        holder.tvBus_card_design1.setText(buses.get(position).getName());
        holder.tvBus_card_design2.setText(buses.get(position).getFrom()+" to "+buses.get(position).getTo());




    }

    @Override
    public int getItemCount() {
        return buses.size();
    }
}


