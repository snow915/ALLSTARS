package com.store.adapters;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.Servicios;
import com.store.SharedPreferencesApp;
import com.store.famous.FragmentDeleteService;
import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolderService> {
    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<String> itemPrices = new ArrayList<>();
    private ArrayList<String> itemDescriptions = new ArrayList<>();
    private ArrayList<String> itemMaximumTimes = new ArrayList<>();
    private ArrayList<String> itemIds = new ArrayList<>();
    private Context context;
    private String stringContext;

    public AdapterServices(ArrayList<String> itemNames, ArrayList<String> itemPrices, ArrayList<String> itemDescriptions, ArrayList<String> itemMaximumTimes, ArrayList<String> itemIds, Context context, String CONTEXT) {
        this.itemNames = itemNames;
        this.itemPrices = itemPrices;
        this.itemDescriptions = itemDescriptions;
        this.itemMaximumTimes = itemMaximumTimes;
        this.itemIds = itemIds;
        this.context = context;
        this.stringContext = CONTEXT;
    }

    @NonNull
    @Override
    public ViewHolderService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int id = 0;
        boolean hasCardOnClickAtached = false;
        boolean hasCardDeleteButton = false;
        if (stringContext.equals("view")) {
            id = R.layout.view_services_item;
        } else if(stringContext.equals("modal")){
            id = R.layout.service_item;
            hasCardOnClickAtached = true;
        } else {
            id = R.layout.delete_services_item;
            hasCardDeleteButton = true;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new ViewHolderService(view, hasCardOnClickAtached, hasCardDeleteButton);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderService holder, int position) {
        final int pos = position;
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
        } else if(holder.btnDeleteService !=null){
            holder.btnDeleteService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Realmente deseas eliminar este servicio?")
                            .setContentText("Esta acción no se puede revertir")
                            .setCancelText("No")
                            .setConfirmText("Sí")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    final String name = itemNames.get(pos);
                                    SharedPreferencesApp preferencesApp = new SharedPreferencesApp(context);
                                    preferencesApp.loadPreferences();
                                    final String username = preferencesApp.getArtistUsername();
                                    DatabaseReference df = FirebaseDatabase.getInstance()
                                            .getReference("data")
                                            .child(username)
                                            .child("servicios");
//                                    df.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            itemNames.remove(pos);
//                                            itemDescriptions.remove(pos);
//                                            itemPrices.remove(pos);
//                                            itemMaximumTimes.remove(pos);
//                                            itemIds.remove(pos);
//                                            notifyDataSetChanged();
//                                            Toast.makeText(context, "Eliminación satisfactoria", Toast.LENGTH_LONG).show();
//                                        }
//                                    });
                                    Query query = df.orderByChild("nombre").equalTo(name);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                ds.getRef().removeValue();
                                            }
                                            Toast.makeText(context, "Eliminación satisfactoria", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

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
        @Nullable Button btnDeleteService;
        @Nullable Button btnUpdateService;

        public ViewHolderService(@NonNull View itemView, boolean hasCardOnClickAtached, boolean hasCardDeleteButton) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.item_image);
            serviceName = itemView.findViewById(R.id.item_name);
            servicePrice = itemView.findViewById(R.id.item_price);
            serviceDescription = itemView.findViewById(R.id.item_description);
            serviceMaximumTime = itemView.findViewById(R.id.item_maximum_time);
            card = null;
            btnDeleteService = null;
            btnUpdateService = null;
            if (hasCardOnClickAtached) {
                card = itemView.findViewById(R.id.item_card);
            } else if(hasCardDeleteButton) {
                btnDeleteService = itemView.findViewById(R.id.btnDeleteService);
            }
//            else {
//                // btnUpdateService = itemView.findViewById(R.id.btnDeleteService);
//            }
        }
    }
}
