package com.store;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.store.CarritoCompras.AdminSQLiteOpenHelper;
import com.store.credentials.Login;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    String biografiaIntent;
    RatingBar ratingStars;
    String imageRoute;
    String username;
    String userPreferences, artistPreferences;
    int stars;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        load_preferences();
        load_artist_preferences();
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
        biografiaIntent = getIntent().getStringExtra("biografia");
        imageRoute = getIntent().getStringExtra("image");
        stars = getIntent().getIntExtra("stars",0);
        username = getIntent().getStringExtra("username");
        titulo.setText(nombreIntent);
        precio.setText(precioIntent);
        // Here load the image from FirebaseDataBase
        Glide.with(InfoProducto.this)
                .load(imageRoute)
                .fitCenter()
                .centerCrop()
                .into(imagen);
        ratingStars.setRating(stars);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog dialog = makeDialog();
                //dialog.show();
                new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(nombreIntent)
                        .setContentText(biografiaIntent)
                        .show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarFavoritos(v);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPreferences != null) {
                    Intent intent=new Intent(getApplicationContext(), Contratacion.class);
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("rutaImagen", imageRoute);
                    hashMap.put("nombreFamoso", nombreIntent);
                    hashMap.put("usernameFamoso" , username);
                    intent.putExtra("mapValues", hashMap);
                    startActivity(intent);
                } else if (artistPreferences != null) {
                    new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Debes iniciar sesión como usuario normal para poder contratar")
                            .setConfirmText("Entendido!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
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

    public void agregarFavoritos(View v) {
        initFirebase();
        databaseReference.child("Usuarios").child(userPreferences).child("favoritos").child(username).setValue(username);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡Agregado a favoritos!")
                .show();
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void agregarProducto(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "carrito", null, 4);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("tituloProducto",nombreIntent);
        registro.put("precioProducto",precioIntent);
        registro.put("idImagen",imageRoute);
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

    private void load_preferences(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        userPreferences = preferences.getString("user",null);
    }

    private void load_artist_preferences() {
        SharedPreferences preferences = getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        artistPreferences = preferences.getString("artist_id", null);
    }

}
