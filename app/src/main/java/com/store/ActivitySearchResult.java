package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private RecyclerView recycler;
    private String retrievedQuery;
    private DatabaseReference reference;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchBar = findViewById(R.id.searchResultBar);
        recycler = findViewById(R.id.recyclerQueryResults);
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        searchBar.setIconifiedByDefault(true);
        searchBar.setFocusable(true);
        searchBar.setIconified(false);
        searchBar.requestFocusFromTouch();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchArtists(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchArtists(String query) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }
}
