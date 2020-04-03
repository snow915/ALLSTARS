package com.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

public class Contratacion extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    EditText fechaInicio, fechaFin, horaInicio, horaFin;
    private int dia, mes, anio, hora, minutos;
    boolean statusFechaInicio = false;
    boolean statusFechaFin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratacion);

        fechaInicio = (EditText) findViewById(R.id.fechaInicio);
        fechaFin = (EditText) findViewById(R.id.fechaFin);
        horaInicio = (EditText) findViewById(R.id.horaInicio);
        horaFin = (EditText) findViewById(R.id.horaFin);

        fechaInicio.setOnClickListener(this);
        fechaFin.setOnClickListener(this);

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
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        if(statusFechaInicio){
            fechaInicio.setText(currentDateString);
        }
        else if(statusFechaFin){
            fechaFin.setText(currentDateString);
        }
    }

    /*
    ESTO ES LO PRIMERO ENJECUTARSE, DESPUES LO HACE onDateSet
    CUANDO DETECTA QUE CAMPO SE DIO CLICK LAS VARIABLES BOOLEANAS SE CAMBIAN A TRUE
    (PARA SABER QUE CAMPO LE DI CLICK), Y EL OTRO SE CAMBIA A FALSE
    */
    @Override
    public void onClick(View v) {
        if(v==fechaInicio){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            statusFechaInicio = true;
            statusFechaFin = false;
        }
        else if (v==fechaFin){
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            statusFechaFin = true;
            statusFechaInicio = false;
        }
    }
}
