package com.store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.store.Adapters.AdapterDatos;
import com.store.Adapters.AdapterDatosSolicitud;
import com.store.Vo.DatosSolicitudVo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSolicitud#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSolicitud extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<DatosSolicitudVo> list_datos;
    RecyclerView recycler;
    DatabaseReference reference;
    String artistUser;

    public FragmentSolicitud() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSolicitud.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSolicitud newInstance(String param1, String param2) {
        FragmentSolicitud fragment = new FragmentSolicitud();
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
        load_artist_preferences();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solicitud, container, false);
        llenarDatos(v);
        return v;
    }

    public void llenarDatos(View v){
        final View view = v;
        list_datos = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUser).child("solicitudes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    list_datos.add(
                            new DatosSolicitudVo(postSnapshot.child("fechaInicio").getValue().toString(),
                                    postSnapshot.child("horaInicio").getValue().toString(),
                                    postSnapshot.child("tipoEvento").getValue().toString()
                            )
                    );
                }
                recycler = view.findViewById(R.id.recycler_id_solicitud);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                AdapterDatosSolicitud adapter = new AdapterDatosSolicitud(list_datos, getActivity().getApplicationContext());
                recycler.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fechaInicio = list_datos.get(recycler.getChildAdapterPosition(v)).getFechaInicio();
                        String horaInicio = list_datos.get(recycler.getChildAdapterPosition(v)).getHoraInicio();
                        String tipoEvento = list_datos.get(recycler.getChildAdapterPosition(v)).getTipoEvento();
                        //Intent infoProducto = new Intent(getActivity(), InfoProducto.class);
                        //infoProducto.putExtra("fechaInicio", fechaInicio);
                        //infoProducto.putExtra("horaInicio", horaInicio);
                        //infoProducto.putExtra("tipoEvento", tipoEvento);
                        //startActivity(infoProducto);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void load_artist_preferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        artistUser = preferences.getString("artist_id", null);
    }
}
