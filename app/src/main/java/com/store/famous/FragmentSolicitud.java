package com.store.famous;

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
import com.store.adapters.AdapterDatosSolicitud;
import com.store.vo.DatosSolicitudVo;
import java.util.ArrayList;

public class FragmentSolicitud extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<DatosSolicitudVo> listData;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private String artistUser;
    private SharedPreferencesApp sharedPreferencesApp;

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
        llenarDatos(v);
        return v;
    }

    public void llenarDatos(View v){
        final View view = v;
        listData = new ArrayList<DatosSolicitudVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUser).child("solicitudes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        listData.add(
                                new DatosSolicitudVo(postSnapshot.child("fechaInicio").getValue().toString(),
                                        postSnapshot.child("horaInicio").getValue().toString(),
                                        postSnapshot.child("tipoEvento").getValue().toString(),
                                        postSnapshot.child("fechaFin").getValue().toString(),
                                        postSnapshot.child("horaFin").getValue().toString(),
                                        postSnapshot.child("tipoPublico").getValue().toString(),
                                        postSnapshot.child("detalles").getValue().toString(),
                                        postSnapshot.child("ubicacion").getValue().toString(),
                                        postSnapshot.child("latitud").getValue().toString(),
                                        postSnapshot.child("longitud").getValue().toString(),
                                        postSnapshot.child("userName").getValue().toString(),
                                        postSnapshot.child("userLastname").getValue().toString()
                                )
                        );
                    }
                    recycler = view.findViewById(R.id.recycler_id_solicitud);
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    AdapterDatosSolicitud adapter = new AdapterDatosSolicitud(listData, getActivity().getApplicationContext());
                    recycler.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Este codigo se puede optimizar, tal vez convertirlo a HashMap y asi mandarlo en el intent, como en EnviarSolicitud.class
                            String fechaInicio = listData.get(recycler.getChildAdapterPosition(v)).getFechaInicio();
                            String fechaFin = listData.get(recycler.getChildAdapterPosition(v)).getFechaFin();
                            String horaInicio = listData.get(recycler.getChildAdapterPosition(v)).getHoraInicio();
                            String horaFin = listData.get(recycler.getChildAdapterPosition(v)).getHoraFin();
                            String tipoEvento = listData.get(recycler.getChildAdapterPosition(v)).getTipoEvento();
                            String tipoPublico = listData.get(recycler.getChildAdapterPosition(v)).getTipoPublico();
                            String detalles = listData.get(recycler.getChildAdapterPosition(v)).getDetalles();
                            String ubicacion = listData.get(recycler.getChildAdapterPosition(v)).getNombreUbicacion();
                            String latitud = listData.get(recycler.getChildAdapterPosition(v)).getLatitud();
                            String longitud = listData.get(recycler.getChildAdapterPosition(v)).getLongitud();
                            String nombreSolicitante = listData.get(recycler.getChildAdapterPosition(v)).getNombreSolicitante();
                            String apellidoSolicitante = listData.get(recycler.getChildAdapterPosition(v)).getApellidoSolicitante();

                            guardarDatosUbucacion(ubicacion, latitud, longitud);

                            Intent infoSolicitud = new Intent(getActivity(), InfoSolicitud.class);
                            infoSolicitud.putExtra("fechaInicio", fechaInicio);
                            infoSolicitud.putExtra("horaInicio", horaInicio);
                            infoSolicitud.putExtra("tipoEvento", tipoEvento);
                            infoSolicitud.putExtra("fechaFin", fechaFin);
                            infoSolicitud.putExtra("horaFin", horaFin);
                            infoSolicitud.putExtra("tipoPublico", tipoPublico);
                            infoSolicitud.putExtra("detalles", detalles);
                            infoSolicitud.putExtra("ubicacion", ubicacion);
                            infoSolicitud.putExtra("latitud", latitud);
                            infoSolicitud.putExtra("longitud", longitud);
                            infoSolicitud.putExtra("nombreSolicitante", nombreSolicitante);
                            infoSolicitud.putExtra("apellidoSolicitante", apellidoSolicitante);
                            startActivity(infoSolicitud);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void guardarDatosUbucacion(String locationName, String latitude, String longitude) {
        SharedPreferences preferences = getActivity().getSharedPreferences("datos_ubicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nombreUbicacion", locationName);
        editor.putString("latitud", latitude);
        editor.putString("longitud", longitude);
        editor.commit();
    }
}
