package com.store.credentials;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private Button btnSignIn;
    private Button btnSignUp;
    private Button btnSignUpAsArtist;
    private Button btnSignInAsArtist;
    private EditText edtxtUser;
    private EditText edtxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Associate variables with id from activity_login layout
        btnSignIn = findViewById(R.id.id_sign_in);
        btnSignUp = findViewById(R.id.id_sign_up);
        btnSignUpAsArtist = findViewById(R.id.id_sign_up_as_artist);
        btnSignInAsArtist = findViewById(R.id.id_sign_in_as_artist);
        edtxtUser = findViewById(R.id.editText3);
        edtxtPassword = findViewById(R.id.editText5);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userValue = edtxtUser.getText().toString();
                String passValue = edtxtPassword.getText().toString();
                if (validateFields(userValue, passValue)){
                    userPassExist(userValue, passValue, "user");
                }
            }
        });

        btnSignInAsArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userValue = edtxtUser.getText().toString();
                String passValue = edtxtPassword.getText().toString();
                if (validateFields(userValue, passValue)){
                    userPassExist(userValue, passValue, "artist");
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

        btnSignUpAsArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroArtista.class);
                startActivity(intent);
            }
        });
    }


    private void userPassExist(final String userValue, final String passValue, final String userType){
        DatabaseReference ref;
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
                        sharedPreferencesApp.saveLoginData(retrievedUserFirstName, retrievedUserLastName, userValue, passValue, userType);
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
