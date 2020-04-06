package com.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.store.Adapters.AdapterCategorias;
import com.store.ListaCategorias.Altavoces;
import com.store.ListaCategorias.Auriculares;
import com.store.ListaCategorias.Baterias;
import com.store.ListaCategorias.Botones;
import com.store.ListaCategorias.Camaras;
import com.store.ListaCategorias.Cristales;
import com.store.ListaCategorias.Display;
import com.store.ListaCategorias.Pantallas;
import com.store.ListaCategorias.Placas;
import com.store.Vo.CategoriasVo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Categorias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Categorias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categorias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<CategoriasVo> list_categorias;
    RecyclerView recycler;

    private OnFragmentInteractionListener mListener;

    public Categorias() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Categorias.
     */
    // TODO: Rename and change types and number of parameters
    public static Categorias newInstance(String param1, String param2) {
        Categorias fragment = new Categorias();
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
        View v = inflater.inflate(R.layout.fragment_categorias, container, false);
        recycler = v.findViewById(R.id.recycler_id_categorias);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        list_categorias = new ArrayList<CategoriasVo>();
        llenarDatos();
        AdapterCategorias adapter = new AdapterCategorias(list_categorias);
        recycler.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categoriaSeleccionada = list_categorias.get(recycler.getChildAdapterPosition(v)).getNombreCategorias();
                Fragment nuevoFragment = null;
                boolean fragment_seleccionado = false;

                switch(categoriaSeleccionada){
                    case "Musica":
                        nuevoFragment = new Pantallas();
                        fragment_seleccionado = true;
                        break;
                    case "Cine":
                        nuevoFragment = new Display();
                        fragment_seleccionado = true;
                        break;
                    case "Stand Up":
                        nuevoFragment = new Cristales();
                        fragment_seleccionado = true;
                        break;
                    case "Youtubers":
                        nuevoFragment = new Baterias();
                        fragment_seleccionado = true;
                        break;
                    case "Reality Shows":
                        nuevoFragment = new Placas();
                        fragment_seleccionado = true;
                        break;
                    case "Deportes":
                        nuevoFragment = new Altavoces();
                        fragment_seleccionado = true;
                        break;
                    case "Conductores":
                        nuevoFragment = new Botones();
                        fragment_seleccionado = true;
                        break;
                    case "Comedia":
                        nuevoFragment = new Camaras();
                        fragment_seleccionado = true;
                        break;
                    case "Telenovelas":
                        nuevoFragment = new Auriculares();
                        fragment_seleccionado = true;
                        break;
                    default:
                        Toast.makeText(getContext(), "NO SE ENCONTRO DICHA CATEGORÍA", Toast.LENGTH_SHORT).show();
                }

                if(fragment_seleccionado){
                    getFragmentManager().beginTransaction().replace(R.id.content_main, nuevoFragment).addToBackStack(null).commit();
                }
            }
        });
        return v;
    }

    public void llenarDatos(){
        list_categorias.add(new CategoriasVo("Musica",R.drawable.musica));
        list_categorias.add(new CategoriasVo("Cine",R.drawable.cine));
        list_categorias.add(new CategoriasVo("Stand Up",R.drawable.standup));
        list_categorias.add(new CategoriasVo("Youtubers",R.drawable.youtube));
        list_categorias.add(new CategoriasVo("Reality Shows",R.drawable.reallity));
        list_categorias.add(new CategoriasVo("Deportes",R.drawable.deportes));
        list_categorias.add(new CategoriasVo("Conductores",R.drawable.conductores));
        list_categorias.add(new CategoriasVo("Comedia",R.drawable.comedia));
        list_categorias.add(new CategoriasVo("Telenovelas",R.drawable.telenovelas));
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
