package com.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class EnviarSolicitud extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_solicitud);
        final ArrayList<String> list = getIntent().getExtras().getStringArrayList("valuesHiring");

    }
}
