package com.store.Credenciales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    EditText user;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.sign_in = findViewById(R.id.id_sign_in);
        this.sign_up = findViewById(R.id.id_sign_up);
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

        this.sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
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
                        save_preferences(user_name, userd);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("user_id", userd);
                        bundle.putString("user_name", user_name);
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

    private void save_preferences(String username, String user_id){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user",user.getText().toString());
        editor.putString("user_id", user_id);
        editor.putString("pass",password.getText().toString());
        editor.putString("username", username);
        editor.commit();
    }
}
