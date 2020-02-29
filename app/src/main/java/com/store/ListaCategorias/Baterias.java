package com.store.ListaCategorias;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.store.Adapters.AdapterDatos;
import com.store.Vo.DatosVo;
import com.store.InfoProducto;
import com.store.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Baterias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Baterias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Baterias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<DatosVo> list_datos;
    RecyclerView recycler;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Baterias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Baterias.
     */
    // TODO: Rename and change types and number of parameters
    public static Baterias newInstance(String param1, String param2) {
        Baterias fragment = new Baterias();
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
        View v = inflater.inflate(R.layout.fragment_baterias, container, false);

        recycler = (RecyclerView) v.findViewById(R.id.recycler_id_baterias);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list_datos = new ArrayList<DatosVo>();
        llenarDatos();
        AdapterDatos adapter = new AdapterDatos(list_datos);
        recycler.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = list_datos.get(recycler.getChildAdapterPosition(v)).getNombre();
                String precio = list_datos.get(recycler.getChildAdapterPosition(v)).getPrecio();
                int imagen = list_datos.get(recycler.getChildAdapterPosition(v)).getImagen();
                int stars = list_datos.get(recycler.getChildAdapterPosition(v)).getStars();
                Intent infoProducto = new Intent(getActivity(), InfoProducto.class);
                infoProducto.putExtra("nombre", nombre);
                infoProducto.putExtra("precio", precio);
                infoProducto.putExtra("image",imagen);
                infoProducto.putExtra("stars", stars);
                startActivity(infoProducto);
            }
        });

        return v;
    }

    public void llenarDatos(){
        list_datos.add(new DatosVo("Bateria Alcatel OTC550","$229",R.drawable.alcatel_otc550,5));
        list_datos.add(new DatosVo("Bateria Huawei HB500","$199",R.drawable.huawei_hb500,5));
        list_datos.add(new DatosVo("Bateria Lenovo Vibe K5","$219",R.drawable.lenovo_vibek5,5));
        list_datos.add(new DatosVo("Bateria Motorola BR50","$349",R.drawable.motorola_br50,5));
        list_datos.add(new DatosVo("Bateria Samsung B500AE","$349",R.drawable.samsung_b500ae,5));
        list_datos.add(new DatosVo("Bateria Blu Studio J2","$349",R.drawable.blu_studioj2,5));
        list_datos.add(new DatosVo("Bateria iPhone 5s","$349",R.drawable.iphone_5s,5));
        list_datos.add(new DatosVo("Bateria LG Generica","$349",R.drawable.lg,5));
        list_datos.add(new DatosVo("Bateria Nokia B1CT","$349",R.drawable.nokia_bl4ct,5));
        list_datos.add(new DatosVo("Bateria Sony XPeria","$349",R.drawable.sony_xperias,5));
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
