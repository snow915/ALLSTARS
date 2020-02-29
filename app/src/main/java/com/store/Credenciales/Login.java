package com.store.Credenciales;

import android.content.Intent;
import android.os.Bundle;

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
        this.sign_in = (Button) findViewById(R.id.id_sign_in);
        this.sign_up = (Button) findViewById(R.id.id_sign_up);
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("user_id", userd);
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
}
