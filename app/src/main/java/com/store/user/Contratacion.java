package com.store.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.global.DatePickerFragment;
import com.store.Map;
import com.store.R;
import com.store.global.TimePickerFragment;
import com.store.vo.ServiciosVo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Contratacion extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener{

    private EditText startDate, finishDate, startTime, finishTime, details;
    private Spinner audienceType, eventType, services;
    private String currentDateString;
    private Button maps;
    private boolean statusStartDate = false;
    private boolean statusFinishDate = false;
    private boolean statusStartTime = false;
    private boolean statusFinishTime = false;
    public HashMap<String, String> hashMap;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String serviceName;
    private String servicePrice;

    private static final String TAG = "ContratacionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratacion);

        initFirebase();
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("mapValues");

        startDate = findViewById(R.id.fechaInicio);
        finishDate = findViewById(R.id.fechaFin);
        startTime = findViewById(R.id.horaInicio);
        finishTime = findViewById(R.id.horaFin);
        details = findViewById(R.id.idDetalles);
        audienceType = findViewById(R.id.idPublico);
        eventType = findViewById(R.id.idEvento);
        maps = findViewById(R.id.idMaps);
        services = findViewById(R.id.idServicio);

        startDate.setOnClickListener(this);
        finishDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        finishTime.setOnClickListener(this);
        maps.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_publico, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audienceType.setAdapter(adapter);
        audienceType.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.array_eventos, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(adapter2);
        eventType.setOnItemSelectedListener(this);

        //CODIGO SPINNER SERVICIOS
        final ArrayList<ServiciosVo> listServices = new ArrayList<ServiciosVo>();
        listServices.add(new ServiciosVo("Elige un servicio", ""));
        databaseReference.child("data").child(hashMap.get("usernameFamoso")).child("servicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot element: dataSnapshot.getChildren()){
                    //listServices.add(element.child("nombre").getValue().toString() + " - $" + element.child("precio").getValue().toString());
                    String serviceName = element.child("nombre").getValue().toString();
                    String servicePrice = element.child("precio").getValue().toString();
                    listServices.add(new ServiciosVo(serviceName, servicePrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        ArrayAdapter<ServiciosVo> adapter3 = new ArrayAdapter<ServiciosVo>(this, android.R.layout.simple_spinner_item, listServices);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services.setAdapter(adapter3);
        services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ServiciosVo service = (ServiciosVo) parent.getSelectedItem();
                serviceName = service.getNombreServicio();
                servicePrice = service.getPrecioServicio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //CODIGO SPINNER SERVICIOS

    }

    /*
     * LA FECHA SE SETEA CUANDO ENTRA A ESTA FUNCIÓN, ES POR ESO QUE SE TIENEN LAS VARIABLES
     * statusFechaInicio y statusFechaFin
     * PARA IDENTIFICAR EN QUE CAMPO SE VA A SETEAR LA FECHA
     * */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        if (statusStartDate) {
            startDate.setText(currentDateString);
        } else if (statusFinishDate) {
            finishDate.setText(currentDateString);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.format("%02d:%02d", hourOfDay, minute);
        if (statusStartTime) {
            startTime.setText(hour);
        } else if (statusFinishTime) {
            finishTime.setText(hour);
        }
    }

    /*
        ESTO ES LO PRIMERO ENJECUTARSE, DESPUES LO HACE onDateSet
        CUANDO DETECTA QUE CAMPO SE DIO CLICK LAS VARIABLES BOOLEANAS SE CAMBIAN A TRUE
        (PARA SABER QUE CAMPO LE DI CLICK), Y EL OTRO SE CAMBIA A FALSE
        */
    @Override
    public void onClick(View v) {
        if(v == startDate || v == finishDate){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            //This executes when startDate first is true, then pass to if statement unread finishDate
            statusStartDate = true;
            statusFinishDate = false;
            //This executes when finishDate ir is read
            if(v == finishDate){
                statusFinishDate = true;
                statusStartDate = false;
            }
        } else if (v == startTime || v == finishTime) {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
            statusStartTime = true;
            statusFinishTime = false;
            if(v == finishTime){
                statusFinishTime = true;
                statusStartTime = false;
            }
        } else if (v == maps){
            if (validations()){
                hashMap.put("tipoPublico", audienceType.getSelectedItem().toString());
                hashMap.put("tipoEvento", eventType.getSelectedItem().toString());
                hashMap.put("horaInicio", startTime.getText().toString());
                hashMap.put("horaFin", finishTime.getText().toString());
                hashMap.put("fechaInicio", startDate.getText().toString());
                hashMap.put("fechaFin", finishDate.getText().toString());
                hashMap.put("detalles", details.getText().toString());
                hashMap.put("nombreServicio", serviceName);
                hashMap.put("precioServicio", servicePrice);

                Intent intent = new Intent(getApplicationContext(), Map.class);
                intent.putExtra("mapValues", hashMap);
                startActivity(intent);
            }
        }
    }

    private boolean validations(){
        String required = getString(R.string.requerido);
        TextView errorTextviewAudience = (TextView) audienceType.getSelectedView();
        TextView errorTextviewEvents = (TextView) eventType.getSelectedView();
        TextView errorTextviewServices = (TextView) services.getSelectedView();

        if(startDate.getText().toString().equals("")){
            startDate.setError(required);
            return false;
        } else {
            startDate.setError(required, null);
        }
        if(finishDate.getText().toString().equals("")){
            finishDate.setError(required);
            return false;
        } else {
            finishDate.setError(required, null);
        }
        if(startTime.getText().toString().equals("")){
            startTime.setError(required);
            return false;
        } else {
            startTime.setError(required, null);
        }
        if(finishTime.getText().toString().equals("")){
            finishTime.setError(required);
            return false;
        } else {
            finishTime.setError(required, null);
        }
        if(audienceType.getSelectedItem().toString().equals("Público")) {
            errorTextviewAudience.setError(required);
            return false;
        } else {
            errorTextviewAudience.setError(required, null);
        }
        if(eventType.getSelectedItem().toString().equals("Evento")) {
            errorTextviewEvents.setError(required);
            return false;
        }
        if(services.getSelectedItem().toString().equals("Elige un servicio")){
            errorTextviewServices.setError(required);
            return false;
        } else {
            errorTextviewEvents.setError(required, null);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
