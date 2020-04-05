package com.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.store.Adapters.AdapterDatos;
import com.store.Vo.DatosVo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link index.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link index#newInstance} factory method to
 * create an instance of this fragment.
 */
public class index extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<DatosVo> list_datos;
    RecyclerView recycler;
    DatabaseReference reference;
    private OnFragmentInteractionListener mListener;

    public index() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment index.
     */
    // TODO: Rename and change types and number of parameters
    public static index newInstance(String param1, String param2) {
        index fragment = new index();
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

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        llenarDatos(v);
        return v;
    }

    public void llenarDatos(View v){
        final View view = v;
        list_datos = new ArrayList<DatosVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    list_datos.add(
                            new DatosVo(postSnapshot.child("nombre").getValue().toString(),
                                postSnapshot.child("categoria").getValue().toString(),
                                postSnapshot.child("biografia").getValue().toString(),
                                postSnapshot.child("imagen").getValue().toString(),
                                Integer.valueOf(postSnapshot.child("puntaje").getValue().toString())
                            )
                    );
                }
                recycler = view.findViewById(R.id.recycler_id);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                AdapterDatos adapter = new AdapterDatos(list_datos, getActivity().getApplicationContext());
                recycler.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombre = list_datos.get(recycler.getChildAdapterPosition(v)).getNombre();
                        String precio = list_datos.get(recycler.getChildAdapterPosition(v)).getPrecio();
                        String biografia = list_datos.get(recycler.getChildAdapterPosition(v)).getBiografia();
                        String imagen = list_datos.get(recycler.getChildAdapterPosition(v)).getRutaImagen();
                        int stars = list_datos.get(recycler.getChildAdapterPosition(v)).getStars();
                        Intent infoProducto = new Intent(getActivity(), InfoProducto.class);
                        infoProducto.putExtra("nombre", nombre);
                        infoProducto.putExtra("precio", precio);
                        infoProducto.putExtra("image",imagen);
                        infoProducto.putExtra("stars", stars);
                        infoProducto.putExtra("biografia", biografia);
                        startActivity(infoProducto);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
