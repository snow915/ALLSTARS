package com.store.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.store.R;

import java.util.HashMap;

public class InfoSolicitudERUsuario extends AppCompatActivity {

    private TextView title, price, artistFirstName, startDate, finishDate, startTime, finishTime,
    eventType, audienceType, details, status;
    private ImageView image;
    private Button pay;
    public HashMap<String, String> hashMapRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud_e_r_usuario);
        hashMapRequest = (HashMap<String, String>) getIntent().getSerializableExtra("mapValuesRequest");
        associateIDs();
        setValuesLayout();
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
        } else if(hashMapRequest.get("tipoSolicitud").equals("REJECTED")){
            status.setText("RECHAZADO");
            pay.setVisibility(View.GONE);
            status.setBackgroundColor(getResources().getColor(R.color.colorCancel));
        } else {
            status.setText("ACEPTADO");
            status.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
            pay.setVisibility(View.VISIBLE);
        }

        Glide.with(InfoSolicitudERUsuario.this)
                .load(hashMapRequest.get("imagenFamoso"))
                .fitCenter()
                .centerCrop()
                .into(image);
    }
}
