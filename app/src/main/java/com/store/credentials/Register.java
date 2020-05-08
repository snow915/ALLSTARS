package com.store.credentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private EditText edtxtFirstName, edtxtLastName, edtxtEmail, edtxtPhone, edtxtPass;
    private Button btnSignUp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public Usuarios u;
    //private static final String TAG = "Register";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sex = findViewById(R.id.id_sexo);
        edtxtFirstName = findViewById(R.id.id_nombre);
        edtxtLastName = findViewById(R.id.id_apellido);
        edtxtEmail = findViewById(R.id.id_email);
        edtxtPhone = findViewById(R.id.id_telefono);
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtxtEmail.getText().toString();
                String password = edtxtPass.getText().toString();
                String emailConverted = email.replace('@', '-').replace('.', '_');

                if(validations()){
                    u.setUid(emailConverted);
                    u.setNombre(edtxtFirstName.getText().toString());
                    u.setApellido(edtxtLastName.getText().toString());
                    u.setCorreo(edtxtEmail.getText().toString());
                    u.setTelefono(edtxtPhone.getText().toString());
                    u.setPass(edtxtPass.getText().toString());
                    u.setSexo(sex.getSelectedItem().toString()+","+ sex.getSelectedItemPosition());
                    user_exist(emailConverted, email, password);
                }
            }
        });

    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //AQUI OCURRE EL REGISTRO
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {

                            new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Ouch!")
                                    .setContentText("Ocurrio un error inesperado, intenta de nuevo más tarde")
                                    .hideConfirmButton()
                                    .setCancelText("Entendido")
                                    .show();
                            //updateUI(null);
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

    private void user_exist(final String username, final String email, final String password) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(username);
        //addListenerForSingleValueEvent does that onDataChange of Firebase run 1 time, else run 2 times
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Ya hay una persona registrada con este correo")
                            .hideConfirmButton()
                            .setCancelText("Entendido")
                            .show();
                }
                else {
                    createUser(email, password);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
