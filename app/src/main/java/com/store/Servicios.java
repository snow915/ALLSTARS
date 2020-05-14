package com.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class Servicios extends AppCompatActivity {
    EditText viewServiceName;
    EditText viewServiceDescription;
    EditText viewServicePrice;
    EditText viewServiceMaxTime;
    String retrievedServiceName;
    String retrievedServiceDescription;
    String retrievedServicePrice;
    String retrievedServiceMaxTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
        viewServiceName = findViewById(R.id.viewNameService);
        viewServiceDescription = findViewById(R.id.viewServiceDescriptionInput);
        viewServicePrice = findViewById(R.id.viewServicePriceInput);
        viewServiceMaxTime = findViewById(R.id.viewServiceMaximumTimeInput);
        retrievedServiceName = getIntent().getStringExtra("name");
        retrievedServiceDescription = getIntent().getStringExtra("description");
        retrievedServicePrice = getIntent().getStringExtra("price");
        retrievedServiceMaxTime = getIntent().getStringExtra("maximum_hours");
        viewServiceName.setText(retrievedServiceName);
        viewServicePrice.setText(retrievedServicePrice);
        viewServiceDescription.setText(retrievedServiceDescription);
        viewServiceMaxTime.setText(retrievedServiceMaxTime);
    }
}
