package com.store.adapters;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.store.R;
import com.store.Servicios;
import java.util.ArrayList;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolderService> {
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
        int id = 0;
        boolean hasCardOnClickAtached = false;
        if (String.valueOf(parent.getContext().getClass()).equals("class com.store.MainActivity")) {
            id = R.layout.view_services_item;
        } else {
            id = R.layout.service_item;
            hasCardOnClickAtached = true;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new ViewHolderService(view, hasCardOnClickAtached);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderService holder, int position) {
        holder.serviceName.setText(itemNames.get(position));
        final String serviceName = holder.serviceName.getText().toString();
        holder.servicePrice.setText("$"+itemPrices.get(position));
        final String servicePrice = holder.servicePrice.getText().toString();
        holder.serviceDescription.setText(itemDescriptions.get(position));
        final String des = holder.serviceDescription.getText().toString();
        holder.serviceMaximumTime.setText(itemMaximumTimes.get(position) + " horas");
        final String maxTime = holder.serviceMaximumTime.getText().toString();
        if (holder.card != null) {
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
        @Nullable CardView card;

        public ViewHolderService(@NonNull View itemView, boolean hasCardOnClickAtached) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.item_image);
            serviceName = itemView.findViewById(R.id.item_name);
            servicePrice = itemView.findViewById(R.id.item_price);
            serviceDescription = itemView.findViewById(R.id.item_description);
            serviceMaximumTime = itemView.findViewById(R.id.item_maximum_time);
            if (hasCardOnClickAtached) {
                card = itemView.findViewById(R.id.item_card);
            } else {
                card = null;
            }
        }
    }
}
