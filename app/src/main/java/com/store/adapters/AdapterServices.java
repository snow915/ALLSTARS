package com.store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.R;

import java.util.ArrayList;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolderService>{

    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<String> itemPrices = new ArrayList<>();
    private Context context;

    public AdapterServices(ArrayList<String> itemNames, ArrayList<String> itemPrices, Context context) {
        this.itemNames = itemNames;
        this.itemPrices = itemPrices;
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
        holder.servicePrice.setText("$"+itemPrices.get(position));
    }


    @Override
    public int getItemCount() {
        return this.itemNames.size();
    }

    public class ViewHolderService extends RecyclerView.ViewHolder {

        ImageView serviceImage;
        TextView serviceName;
        TextView servicePrice;

        public ViewHolderService(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.item_image);
            serviceName = itemView.findViewById(R.id.item_name);
            servicePrice = itemView.findViewById(R.id.item_price);
        }
    }
}
