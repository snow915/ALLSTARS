package com.store;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import com.store.CarritoCompras.AdminSQLiteOpenHelper;

import java.text.DateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoProducto extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    Button moreInfo;
    Button buy;
    Button add;
    Button precios;
    TextView titulo;
    TextView precio;
    ImageView imagen;
    String nombreIntent;
    String precioIntent;
    RatingBar ratingStars;
    int image;
    int stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);
        moreInfo = findViewById(R.id.detalles);
        buy = findViewById(R.id.comprar);
        add = findViewById(R.id.agregar);
        precios = findViewById(R.id.precios);
        titulo = findViewById(R.id.titulo);
        precio = findViewById(R.id.precio);
        imagen = findViewById(R.id.imagen);
        ratingStars = findViewById(R.id.ratingStars);

        nombreIntent = getIntent().getStringExtra("nombre");
        precioIntent = getIntent().getStringExtra("precio");
        image = getIntent().getIntExtra("image",0);
        stars = getIntent().getIntExtra("stars",0);
        titulo.setText(nombreIntent);
        precio.setText(precioIntent);
        imagen.setImageResource(image);
        ratingStars.setRating(stars);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog dialog = makeDialog();
                //dialog.show();
                new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(nombreIntent)
                        .setContentText("Texto de su biografía.")
                        .show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarProducto(v);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        precios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = tablePrices();
                dialog.show();
            }
        });
    }

    public AlertDialog tablePrices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.prices);
        return builder.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        //Iniciamos para la hora
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    public void agregarProducto(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "carrito", null, 4);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("tituloProducto",nombreIntent);
        registro.put("precioProducto",precioIntent);
        registro.put("idImagen",image);
        registro.put("idStars", stars);
        db.insert("Carrito",null, registro);
        db.close();
        Toast.makeText(this,"Agregado a favoritos",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public AlertDialog makeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.details_message);
        return builder.create();
    }

}
