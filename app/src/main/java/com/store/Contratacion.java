package com.store;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Contratacion extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener{

    EditText fechaInicio, fechaFin, horaInicio, horaFin, detalles;
    Spinner tipoPublico, tipoEvento;
    String currentDateString;
    Button maps;
    public int dia, mes, anio, hora, minutos;
    boolean statusFechaInicio = false;
    boolean statusFechaFin = false;
    boolean statusHoraInicio = false;
    boolean statusHoraFin = false;
    HashMap<String, String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratacion);

        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("mapValues");

        fechaInicio = findViewById(R.id.fechaInicio);
        fechaFin = findViewById(R.id.fechaFin);
        horaInicio = findViewById(R.id.horaInicio);
        horaFin = findViewById(R.id.horaFin);
        detalles = findViewById(R.id.idDetalles);
        tipoPublico = findViewById(R.id.idPublico);
        tipoEvento = findViewById(R.id.idEvento);
        maps = findViewById(R.id.idMaps);

        fechaInicio.setOnClickListener(this);
        fechaFin.setOnClickListener(this);

        horaInicio.setOnClickListener(this);
        horaFin.setOnClickListener(this);

        maps.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_publico, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoPublico.setAdapter(adapter);
        tipoPublico.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.array_eventos, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEvento.setAdapter(adapter2);
        tipoEvento.setOnItemSelectedListener(this);

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
        if (statusFechaInicio) {
            fechaInicio.setText(currentDateString);
        } else if (statusFechaFin) {
            fechaFin.setText(currentDateString);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.format("%02d:%02d", hourOfDay, minute);
        if (statusHoraInicio) {
            horaInicio.setText(hour);
        } else if (statusHoraFin) {
            horaFin.setText(hour);
        }
    }

    /*
        ESTO ES LO PRIMERO ENJECUTARSE, DESPUES LO HACE onDateSet
        CUANDO DETECTA QUE CAMPO SE DIO CLICK LAS VARIABLES BOOLEANAS SE CAMBIAN A TRUE
        (PARA SABER QUE CAMPO LE DI CLICK), Y EL OTRO SE CAMBIA A FALSE
        */
    @Override
    public void onClick(View v) {
        if (v == fechaInicio) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            statusFechaInicio = true;
            statusFechaFin = false;
        } else if (v == fechaFin) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            statusFechaFin = true;
            statusFechaInicio = false;
        } else if (v == horaInicio) {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
            statusHoraInicio = true;
            statusHoraFin = false;
        } else if (v == horaFin) {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
            statusHoraFin = true;
            statusHoraInicio = false;
        } else if (v == maps){

            if (validaciones()){
                hashMap.put("tipoPublico", tipoPublico.getSelectedItem().toString());
                hashMap.put("tipoEvento", tipoEvento.getSelectedItem().toString());
                hashMap.put("horaInicio",horaInicio.getText().toString());
                hashMap.put("horaFin", horaFin.getText().toString());
                hashMap.put("fechaInicio", fechaInicio.getText().toString());
                hashMap.put("fechaFin", fechaFin.getText().toString());
                hashMap.put("detalles", detalles.getText().toString());

                Intent intent = new Intent(getApplicationContext(), Map.class);
                intent.putExtra("mapValues", hashMap);
                startActivity(intent);
            }

        }
    }

    private boolean validaciones(){
        String requerido = getString(R.string.requerido);
        TextView errorTextviewPublico = (TextView) tipoPublico.getSelectedView();
        TextView errorTextviewEventos = (TextView) tipoEvento.getSelectedView();

        if(fechaInicio.getText().toString().equals("")){
            fechaInicio.setError(requerido);
            return false;
        } else {
            fechaInicio.setError(requerido, null);
        }
        if(fechaFin.getText().toString().equals("")){
            fechaFin.setError(requerido);
            return false;
        } else {
            fechaFin.setError(requerido, null);
        }
        if(horaInicio.getText().toString().equals("")){
            horaInicio.setError(requerido);
            return false;
        } else {
            horaInicio.setError(requerido, null);
        }
        if(horaFin.getText().toString().equals("")){
            horaFin.setError(requerido);
            return false;
        } else {
            horaFin.setError(requerido, null);
        }
        if(tipoPublico.getSelectedItem().toString().equals("Público")) {
            errorTextviewPublico.setError(requerido);
            return false;
        } else {
            errorTextviewPublico.setError(requerido, null);
        }
        if(tipoEvento.getSelectedItem().toString().equals("Evento")) {
            errorTextviewEventos.setError(requerido);
            return false;
        } else {
            errorTextviewEventos.setError(requerido, null);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
