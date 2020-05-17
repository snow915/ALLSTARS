package com.store.famous;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.adapters.AdapterServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDeleteService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDeleteService extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private SharedPreferencesApp preferencesApp;
    private String artistUserName;

    public FragmentDeleteService() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDeleteService.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDeleteService newInstance(String param1, String param2) {
        FragmentDeleteService fragment = new FragmentDeleteService();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_service, container, false);
        fillData(view);
        return view;
    }

    private void fillData(View v){
        final View view = v;
        final ArrayList<String> itemNames = new ArrayList<>();
        final ArrayList<String> itemPrices = new ArrayList<>();
        final ArrayList<String> itemDescriptions = new ArrayList<>();
        final ArrayList<String> itemMaximumTimes = new ArrayList<>();
        final ArrayList<String> itemIds = new ArrayList<>();
        final String CONTEXT = "delete";
        preferencesApp = new SharedPreferencesApp(getContext());
        preferencesApp.loadPreferences();
        artistUserName = preferencesApp.getArtistUsername();
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUserName).child("servicios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot element : dataSnapshot.getChildren()) {
                    itemNames.add(element.child("nombre").getValue().toString());
                    itemPrices.add(element.child("precio").getValue().toString());
                    itemDescriptions.add(element.child("detalles").getValue().toString());
                    itemMaximumTimes.add(element.child("tiempoMaximo").getValue().toString());
                    itemIds.add(element.getKey());
                }
                recycler = view.findViewById(R.id.recycler_delete_services);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                AdapterServices adapter = new AdapterServices(itemNames, itemPrices, itemDescriptions, itemMaximumTimes, itemIds, getContext(), CONTEXT);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
