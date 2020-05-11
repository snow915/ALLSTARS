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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.store.MainActivity;
import com.store.R;
import com.store.user.Usuarios;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinSex;
    private EditText edtxtFirstName, edtxtLastName, edtxtEmail, edtxtPhone, edtxtPass;
    private Button btnSignUp, btnSignUpArtist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public Usuarios usersObj;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinSex = findViewById(R.id.id_sexo);
        edtxtFirstName = findViewById(R.id.id_nombre);
        edtxtLastName = findViewById(R.id.id_apellido);
        edtxtEmail = findViewById(R.id.id_email);
        edtxtPhone = findViewById(R.id.id_telefono);
        edtxtPass = findViewById(R.id.id_pass);
        btnSignUp = findViewById(R.id.id_sign_up);
        btnSignUpArtist = findViewById(R.id.id_sign_up_artist);

        //With this i show the Spinner with the values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSex.setAdapter(adapter);
        spinSex.setOnItemSelectedListener(this);

        initFirebase();
        usersObj = new Usuarios();

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtxtEmail.getText().toString();
                String password = edtxtPass.getText().toString();

                if(validations()){
                    //This data is for save in Realtime Database
                    usersObj.setNombre(edtxtFirstName.getText().toString());
                    usersObj.setApellido(edtxtLastName.getText().toString());
                    usersObj.setCorreo(edtxtEmail.getText().toString());
                    usersObj.setTelefono(edtxtPhone.getText().toString());
                    usersObj.setPass(edtxtPass.getText().toString());
                    usersObj.setSexo(spinSex.getSelectedItem().toString()+","+ spinSex.getSelectedItemPosition());
                    createUser(email, password);
                }
            }
        });

        btnSignUpArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, RegistroArtista.class);
                startActivity(intent);
            }
        });

    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID  = user.getUid();

                            sendMail(user, userID);

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e){
                                    new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("La cuenta de correo electronico esta siendo usada por otra cuenta")
                                            .hideConfirmButton()
                                            .setCancelText("Entendido")
                                            .show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    private void sendMail(FirebaseUser user, final String userID){
        //The userID is for save de data in RealtimeDatabase
        user.sendEmailVerification()
                .addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Save data in Realtime Database
                            databaseReference.child("Usuarios").child(userID).setValue(usersObj);
                            clearInputs();

                            new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Registro exitoso")
                                    .setContentText("Se envio un correo de confirmación")
                                    .setConfirmText("Perfecto")
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

                        } else {
                            new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Un error interno ha ocurrido, intenta de nuevo más tarde")
                                    .hideConfirmButton()
                                    .setCancelText("Entendido")
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
        spinSex.setSelection(0);
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
        if(spinSex.getSelectedItem().toString().equals("Gender")){
            TextView errorTextview = (TextView) spinSex.getSelectedView();
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
}
