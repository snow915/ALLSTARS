package com.store.Credenciales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.MainActivity;
import com.store.R;
import com.store.Register;
import com.store.RegistroArtista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button sign_in;
    Button sign_up;
    Button sign_up_as_artist;
    Button sign_in_as_artist;
    EditText user;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.sign_in = findViewById(R.id.id_sign_in);
        this.sign_up = findViewById(R.id.id_sign_up);
        this.sign_up_as_artist = findViewById(R.id.id_sign_up_as_artist);
        this.sign_in_as_artist = findViewById(R.id.id_sign_in_as_artist);
        this.sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = findViewById(R.id.editText3);
                password = findViewById(R.id.editText5);
                String user_val = user.getText().toString();
                String password_val = password.getText().toString();
                if (validate_fields(user, password)){
                    user_exist(user_val, password_val);
                }
            }
        });
        this.sign_in_as_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = findViewById(R.id.editText3);
                password = findViewById(R.id.editText5);
                String user_val = user.getText().toString();
                String password_val = password.getText().toString();
                if (validate_fields(user, password)){
                    artist_exist(user_val, password_val);
                }
            }
        });

        this.sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        this.sign_up_as_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroArtista.class);
                startActivity(intent);
            }
        });

    }

    private void user_exist(final String userd, final String password) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userd);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String retrieved_password = dataSnapshot.child("pass").getValue().toString();
                    if (password.equals(retrieved_password)){
                        String user_name = dataSnapshot.child("nombre").getValue().toString();
                        String last_name = dataSnapshot.child("apellido").getValue().toString();
                        save_preferences(user_name, userd, last_name);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("user_id", userd);
                        bundle.putString("user_name", user_name);
                        bundle.putString("user_type", "user");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "The user "+userd+" doesn't exist", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void artist_exist(final String userd, final String password) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("data").child(userd);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String retrieved_password = dataSnapshot.child("pass").getValue().toString();
                    if (password.equals(retrieved_password)){
                        String artist_name = dataSnapshot.child("nombre").getValue().toString();
                        save_artist_preferences(artist_name, userd);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("artist_id", userd);
                        bundle.putString("artist_name", artist_name);
                        bundle.putString("user_type", "artist");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "The user "+userd+" doesn't exist", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validate_fields(EditText user, EditText pass){
        if (user.getText().toString().equals("")){
            user.setError("Required");
            return false;
        } else if (pass.getText().toString().equals("")){
            pass.setError("Required");
            return false;
        }
        return true;
    }

    private void save_preferences(String username, String user_id, String lastname){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user",user.getText().toString());
        editor.putString("user_id", user_id);
        editor.putString("pass",password.getText().toString());
        editor.putString("username", username);
        editor.putString("user_type", "user");
        editor.putString("lastname", lastname);
        editor.commit();
    }
    private void save_artist_preferences(String username, String user_id) {
        SharedPreferences preferences = getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("artist_id", user_id);
        editor.putString("pass",password.getText().toString());
        editor.putString("artist_name", username);
        editor.commit();
    }
}
