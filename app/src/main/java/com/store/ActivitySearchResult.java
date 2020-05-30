package com.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michael.easydialog.EasyDialog;
import com.store.adapters.AdapterDatos;
import com.store.adapters.AdapterFiltros;
import com.store.vo.DatosVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivitySearchResult extends AppCompatActivity {

    private SearchView searchBar;
    private LinearLayout layoutFilter;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private ConstraintLayout modal;
    private List<String> listGroup;
    private HashMap<String, List<String>> listItem;
    private AdapterFiltros adapterFiltros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchBar = findViewById(R.id.searchResultBar);
        recycler = findViewById(R.id.recyclerQueryResults);
        layoutFilter = findViewById(R.id.layout_filter);
        modal = findViewById(R.id.modal);
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        searchBar.setIconifiedByDefault(true);
        searchBar.setFocusable(true);
        searchBar.setIconified(false);
        searchBar.requestFocusFromTouch();
        final View v = this.getLayoutInflater().inflate(R.layout.modal_filters_app, null);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchArtists(query, v);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchArtists(String query, View modalView) {
        final View modal = modalView;
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
                layoutFilter.setVisibility(View.VISIBLE);
                layoutFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View v2 = v;
                        new EasyDialog(v2.getContext())
                                .setLayout(modal)
                                .setBackgroundColor(v2.getContext().getResources().getColor(R.color.colorWhite))
                                .setLocationByAttachedView(layoutFilter)
                                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                                .setMatchParent(false)
                                .setMarginLeftAndRight(24, 24)
                                .setTouchOutsideDismiss(true)
                                .setOutsideColor(v2.getContext().getResources().getColor(R.color.colorPalid))
                                .setOnEasyDialogShow(new EasyDialog.OnEasyDialogShow() {
                                    @Override
                                    public void onShow() {
                                        ExpandableListView filtersList = modal.findViewById(R.id.filters_list);
                                        Filter ranking = new Filter("Ranking");
                                        ranking.filterTypes.add("range");
                                        Filter requests = new Filter("Solicitudes");
                                        requests.filterTypes.add("range");
                                        ArrayList<Filter> allFilters = new ArrayList<>();
                                        allFilters.add(requests);
                                        allFilters.add(ranking);
                                        AdapterFiltros adapterFiltros = new AdapterFiltros(v2.getContext(), allFilters);
                                        filtersList.setAdapter(adapterFiltros);
                                    }
                                })
                                .setOnEasyDialogDismissed(new EasyDialog.OnEasyDialogDismissed() {
                                    @Override
                                    public void onDismissed() {
                                        ((ViewGroup)modal.getParent()).removeView(modal);
                                    }
                                })
                                .show();
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
