package com.store.famous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoSolicitud extends AppCompatActivity implements View.OnClickListener {

    private TextView txtStartDate, txtFinishDate, txtStartTime, txtFinishTime, txtAudienceType,
            txtEventType, txtDetails, txtName, txtNameSevice, txtPriceService;
    private Button btnDeny, btnAccept;
    public HashMap<String, String> hashMapArtist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_solicitud);
        hashMapArtist = (HashMap<String, String>) getIntent().getSerializableExtra("mapValuesArtist");

        associateIds();
        setValuesLayout();

        btnDeny.setOnClickListener(this);
        btnAccept.setOnClickListener(this);

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
                .setTitleText("RECHAZAR")
                .setContentText("¿Realmente quieres rechazar el servicio?")
                .setCancelText("No")
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        //CODE FOR SEND DATA TO FIREBASE

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
        txtStartDate.setText(hashMapArtist.get("startDate"));
        txtFinishDate.setText(hashMapArtist.get("finishDate"));
        txtStartTime.setText(hashMapArtist.get("startTime"));
        txtFinishTime.setText(hashMapArtist.get("finishTime"));
        txtAudienceType.setText(hashMapArtist.get("audienceType"));
        txtEventType.setText(hashMapArtist.get("eventType"));
        txtDetails.setText(hashMapArtist.get("details"));
        txtName.setText(hashMapArtist.get("applicantsFirstname") + " " + hashMapArtist.get("applicantsLastname"));
        txtNameSevice.setText(hashMapArtist.get("nameService"));
        txtPriceService.setText("$" + hashMapArtist.get("priceService"));
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
        btnAccept = findViewById(R.id.aceptar);
        btnDeny = findViewById(R.id.rechazar);
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
