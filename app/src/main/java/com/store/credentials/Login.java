package com.store.credentials;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.MainActivity;
import com.store.R;
import com.store.SharedPreferencesApp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    private Button btnSignIn, btnSignUp;
    private EditText edtxtUser, edtxtPassword;
    private CheckBox chboxSignInArtist;

    private DatabaseReference ref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Associate variables with id from activity_login layout
        btnSignIn = findViewById(R.id.id_sign_in);
        btnSignUp = findViewById(R.id.id_sign_up);
        edtxtUser = findViewById(R.id.editText3);
        edtxtPassword = findViewById(R.id.editText5);
        chboxSignInArtist = findViewById(R.id.checkBox_sign_in_artist);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userValue = edtxtUser.getText().toString();
                String passValue = edtxtPassword.getText().toString();
                if(validateFields(userValue, passValue)){
                    if(chboxSignInArtist.isChecked()){
                        userPassExist(userValue, passValue, "artist");
                    } else {
                        signIn(userValue, passValue, "user");
                    }
                }
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }

    private void signIn(String email, String password, final String userType) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID  = user.getUid();

                            //EMAIL VERIFICATION
                            //user.isEmailVerified() returns true when the mail was verified
                            if(user.isEmailVerified()){
                                retrieveDataRealtimeDB(userID, userType);
                            } else {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Debes verificar tu correo primero")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                                FirebaseAuth.getInstance().signOut();
                            }
                            //EMAIL VERIFICATION

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Usuario o contraseña erroneos")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                            }  catch (FirebaseAuthInvalidUserException e){
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("El usuario que proporcionaste no existe")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void retrieveDataRealtimeDB(final String userID, final String userType) {
        if (userType.equals("user")) {
            ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("data").child(userID);
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String retrievedUserFirstName = "";
                String retrievedUserLastName = "";
                String retrievedUserEmail = "";
                //Try catch handle errors of type NullPointerException
                try {
                    retrievedUserFirstName = dataSnapshot.child("nombre").getValue().toString();
                    retrievedUserLastName = dataSnapshot.child("apellido").getValue().toString();
                    retrievedUserEmail = dataSnapshot.child("correo").getValue().toString();
                } catch (Exception e) {
                }
                SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                sharedPreferencesApp.saveLoginData(retrievedUserFirstName, retrievedUserLastName, retrievedUserEmail ,userID, userType);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void userPassExist(final String userValue, final String passValue, final String userType){
        if (userType.equals("user")) {
            ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userValue);
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("data").child(userValue);
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //Here verify if user exist
                    //Here get password of that user
                    String retrievedPasswordDB = dataSnapshot.child("pass").getValue().toString();
                    //Verify if pass from EditText and pass from DB is the same
                    if (passValue.equals(retrievedPasswordDB)){
                        String retrievedUserFirstName = "";
                        String retrievedUserLastName = "";
                        //Try catch handle errors of type NullPointerException
                        try {
                            retrievedUserFirstName = dataSnapshot.child("nombre").getValue().toString();
                            retrievedUserLastName = dataSnapshot.child("apellido").getValue().toString();
                        } catch (Exception e) { }
                        SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                        sharedPreferencesApp.saveLoginData(retrievedUserFirstName, retrievedUserLastName, userValue ,userValue, userType);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "El usuario "+userValue+" no existe", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private boolean validateFields(String user, String pass){
        if (user.equals("")){
            edtxtUser.setError("Required");
            return false;
        } else if (pass.equals("")){
            edtxtPassword.setError("Required");
            return false;
        }
        return true;
    }
}
