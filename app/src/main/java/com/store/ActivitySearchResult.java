package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.adapters.AdapterDatos;
import com.store.vo.DatosVo;

import java.util.ArrayList;

public class ActivitySearchResult extends AppCompatActivity {

    private SearchView searchBar;
    private LinearLayout layoutFilter;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchBar = findViewById(R.id.searchResultBar);
        recycler = findViewById(R.id.recyclerQueryResults);
        spinnerFilter = findViewById(R.id.spinner_filter);
        layoutFilter = findViewById(R.id.layout_filter);
        final Context context = this;
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        searchBar.setIconifiedByDefault(true);
        searchBar.setFocusable(true);
        searchBar.setIconified(false);
        searchBar.requestFocusFromTouch();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchArtists(query, context);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchArtists(String query, final Context context) {
        final ArrayList<DatosVo> listData = new ArrayList<DatosVo>();
        final String search = query.toLowerCase().trim();
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean found;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("nombre").getValue(String.class);
                    String categories = ds.child("categoria").getValue(String.class);
                    found = name.toLowerCase().contains(search) || categories.toLowerCase().contains(search);
                    if(found) {
                        String imageRoute = ds.child("imagen").getValue(String.class);
                        int score = ds.child("puntaje").getValue(Integer.class);
                        listData.add(new DatosVo(name, categories, "", imageRoute, score, ""));
                    }
                }
                AdapterDatos adapter = new AdapterDatos(listData, getApplicationContext());
                recycler.setAdapter(adapter);
                ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.array_filters,
                        android.R.layout.simple_spinner_item);
                filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFilter.setAdapter(filterAdapter);
                layoutFilter.setVisibility(View.VISIBLE);
                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }
}
