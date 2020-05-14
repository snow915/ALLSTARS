package com.store.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.store.InfoProducto;
import com.store.R;
import com.store.Servicios;

import java.util.ArrayList;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolderService>{

    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<String> itemPrices = new ArrayList<>();
    private ArrayList<String> itemDescriptions = new ArrayList<>();
    private ArrayList<String> itemMaximumTimes = new ArrayList<>();
    private Context context;

    public AdapterServices(ArrayList<String> itemNames, ArrayList<String> itemPrices, ArrayList<String> itemDescriptions, ArrayList<String> itemMaximumTimes, Context context) {
        this.itemNames = itemNames;
        this.itemPrices = itemPrices;
        this.itemDescriptions = itemDescriptions;
        this.itemMaximumTimes = itemMaximumTimes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ViewHolderService(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderService holder, int position) {
        holder.serviceName.setText(itemNames.get(position));
        final String serviceName = holder.serviceName.getText().toString();
        holder.servicePrice.setText("$"+itemPrices.get(position));
        final String servicePrice = holder.servicePrice.getText().toString();
        holder.serviceDescription.setText(itemDescriptions.get(position));
        final String des = holder.serviceDescription.getText().toString();
        holder.serviceMaximumTime.setText(itemMaximumTimes.get(position));
        final String maxTime = holder.serviceMaximumTime.getText().toString();
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Servicios.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", serviceName);
                intent.putExtra("price", servicePrice);
                intent.putExtra("description", des);
                intent.putExtra("maximum_hours", maxTime);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.itemNames.size();
    }

    public class ViewHolderService extends RecyclerView.ViewHolder {

        ImageView serviceImage;
        TextView serviceName;
        TextView servicePrice;
        TextView serviceDescription;
        TextView serviceMaximumTime;
        CardView card;

        public ViewHolderService(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.item_image);
            serviceName = itemView.findViewById(R.id.item_name);
            servicePrice = itemView.findViewById(R.id.item_price);
            serviceDescription = itemView.findViewById(R.id.item_description);
            serviceMaximumTime = itemView.findViewById(R.id.item_maximum_time);
            card = itemView.findViewById(R.id.item_card);
        }
    }
}
