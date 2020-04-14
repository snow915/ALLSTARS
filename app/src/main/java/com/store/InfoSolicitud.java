package com.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

public class InfoSolicitud extends AppCompatActivity {

    TextView txtFechaInicio;
    TextView txtFechaFin;
    TextView txtHoraInicio;
    TextView txtHoraFin;
    TextView txtTipoPublico;
    TextView txtTipoEvento;
    TextView txtDetalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud);

        //Obtenemos la data de FragmentSolicitud
        String fechaInicio = getIntent().getStringExtra("fechaInicio");
        String fechaFin = getIntent().getStringExtra("fechaFin");
        String horaInicio = getIntent().getStringExtra("horaInicio");
        String horaFin = getIntent().getStringExtra("horaFin");
        String tipoPublico = getIntent().getStringExtra("tipoPublico");
        String tipoEvento = getIntent().getStringExtra("tipoEvento");
        String detalles = getIntent().getStringExtra("detalles");

        txtFechaInicio = findViewById(R.id.idFechaInicio);
        txtFechaFin = findViewById(R.id.idFechaFin);
        txtHoraInicio = findViewById(R.id.idHoraInicio);
        txtHoraFin = findViewById(R.id.idHoraFin);
        txtTipoPublico = findViewById(R.id.idTipoPublico);
        txtTipoEvento = findViewById(R.id.idTipoEvento);
        txtDetalles = findViewById(R.id.idDetalles);

        txtFechaInicio.setText(fechaInicio);
        txtFechaFin.setText(fechaFin);
        txtHoraInicio.setText(horaInicio);
        txtHoraFin.setText(horaFin);
        txtTipoPublico.setText(tipoPublico);
        txtTipoEvento.setText(tipoEvento);
        txtDetalles.setText(detalles);
    }

}
