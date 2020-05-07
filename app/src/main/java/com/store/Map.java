package com.store;

import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.store.adapters.PlaceAutoSuggestAdapter;
import com.store.user.EnviarSolicitud;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private SearchView searchView;
    private FloatingActionButton finishLocation;
    public double latitude, longitude;
    public String location;
    HashMap<String, String> hashMap;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("mapValues");

        final AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autocomplete);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(Map.this,android.R.layout.simple_list_item_1));

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        finishLocation = findViewById(R.id.finishLocation);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(parent.getItemAtPosition(position).toString(), true);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latitude = address.getLatitude();
                    longitude = address.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng ,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        finishLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().equals("") || autoCompleteTextView.getText().toString().equals(null)) {
                    new SweetAlertDialog(Map.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Debes buscar tu ubicación")
                            .show();
                } else {
                    new SweetAlertDialog(Map.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(location)
                            .setContentText("¿Deseas continuar con la ubicación seleccionada?")
                            .setConfirmText("Continuar")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    hashMap.put("ubicacion", location);
                                    hashMap.put("latitud", String.valueOf(latitude));
                                    hashMap.put("longitud", String.valueOf(longitude));
                                    Intent intent = new Intent(getApplicationContext(), EnviarSolicitud.class);
                                    intent.putExtra("mapValues", hashMap);
                                    startActivity(intent);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
