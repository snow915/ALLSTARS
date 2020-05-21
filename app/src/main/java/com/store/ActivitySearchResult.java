package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.store.famous.Artistas;
import com.store.viewHolders.ViewHolderArtistas;

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
        retrievedQuery = getIntent().getStringExtra("query");
        reference = FirebaseDatabase.getInstance().getReference().child("data");
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

    @Override
    protected void onStart() {
        super.onStart();
        searchArtists(retrievedQuery);
    }

    private void searchArtists(String query) {
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Query firebaseQuery = reference.orderByChild("nombre").startAt(query).endAt(query + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Artistas>().setQuery(firebaseQuery, Artistas.class).build();
        adapter = new FirebaseRecyclerAdapter<Artistas, ViewHolderArtistas>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderArtistas holder, int position, @NonNull Artistas model) {
                holder.nombre.setText(model.getNombre());
                holder.precio.setText(model.getCategoria());
                holder.stars.setRating(model.getPuntaje());
                Glide.with(getApplicationContext())
                        .load(model.getImagen())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.imagen);
            }

            @NonNull
            @Override
            public ViewHolderArtistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
                return new ViewHolderArtistas(v);
            }
        };
        adapter.startListening();
        recycler.setAdapter(adapter);
    }
}