package com.store.credentials;

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

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.MainActivity;
import com.store.R;
import com.store.user.Usuarios;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner sex;
    private EditText edtxtFirstName, edtxtLastName, edtxtEmail, edtxtPhone, edtxtUser, edtxtPass;
    private Button btnSignUp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public Usuarios u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sex = findViewById(R.id.id_sexo);
        edtxtFirstName = findViewById(R.id.id_nombre);
        edtxtLastName = findViewById(R.id.id_apellido);
        edtxtEmail = findViewById(R.id.id_email);
        edtxtPhone = findViewById(R.id.id_telefono);
        edtxtUser = findViewById(R.id.id_user);
        edtxtPass = findViewById(R.id.id_pass);
        btnSignUp = findViewById(R.id.id_sign_up);

        //With this i show the Spinner with the values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);
        sex.setOnItemSelectedListener(this);

        initFirebase();
        u = new Usuarios();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validations()){
                    u.setUid(edtxtUser.getText().toString());
                    u.setNombre(edtxtFirstName.getText().toString());
                    u.setApellido(edtxtLastName.getText().toString());
                    u.setCorreo(edtxtEmail.getText().toString());
                    u.setTelefono(edtxtPhone.getText().toString());
                    u.setUser(edtxtUser.getText().toString());
                    u.setPass(edtxtPass.getText().toString());
                    u.setSexo(sex.getSelectedItem().toString()+","+ sex.getSelectedItemPosition());
                    user_exist(edtxtUser.getText().toString());
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
        edtxtFirstName.setText("");
        edtxtLastName.setText("");
        edtxtEmail.setText("");
        edtxtPhone.setText("");
        edtxtUser.setText("");
        edtxtPass.setText("");
        sex.setSelection(0);
    }

    private boolean validations(){
        if(edtxtFirstName.getText().toString().equals("")){
            edtxtFirstName.setError("Required");
            return false;
        }
        if(edtxtLastName.getText().toString().equals("")){
            edtxtLastName.setError("Required");
            return false;
        }
        if(edtxtEmail.getText().toString().equals("")){
            edtxtEmail.setError("Required");
            return false;
        }
        if(edtxtPhone.getText().toString().equals("")){
            edtxtPhone.setError("Required");
            return false;
        }
        if(edtxtPhone.getText().toString().length() < 10){
            edtxtPhone.setError("Missing numbers");
            return false;
        }
        if(sex.getSelectedItem().toString().equals("Gender")){
            TextView errorTextview = (TextView) sex.getSelectedView();
            errorTextview.setError("Your Error Message here");
            return false;
        }
        if(edtxtUser.getText().toString().equals("")){
            edtxtUser.setError("Required");
            return false;
        }
        if(edtxtUser.getText().toString().length() < 4){
            edtxtUser.setError("4 characters minimum");
            return false;
        }
        if(edtxtPass.getText().toString().equals("")){
            edtxtPass.setError("Required");
            return false;
        }
        if(edtxtPass.getText().toString().length() < 8){
            edtxtPass.setError("8 characters minimum");
            return false;
        }
        return true;
    }

    private void user_exist(final String username) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(username);
        //addListenerForSingleValueEvent does that onDataChange of Firebase run 1 time, else run 2 times
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ouch!")
                            .setContentText(username + " ya lo esta usando alguien más")
                            .hideConfirmButton()
                            .setCancelText("Entendido")
                            .show();
                }
                else {
                    databaseReference.child("Usuarios").child(u.getUid()).setValue(u);
                    clearInputs();
                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Felicidades!")
                            .setContentText("Registro exitoso!")
                            .setConfirmText("Perfecto!")
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
