package com.store.famous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.store.MainActivity;
import com.store.R;

public class SolicitudRechazada extends AppCompatActivity {
    Button regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_rechazada);
        regresar = findViewById(R.id.idRegresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SolicitudRechazada.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                finish();
            }
        });
    }
}
