package com.store.user;

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
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.Solicitud;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnviarSolicitud extends AppCompatActivity {

    private TextView title, location, startDate, finishDate, startTime, finishTime, audienceType, eventType, details;
    private ImageView img;
    private Button sendRequest;
    HashMap<String, String> hashMap;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String username, userFirstName, userLastname;
    public Solicitud requestObj;
    private SharedPreferencesApp sharedPreferencesApp;
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

        sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
        sharedPreferencesApp.loadPreferences();
        username = sharedPreferencesApp.getUsername();
        userFirstName = sharedPreferencesApp.getUserFirstName();
        userLastname = sharedPreferencesApp.getUserLastName();

        initFirebase();
        requestObj = new Solicitud();
        //associate variables with ids
        title = findViewById(R.id.titulo);
        location = findViewById(R.id.getUbicacion);
        startDate = findViewById(R.id.getFechaInicio);
        finishDate = findViewById(R.id.getFechaFin);
        startTime = findViewById(R.id.getHoraInicio);
        finishTime = findViewById(R.id.getHoraFin);
        audienceType = findViewById(R.id.getTipoPublico);
        eventType = findViewById(R.id.getTipoEvento);
        details = findViewById(R.id.getDetalles);
        img = findViewById(R.id.imagen);
        sendRequest = findViewById(R.id.enviarSolicitud);

        Glide.with(EnviarSolicitud.this)
                .load(hashMap.get("rutaImagen"))
                .fitCenter()
                .centerCrop()
                .into(img);
        title.setText(hashMap.get("nombreFamoso"));
        location.setText(hashMap.get("ubicacion"));
        startDate.setText(hashMap.get("fechaInicio"));
        finishDate.setText(hashMap.get("fechaFin"));
        startTime.setText(hashMap.get("horaInicio"));
        finishTime.setText(hashMap.get("horaFin"));
        audienceType.setText(hashMap.get("tipoPublico"));
        eventType.setText(hashMap.get("tipoEvento"));
        if(!hashMap.get("detalles").equals("")){
            details.setText(hashMap.get("detalles"));
        }

        sendRequest.setOnClickListener(new View.OnClickListener() {
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
                                requestObj.setNombreFamoso(hashMap.get("nombreFamoso"));
                                requestObj.setFechaInicio(hashMap.get("fechaInicio"));
                                requestObj.setFechaFin(hashMap.get("fechaFin"));
                                requestObj.setHoraInicio(hashMap.get("horaInicio"));
                                requestObj.setHoraFin(hashMap.get("horaFin"));
                                requestObj.setTipoPublico(hashMap.get("tipoPublico"));
                                requestObj.setTipoEvento(hashMap.get("tipoEvento"));
                                requestObj.setUserFamoso(hashMap.get("usernameFamoso"));
                                requestObj.setDetalles(hashMap.get("detalles"));
                                requestObj.setUserName(userFirstName);
                                requestObj.setUserLastname(userLastname);
                                requestObj.setUbicacion(hashMap.get("ubicacion"));
                                requestObj.setLatitud(hashMap.get("latitud"));
                                requestObj.setLongitud(hashMap.get("longitud"));

                                databaseReference.child("data")
                                        .child(requestObj.getUserFamoso())
                                        .child("solicitudes")
                                        .child(username)
                                        .setValue(requestObj);
                                sDialog.dismissWithAnimation();

                                Intent intent = new Intent(getApplicationContext(), SollicitudEnviada.class);
                                startActivity(intent);
                                finishAffinity();
                                finish();
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
