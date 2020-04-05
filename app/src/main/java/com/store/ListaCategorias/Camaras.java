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
 * {@link Camaras.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Camaras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camaras extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<DatosVo> list_datos;
    RecyclerView recycler;
    private OnFragmentInteractionListener mListener;

    public Camaras() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Camaras.
     */
    // TODO: Rename and change types and number of parameters
    public static Camaras newInstance(String param1, String param2) {
        Camaras fragment = new Camaras();
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
        View v = inflater.inflate(R.layout.fragment_camaras, container, false);

        recycler = v.findViewById(R.id.recycler_id_camaras);
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
        list_datos.add(new DatosVo("Camara frontal Huawei P30 Lite","$229",R.drawable.camarafrontal_huawirp30lite,4));
        list_datos.add(new DatosVo("Camara frontal iPhone 7 Plus","$199",R.drawable.camara_frontal_iphone7plus,4));
        list_datos.add(new DatosVo("Camara frontal ZTE Apex2","$219",R.drawable.camarafrontal_zte_blade_apex2,4));
        list_datos.add(new DatosVo("Camara trasera Samsung S7","$349",R.drawable.camara_principal_trasera_samsung_galaxy_s7,4));
        list_datos.add(new DatosVo("Camara trasera Moto X","$349",R.drawable.camaratrasera_motorolamotox,4));
        list_datos.add(new DatosVo("Camara trasera Xiaomi N3 Pro","$349",R.drawable.camara_xiaomi_note3pro,4));
        list_datos.add(new DatosVo("Camara trasera LG G3","$349",R.drawable.cama_tracera_lg,4));
        list_datos.add(new DatosVo("Camara Trasera Samsung Galaxy","$349",R.drawable.flex_camara_samsung_galaxy,4));
        list_datos.add(new DatosVo("Camara Trasera iPhone 7","$349",R.drawable.flex_con_camara_frontal_de_7_mpx_para_apple_iphone_7g,4));
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
