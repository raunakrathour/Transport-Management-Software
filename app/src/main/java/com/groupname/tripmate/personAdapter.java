package com.groupname.tripmate;
//1package com.example.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class personAdapter extends RecyclerView.Adapter<personAdapter.viewHolder>
{
    public interface ItemClicked{
        void onItemClicked(int index);

    }
    private ArrayList<person>people;
    ItemClicked activity;
    public personAdapter(Context context,ArrayList<person> list)
    {
        people = list;
        activity = (ItemClicked) context;
    }
    public class viewHolder extends RecyclerView.ViewHolder
    {
        ImageView plane1;
        TextView tv11,tv12;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            plane1 = itemView.findViewById(R.id.plane1);
            tv11 =   itemView.findViewById(R.id.tv11);
            tv12 =    itemView.findViewById(R.id.tv12);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(people.indexOf((person)v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public personAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_elements,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull personAdapter.viewHolder holder, int position) {
        holder.itemView.setTag(people.get(position));
        holder.tv11.setText(people.get(position).getName());
        holder.tv12.setText(people.get(position).getNumber());
       /* if(people.get(position).getPreference().equals("bus"))
        {
            holder.plane1.setImageResource(R.drawable.bus);
        }
        else
        {
            holder.plane1.setImageResource(R.drawable.plane);
        }*/

    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
