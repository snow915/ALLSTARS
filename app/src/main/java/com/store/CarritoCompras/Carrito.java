package com.store.CarritoCompras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.store.Adapters.AdapterDatos;
import com.store.InfoProducto;
import com.store.R;
import com.store.Vo.DatosVo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Carrito.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Carrito#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Carrito extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<DatosVo> list_datos;
    RecyclerView recycler;
    ImageView img;
    private OnFragmentInteractionListener mListener;

    public Carrito() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Carrito.
     */
    // TODO: Rename and change types and number of parameters
    public static Carrito newInstance(String param1, String param2) {
        Carrito fragment = new Carrito();
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
        View v = inflater.inflate(R.layout.fragment_carrito, container, false);

        recycler = v.findViewById(R.id.recycler_id_carrito);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list_datos = new ArrayList<DatosVo>();
        llenarDatos();
        AdapterDatos adapter = new AdapterDatos(list_datos, getContext());
        recycler.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = list_datos.get(recycler.getChildAdapterPosition(v)).getNombre();
                String precio = list_datos.get(recycler.getChildAdapterPosition(v)).getPrecio();
                String biografia = list_datos.get(recycler.getChildAdapterPosition(v)).getBiografia();
                String imagen = list_datos.get(recycler.getChildAdapterPosition(v)).getRutaImagen();
                int stars = list_datos.get(recycler.getChildAdapterPosition(v)).getStars();
                String username = list_datos.get(recycler.getChildAdapterPosition(v)).getUsername();
                Intent infoProducto = new Intent(getActivity(), InfoProducto.class);
                infoProducto.putExtra("nombre", nombre);
                infoProducto.putExtra("precio", precio);
                infoProducto.putExtra("image",imagen);
                infoProducto.putExtra("stars", stars);
                infoProducto.putExtra("biografia", biografia);
                infoProducto.putExtra("username" , username);
                startActivity(infoProducto);
            }
        });

        return v;
    }

    public void llenarDatos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), "carrito", null, 4);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT * FROM Carrito", null);
        if (fila.moveToFirst()) {
            do {
                String nombre = fila.getString(fila.getColumnIndex("tituloProducto"));
                String precio = fila.getString(fila.getColumnIndex("precioProducto"));
                String imagen = fila.getString(fila.getColumnIndex("idImagen"));
                String biografia = fila.getString(fila.getColumnIndex("biografia"));
                int stars = fila.getInt(fila.getColumnIndex("idStars"));
                String username = fila.getString(fila.getColumnIndex("username"));
                list_datos.add(new DatosVo(nombre, precio, biografia,imagen, stars, username));
            } while (fila.moveToNext());
        }
        db.close();
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
