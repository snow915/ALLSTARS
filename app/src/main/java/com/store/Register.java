package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

import java.sql.SQLOutput;
import java.util.UUID;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sexo;
    EditText nombre, apellido, email, telefono, user, pass;
    Button sign_up;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public Usuarios u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sexo = (Spinner) findViewById(R.id.id_sexo);
        nombre = (EditText) findViewById(R.id.id_nombre);
        apellido = (EditText) findViewById(R.id.id_apellido);
        email = (EditText) findViewById(R.id.id_email);
        telefono = (EditText) findViewById(R.id.id_telefono);
        user = (EditText) findViewById(R.id.id_user);
        pass = (EditText) findViewById(R.id.id_pass);
        sign_up = (Button) findViewById(R.id.id_sign_up);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);
        sexo.setOnItemSelectedListener(this);

        String n = nombre.getText().toString();
        initFirebase();
        u = new Usuarios();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validations()){
                    u.setUid(user.getText().toString());
                    u.setNombre(nombre.getText().toString());
                    u.setApellido(apellido.getText().toString());
                    u.setCorreo(email.getText().toString());
                    u.setTelefono(telefono.getText().toString());
                    u.setUser(user.getText().toString());
                    u.setPass(pass.getText().toString());
                    u.setSexo(sexo.getSelectedItem().toString()+","+sexo.getSelectedItemPosition());
                    user_exist(user.getText().toString());
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void clearInputs(){
        nombre.setText("");
        apellido.setText("");
        email.setText("");
        telefono.setText("");
        user.setText("");
        pass.setText("");
        sexo.setSelection(0);
    }

    private boolean validations(){
        if(nombre.getText().toString().equals("")){
            nombre.setError("Required");
            return false;
        }
        if(apellido.getText().toString().equals("")){
            apellido.setError("Required");
            return false;
        }
        if(email.getText().toString().equals("")){
            email.setError("Required");
            return false;
        }
        if(telefono.getText().toString().equals("")){
            telefono.setError("Required");
            return false;
        }
        if(telefono.getText().toString().length() < 10){
            telefono.setError("Missing numbers");
            return false;
        }
        if(sexo.getSelectedItem().toString().equals("Gender")){
            TextView errorTextview = (TextView) sexo.getSelectedView();
            errorTextview.setError("Your Error Message here");
            return false;
        }
        if(user.getText().toString().equals("")){
            user.setError("Required");
            return false;
        }
        if(user.getText().toString().length() < 4){
            user.setError("4 characters minimum");
            return false;
        }
        if(pass.getText().toString().equals("")){
            pass.setError("Required");
            return false;
        }
        if(pass.getText().toString().length() < 8){
            pass.setError("8 characters minimum");
            return false;
        }

        return true;
    }


    private void user_exist(final String userd) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userd);
        //addListenerForSingleValueEvent hace que onDataChange de Firebase se ejecute 1 sola vez, de lo contrario lo hara 2 veces
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ouch!")
                            .setContentText("TThe user already exists!")
                            .hideConfirmButton()
                            .setCancelText("Ok")
                            .show();
                }
                else {
                    databaseReference.child("Usuarios").child(u.getUid()).setValue(u);
                    clearInputs();
                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Congratulations!")
                            .setContentText("Successful registration!")
                            .setConfirmText("Ok!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent=new Intent(Register.this, MainActivity.class);
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
}
