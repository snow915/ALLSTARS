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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferencesApp = new SharedPreferencesApp(getContext());
        sharedPreferencesApp.loadPreferences();
        artistUser = sharedPreferencesApp.getArtistUsername();
        View v = inflater.inflate(R.layout.fragment_solicitud, container, false);
        fillData(v);
        return v;
    }

    private void fillData(View v){
        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUser).child("solicitudes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        final String idHiring = postSnapshot.getValue().toString();
                        retrieveHirings(view, idHiring);
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

    private void retrieveHirings(final View view, final String idSolicitud){
        referenceHiring = FirebaseDatabase.getInstance().getReference().child("solicitudes");
        referenceHiring.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                for(DataSnapshot postSnapshotHiring : dataSnapshot2.getChildren()){
                    if(idSolicitud.equals(postSnapshotHiring.child("solicitudID").getValue().toString())){
                        listData.add(
                                new DatosSolicitudVo(postSnapshotHiring.child("fechaInicio").getValue().toString(),
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
                                        postSnapshotHiring.child("userLastname").getValue().toString()
                                )
                        );
                    }
                }

                recycler = view.findViewById(R.id.recycler_id_solicitud);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                AdapterDatosSolicitud adapter = new AdapterDatosSolicitud(listData, getActivity().getApplicationContext());
                recycler.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String startDate = listData.get(recycler.getChildAdapterPosition(v)).getFechaInicio();
                        String finishDate = listData.get(recycler.getChildAdapterPosition(v)).getFechaFin();
                        String startTime = listData.get(recycler.getChildAdapterPosition(v)).getHoraInicio();
                        String finishTime = listData.get(recycler.getChildAdapterPosition(v)).getHoraFin();
                        String eventType = listData.get(recycler.getChildAdapterPosition(v)).getTipoEvento();
                        String audienceType = listData.get(recycler.getChildAdapterPosition(v)).getTipoPublico();
                        String details = listData.get(recycler.getChildAdapterPosition(v)).getDetalles();
                        String location = listData.get(recycler.getChildAdapterPosition(v)).getNombreUbicacion();
                        String latitude = listData.get(recycler.getChildAdapterPosition(v)).getLatitud();
                        String longitude = listData.get(recycler.getChildAdapterPosition(v)).getLongitud();
                        String applicantsFirstname = listData.get(recycler.getChildAdapterPosition(v)).getNombreSolicitante();
                        String applicantsLastname = listData.get(recycler.getChildAdapterPosition(v)).getApellidoSolicitante();

                        hashMapArtist.put("startDate", startDate);
                        hashMapArtist.put("finishDate", finishDate);
                        hashMapArtist.put("startTime", startTime);
                        hashMapArtist.put("finishTime", finishTime);
                        hashMapArtist.put("eventType", eventType);
                        hashMapArtist.put("audienceType", audienceType);
                        hashMapArtist.put("details", details);
                        hashMapArtist.put("location", location);
                        hashMapArtist.put("latitude", latitude);
                        hashMapArtist.put("longitude", longitude);
                        hashMapArtist.put("applicantsFirstname", applicantsFirstname);
                        hashMapArtist.put("applicantsLastname", applicantsLastname);

                        saveLocationData(location, latitude, longitude);

                        Intent infoSolicitud = new Intent(getActivity(), InfoSolicitud.class);
                        infoSolicitud.putExtra("mapValuesArtist", hashMapArtist);
                        startActivity(infoSolicitud);
                    }
                });
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
