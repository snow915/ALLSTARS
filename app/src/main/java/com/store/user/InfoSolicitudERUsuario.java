package com.store.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.famous.InfoSolicitud;
import com.store.famous.SolicitudAceptada;
import com.store.vo.DatosSolicitudVo;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitudERUsuario extends AppCompatActivity implements View.OnClickListener {

    private TextView title, price, artistFirstName, startDate, finishDate, startTime, finishTime,
    eventType, audienceType, details, status;
    private ImageView image;
    private RatingBar stars;
    private Button pay;
    public HashMap<String, String> hashMapRequest;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatosSolicitudVo datosObj;

    private static final String TAG = "InfoSolicitud";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud_e_r_usuario);
        hashMapRequest = (HashMap<String, String>) getIntent().getSerializableExtra("mapValuesRequest");
        if(hashMapRequest.get("tipoSolicitud").equals("ACCEPTED")){
            new SweetAlertDialog(InfoSolicitudERUsuario.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Felicidades")
                    .setContentText("Tu solicitud fue aceptada, tienes solo 24 horas para realizar el pago correspondiente")
                    .setConfirmText("Entendido")
                    .show();
        }
        associateIDs();
        setValuesLayout();

        pay.setOnClickListener(this);
    }

    private void associateIDs(){
        title = findViewById(R.id.id_titulo);
        price = findViewById(R.id.id_precio);
        artistFirstName = findViewById(R.id.id_nombre_artista);
        startDate = findViewById(R.id.id_fecha_inicio);
        finishDate = findViewById(R.id.id_fecha_fin);
        startTime = findViewById(R.id.id_hora_inicio);
        finishTime = findViewById(R.id.id_hora_fin);
        eventType = findViewById(R.id.id_evento);
        audienceType = findViewById(R.id.id_publico);
        details = findViewById(R.id.id_detalles);
        status = findViewById(R.id.id_status);

        pay = findViewById(R.id.id_pagar);

        image = findViewById(R.id.imagen);

        stars = findViewById(R.id.ratingStars);
    }

    private void setValuesLayout(){
        title.setText(hashMapRequest.get("nombreServicio"));
        price.setText(hashMapRequest.get("precioServicio"));
        artistFirstName.setText(hashMapRequest.get("nombreFamoso"));
        startDate.setText(hashMapRequest.get("fechaInicio"));
        finishDate.setText(hashMapRequest.get("fechaFin"));
        startTime.setText(hashMapRequest.get("horaInicio"));
        finishTime.setText(hashMapRequest.get("horaFin"));
        eventType.setText(hashMapRequest.get("tipoEvento"));
        audienceType.setText(hashMapRequest.get("tipoPublico"));
        details.setText(hashMapRequest.get("detalles"));
        if(hashMapRequest.get("tipoSolicitud").equals("PENDING")){
            status.setText("EN ESPERA");
            status.setBackgroundColor(getResources().getColor(R.color.colorInfo2));
            pay.setVisibility(View.GONE);
            stars.setVisibility(View.GONE);
        } else if(hashMapRequest.get("tipoSolicitud").equals("REJECTED")){
            status.setText("RECHAZADO");
            pay.setVisibility(View.GONE);
            stars.setVisibility(View.GONE);
            status.setBackgroundColor(getResources().getColor(R.color.colorCancel));
        } else if(hashMapRequest.get("tipoSolicitud").equals("DONE")){
            status.setText("EVENTO FINALIZADO");
            pay.setVisibility(View.GONE);
            stars.setVisibility(View.VISIBLE);
            status.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
        } else {
            status.setText("ACEPTADO");
            status.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
            pay.setVisibility(View.VISIBLE);
            stars.setVisibility(View.GONE);
        }

        Glide.with(InfoSolicitudERUsuario.this)
                .load(hashMapRequest.get("imagenFamoso"))
                .fitCenter()
                .centerCrop()
                .into(image);

        //PROCESO DEL PUNTAJE EN EL EVENTO, AUN NO EN EL FAMOSO DIRECTAMENTE
        initFirebase();
        databaseReference = databaseReference.child("solicitudes_terminadas").child(hashMapRequest.get("solicitudID"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ratingEvent = "0";
                    try{
                        ratingEvent = dataSnapshot.child("ratingEvent").getValue().toString();

                    } catch(Exception e){
                        sendRating();
                        ratingEvent = "0";
                    }
                    stars.setRating(Float.parseFloat(ratingEvent));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendRating(){
        final Dialog dialog = new Dialog(InfoSolicitudERUsuario.this);
        dialog.setContentView(R.layout.rating_alert);
        final RatingBar rating = dialog.findViewById(R.id.puntaje);
        Button sendRating = (Button) dialog.findViewById(R.id.enviar_puntaje);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        sendRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String starsNumber = String.valueOf(rating.getRating());
                initFirebase();
                databaseReference.child("solicitudes_terminadas")
                        .child(hashMapRequest.get("solicitudID"))
                        .child("ratingEvent").setValue(Math.round(Float.parseFloat(starsNumber)))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });

    }


    private void setValuesDatosSolicitud(){
        datosObj = new DatosSolicitudVo();
        datosObj.setDetalles(hashMapRequest.get("detalles"));
        datosObj.setFechaFin(hashMapRequest.get("fechaFin"));
        datosObj.setFechaInicio(hashMapRequest.get("fechaInicio"));
        datosObj.setHoraFin(hashMapRequest.get("horaFin"));
        datosObj.setHoraInicio(hashMapRequest.get("horaInicio"));
        datosObj.setLatitud(hashMapRequest.get("latitud"));
        datosObj.setLongitud(hashMapRequest.get("longitud"));
        datosObj.setNombreFamoso(hashMapRequest.get("nombreFamoso"));
        datosObj.setNombreServicio(hashMapRequest.get("nombreServicio"));
        datosObj.setPrecioServicio(hashMapRequest.get("precioServicio"));
        datosObj.setSolicitudID(hashMapRequest.get("solicitudID"));
        datosObj.setTipoEvento(hashMapRequest.get("tipoEvento"));
        datosObj.setTipoPublico(hashMapRequest.get("tipoPublico"));
        datosObj.setUbicacion(hashMapRequest.get("ubicacion"));
        datosObj.setUserFamoso(hashMapRequest.get("userFamoso"));
        datosObj.setUserID(hashMapRequest.get("userID"));
        datosObj.setUserLastname(hashMapRequest.get("userLastname"));
        datosObj.setUserName(hashMapRequest.get("userName"));
        datosObj.setImagenFamoso(hashMapRequest.get("imagenFamoso"));
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void paymentCompleteFirebase(){
        initFirebase();
        setValuesDatosSolicitud();

        //Pasa la solicitud a solicitudes_aceptadas
        databaseReference.child("solicitudes_aceptadas").child("solicitudes_pagadas")
                .child(datosObj.getSolicitudID())
                .setValue(datosObj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                databaseReference.child("solicitudes_aceptadas").child("solicitudes_en_proceso")
                                        .child(datosObj.getSolicitudID())
                                        .setValue(null);

                                new SweetAlertDialog(InfoSolicitudERUsuario.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Pago realizado")
                                        .setConfirmText("Entendido")
                                        .show();
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.id_pagar){
            paymentCompleteFirebase();
        }
    }
}
