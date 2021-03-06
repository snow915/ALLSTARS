package com.store;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.adapters.AdapterServices;
import com.store.credentials.Login;
import com.store.global.TimePickerFragment;
import com.store.user.Contratacion;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoProducto extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private TextView moreInfo;
    private Button hire;
    private Button btnFavorites;
    private Button btnServices;
    private TextView txtTitle;
    private ImageView imgImage;
    private String artistName;
    private String biography;
    private RatingBar ratingStars;
    private String imageRoute;
    private String artistUsername;
    private String userID, artistUsernameSession;
    private int stars;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public static Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
        sharedPreferencesApp.loadPreferences();
        userID = sharedPreferencesApp.getUserID();
        artistUsernameSession = sharedPreferencesApp.getArtistUsername();

        moreInfo = findViewById(R.id.detalles);
        hire = findViewById(R.id.comprar);
        btnFavorites = findViewById(R.id.agregar);
        btnServices = findViewById(R.id.precios);
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

        moreInfo.setText(biography);

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID != null){
                    addFavorites(v);
                } else {
                    new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Debes iniciar sesión como usuario normal para poder agregar a favoritos")
                            .setConfirmText("Entendido!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID != null) {
                    Intent intent=new Intent(getApplicationContext(), Contratacion.class);
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("rutaImagen", imageRoute);
                    hashMap.put("nombreFamoso", artistName);
                    hashMap.put("usernameFamoso" , artistUsername);
                    hashMap.put("rutaImagen", imageRoute);
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

        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServices();
            }
        });
    }

    private void getServices() {
        final ArrayList<String> itemNames = new ArrayList<>();
        final ArrayList<String> itemPrices = new ArrayList<>();
        final ArrayList<String> itemDescriptions = new ArrayList<>();
        final ArrayList<String> itemMaximumTimes = new ArrayList<>();
        final ArrayList<String> itemIds = new ArrayList<>();
        initFirebase();
        databaseReference.child("data").child(artistUsername).child("servicios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    itemNames.add(element.child("nombre").getValue().toString());
                    itemPrices.add(element.child("precio").getValue().toString());
                    itemDescriptions.add(element.child("detalles").getValue().toString());
                    itemMaximumTimes.add(element.child("tiempoMaximo").getValue().toString());
                    itemIds.add(element.getValue().toString());
                }
                showServices(itemNames, itemPrices, itemDescriptions, itemMaximumTimes, itemIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showServices(ArrayList itemNames, ArrayList itemPrices, ArrayList itemDescriptions, ArrayList itemMaximumTimes, ArrayList itemIds) {
        dialog = new Dialog(InfoProducto.this);
        // Definimos el layout del dialogo
        dialog.setContentView(R.layout.prices);
        final String CONTEXT = "modal";
        RecyclerView recyclerServices = dialog.findViewById(R.id.services_recycler);
        AdapterServices adapterServices = new AdapterServices(itemNames, itemPrices, itemDescriptions, itemMaximumTimes, itemIds, getApplicationContext(), CONTEXT);
        recyclerServices.setAdapter(adapterServices);
        // Es necesario para el scroll horizontal
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerServices.setLayoutManager(lm);
        dialog.show();

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
        databaseReference.child("Usuarios")
                .child(userID)
                .child("favoritos")
                .child(artistUsername)
                .setValue(artistUsername)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("¡Agregado a favoritos!")
                                    .show();
                        }
                    }
                });
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
