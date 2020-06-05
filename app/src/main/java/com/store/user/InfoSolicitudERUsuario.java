package com.store.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
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
import com.store.Pay;
import com.store.R;
import com.store.famous.InfoSolicitud;
import com.store.famous.SolicitudAceptada;
import com.store.vo.DatosSolicitudVo;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitudERUsuario extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1234;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    String API_GET_TOKEN="http://52.201.229.104/braintree_allstars/main.php";
    String API_CHECKOUT="http://52.201.229.104/braintree_allstars/checkout.php";
    String token,amount;
    HashMap<String,String> paramsHash;

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

        //PROCESO DEL PUNTAJE
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
        if(!(InfoSolicitudERUsuario.this).isFinishing()) {

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
                    final String starsNumber = String.valueOf(rating.getRating());
                    initFirebase();
                    databaseReference.child("solicitudes_terminadas")
                            .child(hashMapRequest.get("solicitudID"))
                            .child("ratingEvent").setValue(Math.round(Float.parseFloat(starsNumber)));

                    databaseReference.child("data")
                            .child(hashMapRequest.get("userFamoso"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        float avgRating = Float.parseFloat(dataSnapshot.child("puntaje").getValue().toString());
                                        int numberOfRatings = Integer.parseInt(dataSnapshot.child("cantidadCalificaciones").getValue().toString());
                                        numberOfRatings++;
                                        avgRating = (avgRating + Math.round(Float.parseFloat(starsNumber))) / numberOfRatings;
                                        databaseReference.child("data")
                                                .child(hashMapRequest.get("userFamoso"))
                                                .child("cantidadCalificaciones")
                                                .setValue(numberOfRatings);

                                        databaseReference.child("data")
                                                .child(hashMapRequest.get("userFamoso"))
                                                  .child("puntaje")
                                                .setValue(Math.round(avgRating));

                                        dialog.dismiss();
                                        new SweetAlertDialog(InfoSolicitudERUsuario.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Gracias por tu puntuación")
                                                .show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            });
        }

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
            submitPayment();
        }
    }


    /*=======================PAYMENTS FUNCTIONS=======================*/

    private void submitPayment(){
        amount=hashMapRequest.get("precioServicio");
        if(!amount.isEmpty()) {

            DropInRequest dropInRequest=new DropInRequest()
                    .vaultManager(true)
                    .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpGVXpJMU5pSXNJbXRwWkNJNklqSXdNVGd3TkRJMk1UWXRjMkZ1WkdKdmVDSXNJbWx6Y3lJNklrRjFkR2g1SW4wLmV5SmxlSEFpT2pFMU9URTBOakl4TVRZc0ltcDBhU0k2SW1GbE5tUXdOalF4TFdSaU1tRXROR05tWVMwNU1HTmpMVFF6WldZNE4yVm1ORFZtT0NJc0luTjFZaUk2SWpSNmNYaHlPRGQwYURSdGRucHliamtpTENKcGMzTWlPaUpCZFhSb2VTSXNJbTFsY21Ob1lXNTBJanA3SW5CMVlteHBZMTlwWkNJNklqUjZjWGh5T0RkMGFEUnRkbnB5YmpraUxDSjJaWEpwWm5sZlkyRnlaRjlpZVY5a1pXWmhkV3gwSWpwbVlXeHpaWDBzSW5KcFoyaDBjeUk2V3lKdFlXNWhaMlZmZG1GMWJIUWlYU3dpYjNCMGFXOXVjeUk2ZTMxOS5idkV0SGxjRkFPMl9jWkUzYXZYQkc0X0k4c0VXWWRXY0NjamZMb2QteFBhcWtPWThmajgwd2VlbGVsd2NwNXNsejFUM1plMk9pMUdKMjhvLTNtVjNmdyIsImNvbmZpZ1VybCI6Imh0dHBzOi8vYXBpLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy80enF4cjg3dGg0bXZ6cm45L2NsaWVudF9hcGkvdjEvY29uZmlndXJhdGlvbiIsImdyYXBoUUwiOnsidXJsIjoiaHR0cHM6Ly9wYXltZW50cy5zYW5kYm94LmJyYWludHJlZS1hcGkuY29tL2dyYXBocWwiLCJkYXRlIjoiMjAxOC0wNS0wOCIsImZlYXR1cmVzIjpbInRva2VuaXplX2NyZWRpdF9jYXJkcyJdfSwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzR6cXhyODd0aDRtdnpybjkvY2xpZW50X2FwaSIsImVudmlyb25tZW50Ijoic2FuZGJveCIsIm1lcmNoYW50SWQiOiI0enF4cjg3dGg0bXZ6cm45IiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhdXRoVXJsIjoiaHR0cHM6Ly9hdXRoLnZlbm1vLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJ2ZW5tbyI6Im9mZiIsImNoYWxsZW5nZXMiOltdLCJ0aHJlZURTZWN1cmVFbmFibGVkIjp0cnVlLCJhbmFseXRpY3MiOnsidXJsIjoiaHR0cHM6Ly9vcmlnaW4tYW5hbHl0aWNzLXNhbmQuc2FuZGJveC5icmFpbnRyZWUtYXBpLmNvbS80enF4cjg3dGg0bXZ6cm45In0sInBheXBhbEVuYWJsZWQiOnRydWUsInBheXBhbCI6eyJiaWxsaW5nQWdyZWVtZW50c0VuYWJsZWQiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYWxsb3dIdHRwIjp0cnVlLCJkaXNwbGF5TmFtZSI6IkFsbFN0YXJzIiwiY2xpZW50SWQiOm51bGwsInByaXZhY3lVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vcHAiLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3RvcyIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImVudmlyb25tZW50Ijoib2ZmbGluZSIsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsIm1lcmNoYW50QWNjb3VudElkIjoiYWxsc3RhcnMiLCJjdXJyZW5jeUlzb0NvZGUiOiJVU0QifX0=");

            startActivityForResult(dropInRequest.getIntent(getApplicationContext()),REQUEST_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "Enter a valid amount for payment", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPayments(){
        RequestQueue queue= Volley.newRequestQueue(InfoSolicitudERUsuario.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, API_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")){
                            paymentCompleteFirebase();
                        }
                        else {
                            Toast.makeText(InfoSolicitudERUsuario.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Err",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash==null)
                    return null;
                Map<String,String> params=new HashMap<>();
                for(String key:paramsHash.keySet())
                {
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy=new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== REQUEST_CODE){
            if(resultCode==RESULT_OK)
            {
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce= result.getPaymentMethodNonce();
                String strNounce=nonce.getNonce();

                if(!amount.isEmpty()) {
                    Double convertedToDollar = Double.parseDouble(amount) * 0.0463095;
                    amount = String.valueOf(convertedToDollar);
                    paramsHash=new HashMap<>();
                    paramsHash.put("amount",amount);
                    paramsHash.put("nonce",strNounce);

                    sendPayments();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if(resultCode==RESULT_CANCELED) {
                //Toast.makeText(getApplicationContext(), "User canceled", Toast.LENGTH_SHORT).show();
            } else {
                Exception error=(Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Err",error.toString());
            }
        }
    }

}
