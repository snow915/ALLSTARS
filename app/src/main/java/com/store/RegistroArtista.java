package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroArtista extends AppCompatActivity {
    EditText artist_name;
    String categories;
    ArrayList values;
    EditText user;
    EditText pass;
    Button register_artist;
    ConstraintLayout my_layout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public Artistas a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_artista);
        initFirebase();
        a = new Artistas();
        artist_name = findViewById(R.id.id_nombre);
        categories = "";
        values = new ArrayList<String>();
        user = findViewById(R.id.id_user);
        pass = findViewById(R.id.id_pass);
        my_layout = findViewById(R.id.my_layout);
        register_artist = findViewById(R.id.id_sign_up_as_artist);
        register_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList results = validations(my_layout, values, user, pass, artist_name);
                if (results.size() > 0) {
                    for (int i = 0; i < results.size(); i++) {
                        categories+=results.get(i).toString()+ ", ";
                    }
                    a.setUser(user.getText().toString());
                    a.setNombre(artist_name.getText().toString());
                    a.setPass(pass.getText().toString());
                    a.setBiografia("");
                    a.setCategoria(categories);
                    a.setImagen("");
                    a.setPuntaje(0);
                    user_exist(user.getText().toString());
                    results.clear();
                    categories = "";

                } else {
                    new SweetAlertDialog(RegistroArtista.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh vaya, faltan algunos datos!")
                            .setContentText("Verifica que los datos sean válidos")
                            .hideConfirmButton()
                            .setCancelText("Ok")
                            .show();
                }
            }
        });
    }
    public ArrayList validations(ConstraintLayout ml, ArrayList vals, EditText username, EditText password, EditText artist_name) {
        if(artist_name.getText().toString().equals("")){
            artist_name.setError("Required");
            return vals;
        }
        if(artist_name.getText().toString().length() < 5){
            artist_name.setError("5 characters minimum");
            return vals;
        }
        if(username.getText().toString().equals("")){
            username.setError("Required");
            return vals;
        }
        if(username.getText().toString().length() < 4){
            username.setError("4 characters minimum");
            return vals;
        }
        if(password.getText().toString().equals("")){
            password.setError("Required");
            return vals;
        }
        if(password.getText().toString().length() < 8){
            password.setError("8 characters minimum");
            return vals;
        }
        int total = ml.getChildCount();
        for (int i = 0 ; i<total; i++) {
            View v = ml.getChildAt(i);
            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                String element = ((CheckBox) v).getText().toString();
                vals.add(element);
            }
        }
        return vals;
    }
    private void user_exist(final String userd) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("data").child(userd);
        //addListenerForSingleValueEvent hace que onDataChange de Firebase se ejecute 1 sola vez, de lo contrario lo hara 2 veces
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    new SweetAlertDialog(RegistroArtista.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ouch!")
                            .setContentText("The user already exists!")
                            .hideConfirmButton()
                            .setCancelText("Ok")
                            .show();
                }
                else {
                    databaseReference.child("data").child(a.getUser()).setValue(a);
                    new SweetAlertDialog(RegistroArtista.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Congratulations!")
                            .setContentText("Successful registration!")
                            .setConfirmText("Ok!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent=new Intent(RegistroArtista.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
