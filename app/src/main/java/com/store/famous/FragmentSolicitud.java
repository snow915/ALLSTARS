package com.store.famous;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.adapters.AdapterDatosSolicitud;
import com.store.vo.DatosSolicitudVo;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSolicitud extends Fragment {
    private static final String TAG = "FragmentSolicitud";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<DatosSolicitudVo> listData;
    private RecyclerView recycler;
    private DatabaseReference reference, referenceHiring;
    private String artistUser;
    private SharedPreferencesApp sharedPreferencesApp;
    public HashMap<String, String> hashMapArtist = new HashMap<String, String>();
    private String typeRequest;

    public FragmentSolicitud() {}

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
        Bundle b = this.getArguments();
        typeRequest = b.getString("typeRequest");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferencesApp = new SharedPreferencesApp(getContext());
        sharedPreferencesApp.loadPreferences();
        artistUser = sharedPreferencesApp.getArtistUsername();
        View v = inflater.inflate(R.layout.fragment_solicitud, container, false);
        fillData(v, typeRequest.toUpperCase());
        return v;
    }

    private void fillData(View v, final String typeRequest){
        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUser).child("solicitudes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        final String idHiring = postSnapshot.getValue().toString();
                        retrieveHirings(view, idHiring, typeRequest);
                    }

                } else {
                    Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void retrieveHirings(final View view, final String idSolicitud, final String typeRequest){
        hashMapArtist.put("tipoSolicitud", typeRequest);
        if(typeRequest.equals("PENDING")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes");
        } else if(typeRequest.equals("ACCEPTED")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes_aceptadas").child("solicitudes_en_proceso");
        } else if(typeRequest.equals("REJECTED")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes_rechazadas");
        } else {
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes_aceptadas").child("solicitudes_pagadas");
        }

        referenceHiring.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                for(DataSnapshot postSnapshotHiring : dataSnapshot2.getChildren()){
                    if(idSolicitud.equals(postSnapshotHiring.child("solicitudID").getValue().toString())){
                        listData.add(
                                new DatosSolicitudVo(postSnapshotHiring.child("solicitudID").getValue().toString(),
                                        postSnapshotHiring.child("fechaInicio").getValue().toString(),
                                        postSnapshotHiring.child("horaInicio").getValue().toString(),
                                        postSnapshotHiring.child("tipoEvento").getValue().toString(),
                                        postSnapshotHiring.child("fechaFin").getValue().toString(),
                                        postSnapshotHiring.child("horaFin").getValue().toString(),
                                        postSnapshotHiring.child("tipoPublico").getValue().toString(),
                                        postSnapshotHiring.child("detalles").getValue().toString(),
                                        postSnapshotHiring.child("ubicacion").getValue().toString(),
                                        postSnapshotHiring.child("latitud").getValue().toString(),
                                        postSnapshotHiring.child("longitud").getValue().toString(),
                                        postSnapshotHiring.child("userName").getValue().toString(),
                                        postSnapshotHiring.child("userLastname").getValue().toString(),
                                        postSnapshotHiring.child("nombreServicio").getValue().toString(),
                                        postSnapshotHiring.child("precioServicio").getValue().toString(),
                                        postSnapshotHiring.child("nombreFamoso").getValue().toString(),
                                        postSnapshotHiring.child("userFamoso").getValue().toString(),
                                        postSnapshotHiring.child("userID").getValue().toString(),
                                        postSnapshotHiring.child("imagenFamoso").getValue().toString()
                                )
                        );
                    }
                }

                recycler = view.findViewById(R.id.recycler_id_solicitud);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                try{
                    AdapterDatosSolicitud adapter = new AdapterDatosSolicitud(listData, getActivity(), typeRequest);
                    recycler.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String solicitudID = listData.get(recycler.getChildAdapterPosition(v)).getSolicitudID();
                            String fechaInicio = listData.get(recycler.getChildAdapterPosition(v)).getFechaInicio();
                            String fechaFin = listData.get(recycler.getChildAdapterPosition(v)).getFechaFin();
                            String horaInicio = listData.get(recycler.getChildAdapterPosition(v)).getHoraInicio();
                            String horaFin = listData.get(recycler.getChildAdapterPosition(v)).getHoraFin();
                            String tipoEvento = listData.get(recycler.getChildAdapterPosition(v)).getTipoEvento();
                            String tipoPublico = listData.get(recycler.getChildAdapterPosition(v)).getTipoPublico();
                            String detalles = listData.get(recycler.getChildAdapterPosition(v)).getDetalles();
                            String ubicacion = listData.get(recycler.getChildAdapterPosition(v)).getUbicacion();
                            String latitud = listData.get(recycler.getChildAdapterPosition(v)).getLatitud();
                            String longitud = listData.get(recycler.getChildAdapterPosition(v)).getLongitud();
                            String userName = listData.get(recycler.getChildAdapterPosition(v)).getUserName();
                            String userLastname = listData.get(recycler.getChildAdapterPosition(v)).getUserLastname();
                            String nombreServicio = listData.get(recycler.getChildAdapterPosition(v)).getNombreServicio();
                            String precioServicio = listData.get(recycler.getChildAdapterPosition(v)).getPrecioServicio();
                            String nombreFamoso = listData.get(recycler.getChildAdapterPosition(v)).getNombreFamoso();
                            String userFamoso = listData.get(recycler.getChildAdapterPosition(v)).getUserFamoso();
                            String userID = listData.get(recycler.getChildAdapterPosition(v)).getUserID();
                            String imagenFamoso = listData.get(recycler.getChildAdapterPosition(v)).getImagenFamoso();

                            //Tal vez con un for?
                            hashMapArtist.put("solicitudID", solicitudID);
                            hashMapArtist.put("fechaInicio", fechaInicio);
                            hashMapArtist.put("fechaFin", fechaFin);
                            hashMapArtist.put("horaInicio", horaInicio);
                            hashMapArtist.put("horaFin", horaFin);
                            hashMapArtist.put("tipoEvento", tipoEvento);
                            hashMapArtist.put("tipoPublico", tipoPublico);
                            hashMapArtist.put("detalles", detalles);
                            hashMapArtist.put("ubicacion", ubicacion);
                            hashMapArtist.put("latitud", latitud);
                            hashMapArtist.put("longitud", longitud);
                            hashMapArtist.put("userName", userName);
                            hashMapArtist.put("userLastname", userLastname);
                            hashMapArtist.put("nombreServicio", nombreServicio);
                            hashMapArtist.put("precioServicio", precioServicio);
                            hashMapArtist.put("nombreFamoso", nombreFamoso);
                            hashMapArtist.put("userFamoso", userFamoso);
                            hashMapArtist.put("userID", userID);
                            hashMapArtist.put("imagenFamoso", imagenFamoso);

                            saveLocationData(ubicacion, latitud, longitud);

                            Intent infoSolicitud = new Intent(getActivity(), InfoSolicitud.class);
                            infoSolicitud.putExtra("mapValuesArtist", hashMapArtist);
                            startActivity(infoSolicitud);
                        }
                    });
                } catch(Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveLocationData(String locationName, String latitude, String longitude) {
        SharedPreferences preferences = getActivity().getSharedPreferences("datos_ubicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nombreUbicacion", locationName);
        editor.putString("latitud", latitude);
        editor.putString("longitud", longitude);
        editor.commit();
    }
}
