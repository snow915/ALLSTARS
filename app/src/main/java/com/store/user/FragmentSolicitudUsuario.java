package com.store.user;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.adapters.AdapterDatosSolicitudUsuario;
import com.store.vo.DatosSolicitudVo;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSolicitudUsuario extends Fragment {

    private String typeRequest;
    ArrayList<DatosSolicitudVo> listData;
    private RecyclerView recycler;
    private DatabaseReference reference, referenceHiring;
    private String userID;
    private SharedPreferencesApp sharedPreferencesApp;
    public HashMap<String, String> hashMapRequest = new HashMap<String, String>();

    public FragmentSolicitudUsuario() {}

    public static FragmentSolicitudUsuario newInstance(String param1, String param2) {
        FragmentSolicitudUsuario fragment = new FragmentSolicitudUsuario();
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
        userID = sharedPreferencesApp.getUserID();

        View v = inflater.inflate(R.layout.fragment_solicitud_usuario, container, false);
        switch (typeRequest.toUpperCase()){
            case "PENDING" :
                showPendingsRequests(v, typeRequest.toUpperCase());
                break;
            case "ACCEPTED":
                showAcceptedRequests(v, typeRequest.toUpperCase());
                break;
            case "REJECTED":
                showRejectedRequests(v, typeRequest.toUpperCase());
                break;
        }
        return v;
    }

    private void showPendingsRequests(View v, final String typeRequest){
        hashMapRequest.put("tipoSolicitud", typeRequest);
        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID).child("solicitudes");
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

    private void showAcceptedRequests(View v, final String typeRequest){
        hashMapRequest.put("tipoSolicitud", typeRequest);

        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID).child("solicitudes");
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

    private void showRejectedRequests(View v, final String typeRequest){
        hashMapRequest.put("tipoSolicitud", typeRequest);

        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID).child("solicitudes");
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

    private void retrieveHirings(final View view, final String idSolicitud, String typeRequest){
        if(typeRequest.equals("PENDING")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes");
        } else if(typeRequest.equals("ACCEPTED")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes_aceptadas");
        } else if(typeRequest.equals("REJECTED")){
            referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes_rechazadas");
        }

        referenceHiring.addValueEventListener(new ValueEventListener() {
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

                recycler = view.findViewById(R.id.recycler_id_solicitud_usuario);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                try{
                    AdapterDatosSolicitudUsuario adapter = new AdapterDatosSolicitudUsuario(listData, getActivity().getApplicationContext());
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
                            hashMapRequest.put("solicitudID", solicitudID);
                            hashMapRequest.put("fechaInicio", fechaInicio);
                            hashMapRequest.put("fechaFin", fechaFin);
                            hashMapRequest.put("horaInicio", horaInicio);
                            hashMapRequest.put("horaFin", horaFin);
                            hashMapRequest.put("tipoEvento", tipoEvento);
                            hashMapRequest.put("tipoPublico", tipoPublico);
                            hashMapRequest.put("detalles", detalles);
                            hashMapRequest.put("ubicacion", ubicacion);
                            hashMapRequest.put("latitud", latitud);
                            hashMapRequest.put("longitud", longitud);
                            hashMapRequest.put("userName", userName);
                            hashMapRequest.put("userLastname", userLastname);
                            hashMapRequest.put("nombreServicio", nombreServicio);
                            hashMapRequest.put("precioServicio", precioServicio);
                            hashMapRequest.put("nombreFamoso", nombreFamoso);
                            hashMapRequest.put("userFamoso", userFamoso);
                            hashMapRequest.put("userID", userID);
                            hashMapRequest.put("imagenFamoso", imagenFamoso);

                            saveLocationData(ubicacion, latitud, longitud);

                            Intent infoSolicitud = new Intent(getActivity(), InfoSolicitudERUsuario.class);
                            infoSolicitud.putExtra("mapValuesRequest", hashMapRequest);
                            startActivity(infoSolicitud);
                        }
                    });
                } catch (Exception e){

                }

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
