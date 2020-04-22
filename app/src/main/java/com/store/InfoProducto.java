package com.store;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.store.credentials.Login;
import com.store.global.TimePickerFragment;
import com.store.user.Contratacion;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoProducto extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Button moreInfo;
    private Button hire;
    private Button btnFavorites;
    private Button btnPrices;
    private TextView txtTitle;
    private ImageView imgImage;
    private String artistName;
    private String biography;
    private RatingBar ratingStars;
    private String imageRoute;
    private String artistUsername;
    private String usernameSession, artistUsernameSession;
    private int stars;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
        sharedPreferencesApp.loadPreferences();
        usernameSession = sharedPreferencesApp.getUsername();
        artistUsernameSession = sharedPreferencesApp.getArtistUsername();

        moreInfo = findViewById(R.id.detalles);
        hire = findViewById(R.id.comprar);
        btnFavorites = findViewById(R.id.agregar);
        btnPrices = findViewById(R.id.precios);
        txtTitle = findViewById(R.id.titulo);
        imgImage = findViewById(R.id.imagen);
        ratingStars = findViewById(R.id.ratingStars);

        artistName = getIntent().getStringExtra("name");
        biography = getIntent().getStringExtra("biography");
        imageRoute = getIntent().getStringExtra("image");
        stars = getIntent().getIntExtra("stars",0);
        artistUsername = getIntent().getStringExtra("username");

        txtTitle.setText(artistName);
        // Here load the image from FirebaseDataBase
        Glide.with(InfoProducto.this)
                .load(imageRoute)
                .fitCenter()
                .centerCrop()
                .into(imgImage);
        ratingStars.setRating(stars);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(artistName)
                        .setContentText(biography)
                        .show();
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorites(v);
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameSession != null) {
                    Intent intent=new Intent(getApplicationContext(), Contratacion.class);
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("rutaImagen", imageRoute);
                    hashMap.put("nombreFamoso", artistName);
                    hashMap.put("usernameFamoso" , artistUsername);
                    intent.putExtra("mapValues", hashMap);
                    startActivity(intent);
                } else if (artistUsernameSession != null) {
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

        btnPrices.setOnClickListener(new View.OnClickListener() {
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

    public void addFavorites(View v) {
        initFirebase();
        databaseReference.child("Usuarios").child(usernameSession).child("favoritos").child(artistUsername).setValue(artistUsername);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡Agregado a favoritos!")
                .show();
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
