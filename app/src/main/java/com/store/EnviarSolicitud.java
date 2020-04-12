package com.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnviarSolicitud extends AppCompatActivity {

    private TextView titulo, ubicacion, fechaInicio, fechaFin, horaInicio, horaFin, tipoPublico, tipoEvento, detalles;
    private ImageView imagen;
    private Button enviarSolicitud;
    HashMap<String, String> hashMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_solicitud);

        new SweetAlertDialog(this)
                .setTitleText("Verifica que la información sea la correcta")
                .setContentText("Esta información sera enviada al famoso/a y en breve se comunicará contigo")
                .setConfirmText("Entendido")
                .show();

        //get array from Map.java that contains all values from Contratacion.java and Map.java
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("mapValues");

        //associate variables with ids
        titulo = findViewById(R.id.titulo);
        ubicacion = findViewById(R.id.getUbicacion);
        fechaInicio = findViewById(R.id.getFechaInicio);
        fechaFin = findViewById(R.id.getFechaFin);
        horaInicio = findViewById(R.id.getHoraInicio);
        horaFin = findViewById(R.id.getHoraFin);
        tipoPublico = findViewById(R.id.getTipoPublico);
        tipoEvento = findViewById(R.id.getTipoEvento);
        detalles = findViewById(R.id.getDetalles);
        imagen = findViewById(R.id.imagen);
        enviarSolicitud = findViewById(R.id.enviarSolicitud);

        Glide.with(EnviarSolicitud.this)
                .load(hashMap.get("rutaImagen"))
                .fitCenter()
                .centerCrop()
                .into(imagen);
        titulo.setText(hashMap.get("nombreFamoso"));
        ubicacion.setText(hashMap.get("ubicacion"));
        fechaInicio.setText(hashMap.get("fechaInicio"));
        fechaFin.setText(hashMap.get("fechaFin"));
        horaInicio.setText(hashMap.get("horaInicio"));
        horaFin.setText(hashMap.get("horaFin"));
        tipoPublico.setText(hashMap.get("tipoPublico"));
        tipoEvento.setText(hashMap.get("tipoEvento"));
        if(!hashMap.get("detalles").equals("")){
            detalles.setText(hashMap.get("detalles"));
        }

        enviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(EnviarSolicitud.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirmar")
                        .setContentText("La información se enviara")
                        .setConfirmText("Enviar")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                //aqui mando la info a firebase

                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

}
