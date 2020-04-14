package com.store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.store.Adapters.AdapterDatos;
import com.store.Adapters.AdapterDatosSolicitud;
import com.store.Vo.DatosSolicitudVo;

import java.util.ArrayList;
import java.util.HashMap;


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
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        list_datos.add(
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

                    AdapterDatosSolicitud adapter = new AdapterDatosSolicitud(list_datos, getActivity().getApplicationContext());
                    recycler.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Este codigo se puede optimizar, tal vez convertirlo a HashMap y asi mandarlo en el intent, como en EnviarSolicitud.class
                            String fechaInicio = list_datos.get(recycler.getChildAdapterPosition(v)).getFechaInicio();
                            String fechaFin = list_datos.get(recycler.getChildAdapterPosition(v)).getFechaFin();
                            String horaInicio = list_datos.get(recycler.getChildAdapterPosition(v)).getHoraInicio();
                            String horaFin = list_datos.get(recycler.getChildAdapterPosition(v)).getHoraFin();
                            String tipoEvento = list_datos.get(recycler.getChildAdapterPosition(v)).getTipoEvento();
                            String tipoPublico = list_datos.get(recycler.getChildAdapterPosition(v)).getTipoPublico();
                            String detalles = list_datos.get(recycler.getChildAdapterPosition(v)).getDetalles();
                            String ubicacion = list_datos.get(recycler.getChildAdapterPosition(v)).getNombreUbicacion();
                            String latitud = list_datos.get(recycler.getChildAdapterPosition(v)).getLatitud();
                            String longitud = list_datos.get(recycler.getChildAdapterPosition(v)).getLongitud();
                            String nombreSolicitante = list_datos.get(recycler.getChildAdapterPosition(v)).getNombreSolicitante();
                            String apellidoSolicitante = list_datos.get(recycler.getChildAdapterPosition(v)).getApellidoSolicitante();

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

    private void load_artist_preferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        artistUser = preferences.getString("artist_id", null);
    }

    private void guardarDatosUbucacion(String nombreUbicacion, String latitud, String longitud) {
        SharedPreferences preferences = getActivity().getSharedPreferences("datos_ubicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nombreUbicacion", nombreUbicacion);
        editor.putString("latitud", latitud);
        editor.putString("longitud", longitud);
        editor.commit();
    }
}
