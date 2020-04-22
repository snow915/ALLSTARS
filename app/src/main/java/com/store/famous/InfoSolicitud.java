package com.store.famous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.store.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitud extends AppCompatActivity {

    TextView txtFechaInicio;
    TextView txtFechaFin;
    TextView txtHoraInicio;
    TextView txtHoraFin;
    TextView txtTipoPublico;
    TextView txtTipoEvento;
    TextView txtDetalles;
    TextView txtNombre;
    Button rechazar;
    Button aceptar;

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
        String nombreSolicitante = getIntent().getStringExtra("nombreSolicitante");
        String apellidoSolicitante = getIntent().getStringExtra("apellidoSolicitante");

        txtFechaInicio = findViewById(R.id.idFechaInicio);
        txtFechaFin = findViewById(R.id.idFechaFin);
        txtHoraInicio = findViewById(R.id.idHoraInicio);
        txtHoraFin = findViewById(R.id.idHoraFin);
        txtTipoPublico = findViewById(R.id.idTipoPublico);
        txtTipoEvento = findViewById(R.id.idTipoEvento);
        txtDetalles = findViewById(R.id.idDetalles);
        txtNombre = findViewById(R.id.idNombre);
        aceptar = findViewById(R.id.aceptar);
        rechazar = findViewById(R.id.rechazar);

        txtFechaInicio.setText(fechaInicio);
        txtFechaFin.setText(fechaFin);
        txtHoraInicio.setText(horaInicio);
        txtHoraFin.setText(horaFin);
        txtTipoPublico.setText(tipoPublico);
        txtTipoEvento.setText(tipoEvento);
        txtDetalles.setText(detalles);
        txtNombre.setText(nombreSolicitante + " " + apellidoSolicitante);

        rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(InfoSolicitud.this);
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                editText.setHint("$");
                new SweetAlertDialog(InfoSolicitud.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("¿Cuanto vas a cobrar?")
                        .setConfirmText("Aceptar")
                        .setCustomView(editText)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String costo = editText.getText().toString();
                                if(costo.equals("") || costo.equals(null)){
                                    editText.setError("Escribe un monto");
                                } else {
                                    Intent intent = new Intent(InfoSolicitud.this, SolicitudAceptada.class);
                                    startActivity(intent);
                                    finishAffinity();
                                    finish();
                                }

                            }
                        })
                        .show();
            }
        });
    }

}
