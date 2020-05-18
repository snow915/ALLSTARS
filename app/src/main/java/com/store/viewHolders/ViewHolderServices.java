package com.store.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.store.R;

public class ViewHolderServices extends RecyclerView.ViewHolder {

    public TextView serviceName;
    public TextView servicePrice;
    public TextView serviceDescription;
    public TextView serviceMaximumTime;
    public Button btnDeleteService;

    public ViewHolderServices(@NonNull View itemView) {
        super(itemView);
        serviceName = itemView.findViewById(R.id.item_name);
        servicePrice = itemView.findViewById(R.id.item_price);
        serviceDescription = itemView.findViewById(R.id.item_description);
        serviceMaximumTime = itemView.findViewById(R.id.item_maximum_time);
        btnDeleteService = itemView.findViewById(R.id.btnDeleteService);
    }
}