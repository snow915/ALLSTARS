package com.store.famous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ncorti.slidetoact.SlideToActView;
import com.store.MainActivity;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.vo.DatosSolicitudVo;

import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitud extends AppCompatActivity implements View.OnClickListener {

    private TextView txtStartDate, txtFinishDate, txtStartTime, txtFinishTime, txtAudienceType,
            txtEventType, txtDetails, txtName, txtNameSevice, txtPriceService, txtTitleTypeRequest;
    private Button btnDeny, btnAccept;
    public HashMap<String, String> hashMapArtist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatosSolicitudVo datosObj;
    private SlideToActView sta;
    private String statusSlider = "0";
    private boolean viewSlider = false;

    private static final String TAG = "InfoSolicitud";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud);
        hashMapArtist = (HashMap<String, String>) getIntent().getSerializableExtra("mapValuesArtist");

        associateIds();
        setValuesLayout();
        btnDeny.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        if(viewSlider){
            startFinishEvent();
        }
    }

    private void startFinishEvent(){
        initFirebase();
        if(statusSlider.equals("1")){
            sta.setText("Terminar evento");
        }
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                if(statusSlider.equals("0")){
                    sta.resetSlider();
                    sta.setText("Terminar evento");
                    databaseReference.child("solicitudes_aceptadas")
                            .child("solicitudes_pagadas")
                            .child(hashMapArtist.get("solicitudID"))
                            .child("statusEvent").setValue("1");
                } else {
                    new SweetAlertDialog(InfoSolicitud.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Felicidades, completaste el evento")
                            .setConfirmText("Entendido")
                            .show();
                }
            }
        });
    }

    private void acceptHiring(){
        new SweetAlertDialog(InfoSolicitud.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("ACEPTAR")
                .setContentText("¿Realmente quieres aceptar el servicio?")
                .setCancelText("No")
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        //CODE FOR SEND DATA TO FIREBASE
                        initFirebase();
                        setValuesDatosSolicitud();

                        //Pasa la solicitud a solicitudes_aceptadas
                        databaseReference.child("solicitudes_aceptadas").child("solicitudes_en_proceso")
                                .child(datosObj.getSolicitudID())
                                .setValue(datosObj)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //Esto borra la solicitud de solicitudes
                                                databaseReference.child("solicitudes")
                                                        .child(datosObj.getSolicitudID())
                                                        .setValue(null);

                                                Intent intent = new Intent(InfoSolicitud.this, SolicitudAceptada.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            }
                                        });
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

    private void denyHiring(){
        new SweetAlertDialog(InfoSolicitud.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Realmente quieres rechazar el servicio?")
                .setContentText("Esta acción no se puede revertir")
                .setCancelText("No")
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        //CODE FOR SEND DATA TO FIREBASE
                        initFirebase();
                        setValuesDatosSolicitud();
                        //Pasa la solicitud a solicitudes_rechazadas
                        databaseReference.child("solicitudes_rechazadas")
                                .child(datosObj.getSolicitudID())
                                .setValue(datosObj)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //Esto borra la solicitud de solicitudes
                                                databaseReference.child("solicitudes")
                                                        .child(datosObj.getSolicitudID())
                                                        .setValue(null);

                                                Intent intent = new Intent(InfoSolicitud.this, SolicitudRechazada.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            }
                                        });
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

    private void setValuesLayout(){
        txtStartDate.setText(hashMapArtist.get("fechaInicio"));
        txtFinishDate.setText(hashMapArtist.get("fechaFin"));
        txtStartTime.setText(hashMapArtist.get("horaInicio"));
        txtFinishTime.setText(hashMapArtist.get("horaFin"));
        txtAudienceType.setText(hashMapArtist.get("tipoPublico"));
        txtEventType.setText(hashMapArtist.get("tipoEvento"));
        txtDetails.setText(hashMapArtist.get("detalles"));
        txtName.setText(hashMapArtist.get("userName") + " " + hashMapArtist.get("userLastname"));
        txtNameSevice.setText(hashMapArtist.get("nombreServicio"));
        txtPriceService.setText("$" + hashMapArtist.get("precioServicio"));
        if(!hashMapArtist.get("tipoSolicitud").equals("PENDING")){
            btnAccept.setVisibility(View.GONE);
            btnDeny.setVisibility(View.GONE);

            if(hashMapArtist.get("tipoSolicitud").equals("ACCEPTED")){
                txtTitleTypeRequest.setText("Solicitud en proceso");
                sta.setVisibility(View.GONE);
            } else if (hashMapArtist.get("tipoSolicitud").equals("PAYED")){
                txtTitleTypeRequest.setText("Solicitud pagada");
                viewSlider = true;
            } else if (hashMapArtist.get("tipoSolicitud").equals("REJECTED")){
                sta.setVisibility(View.GONE);
                txtTitleTypeRequest.setText("Solicitud rechazada");
            }
        }
    }

    private void setValuesDatosSolicitud(){
        datosObj = new DatosSolicitudVo();
        datosObj.setDetalles(hashMapArtist.get("detalles"));
        datosObj.setFechaFin(hashMapArtist.get("fechaFin"));
        datosObj.setFechaInicio(hashMapArtist.get("fechaInicio"));
        datosObj.setHoraFin(hashMapArtist.get("horaFin"));
        datosObj.setHoraInicio(hashMapArtist.get("horaInicio"));
        datosObj.setLatitud(hashMapArtist.get("latitud"));
        datosObj.setLongitud(hashMapArtist.get("longitud"));
        datosObj.setNombreFamoso(hashMapArtist.get("nombreFamoso"));
        datosObj.setNombreServicio(hashMapArtist.get("nombreServicio"));
        datosObj.setPrecioServicio(hashMapArtist.get("precioServicio"));
        datosObj.setSolicitudID(hashMapArtist.get("solicitudID"));
        datosObj.setTipoEvento(hashMapArtist.get("tipoEvento"));
        datosObj.setTipoPublico(hashMapArtist.get("tipoPublico"));
        datosObj.setUbicacion(hashMapArtist.get("ubicacion"));
        datosObj.setUserFamoso(hashMapArtist.get("userFamoso"));
        datosObj.setUserID(hashMapArtist.get("userID"));
        datosObj.setUserLastname(hashMapArtist.get("userLastname"));
        datosObj.setUserName(hashMapArtist.get("userName"));
        datosObj.setImagenFamoso(hashMapArtist.get("imagenFamoso"));
    }

    private void associateIds(){
        txtStartDate = findViewById(R.id.idFechaInicio);
        txtFinishDate = findViewById(R.id.idFechaFin);
        txtStartTime = findViewById(R.id.idHoraInicio);
        txtFinishTime = findViewById(R.id.idHoraFin);
        txtAudienceType = findViewById(R.id.idTipoPublico);
        txtEventType = findViewById(R.id.idTipoEvento);
        txtDetails = findViewById(R.id.idDetalles);
        txtName = findViewById(R.id.idNombre);
        txtNameSevice = findViewById(R.id.idNombreServicio);
        txtPriceService = findViewById(R.id.idPrecioServicio);
        txtTitleTypeRequest = findViewById(R.id.titulo_tipo_solicitud);
        btnAccept = findViewById(R.id.aceptar);
        btnDeny = findViewById(R.id.rechazar);
        sta = findViewById(R.id.id_slider);
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.aceptar){
            acceptHiring();
        } else if (i == R.id.rechazar){
            denyHiring();
        }
    }
}
