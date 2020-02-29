package com.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.store.Credenciales.Login;

public class Registro extends AppCompatActivity {

    Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        btn_register = findViewById(R.id.register_btn);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_text_fields()){
                    Toast.makeText(getApplicationContext(),"REGISTRO EXITOSO",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(),"FALTAN CAMPOS",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public Boolean check_text_fields(){
        Boolean flag = true;
       EditText name = findViewById(R.id.name);
       EditText first_name = findViewById(R.id.firstName);
       EditText last_name = findViewById(R.id.lastName);
       RadioGroup sex = findViewById(R.id.sex_group);
       EditText phone = findViewById(R.id.phone);
       EditText mail = findViewById(R.id.mail);
       EditText address = findViewById(R.id.address);
       EditText cp = findViewById(R.id.cp);
       EditText user = findViewById(R.id.user);
       EditText pass = findViewById(R.id.pass);
       EditText arr[] = new EditText[9];
       arr[0] = name;
       arr[1] = first_name;
       arr[2] = last_name;
       arr[3] = phone;
       arr[4] = mail;
       arr[5] = address;
       arr[6] = cp;
       arr[7] = user;
       arr[8] = pass;
        for (EditText txt: arr) {
            if (txt.getText().toString().isEmpty()){
                flag = false;
                break;
            }
        }
        return flag;
    }
}
