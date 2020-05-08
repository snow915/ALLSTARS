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

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitud extends AppCompatActivity {

    private TextView txtStartDate, txtFinishDate, txtStartTime, txtFinishTime, txtAudienceType,
            txtEventType, txtDetails, txtName;
    private Button btnDeny, btnAccept;
    public HashMap<String, String> hashMapArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud);

        hashMapArtist = (HashMap<String, String>) getIntent().getSerializableExtra("mapValuesArtist");

        txtStartDate = findViewById(R.id.idFechaInicio);
        txtFinishDate = findViewById(R.id.idFechaFin);
        txtStartTime = findViewById(R.id.idHoraInicio);
        txtFinishTime = findViewById(R.id.idHoraFin);
        txtAudienceType = findViewById(R.id.idTipoPublico);
        txtEventType = findViewById(R.id.idTipoEvento);
        txtDetails = findViewById(R.id.idDetalles);
        txtName = findViewById(R.id.idNombre);
        btnAccept = findViewById(R.id.aceptar);
        btnDeny = findViewById(R.id.rechazar);

        txtStartDate.setText(hashMapArtist.get("startDate"));
        txtFinishDate.setText(hashMapArtist.get("finishDate"));
        txtStartTime.setText(hashMapArtist.get("startTime"));
        txtFinishTime.setText(hashMapArtist.get("finishTime"));
        txtAudienceType.setText(hashMapArtist.get("audienceType"));
        txtEventType.setText(hashMapArtist.get("eventType"));
        txtDetails.setText(hashMapArtist.get("details"));
        txtName.setText(hashMapArtist.get("applicantsFirstname") + " " + hashMapArtist.get("applicantsLastname"));

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputCost = new EditText(InfoSolicitud.this);
                inputCost.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                inputCost.setHint("$");
                new SweetAlertDialog(InfoSolicitud.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("¿Cuanto vas a cobrar?")
                        .setConfirmText("Aceptar")
                        .setCustomView(inputCost)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String cost = inputCost.getText().toString();
                                if (cost.equals("") || cost.equals(null)) {
                                    inputCost.setError("Escribe un monto");
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
