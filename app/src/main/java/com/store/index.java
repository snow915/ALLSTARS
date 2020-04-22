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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.adapters.AdapterDatos;
import com.store.vo.DatosVo;
import java.util.ArrayList;

public class index extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<DatosVo> listData;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private OnFragmentInteractionListener mListener;

    public index() {}

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
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        fillData(v);
        return v;
    }

    private void fillData(View v){
        final View view = v;
        listData = new ArrayList<DatosVo>();
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    listData.add(
                            new DatosVo(postSnapshot.child("nombre").getValue().toString(),
                                    postSnapshot.child("categoria").getValue().toString(),
                                    postSnapshot.child("biografia").getValue().toString(),
                                    postSnapshot.child("imagen").getValue().toString(),
                                    Integer.valueOf(postSnapshot.child("puntaje").getValue().toString()),
                                    postSnapshot.child("user").getValue().toString()
                            )
                    );
                }
                recycler = view.findViewById(R.id.recycler_id);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                AdapterDatos adapter = new AdapterDatos(listData, getContext());
                recycler.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = listData.get(recycler.getChildAdapterPosition(v)).getNombre();
                        String biography = listData.get(recycler.getChildAdapterPosition(v)).getBiografia();
                        String image = listData.get(recycler.getChildAdapterPosition(v)).getRutaImagen();
                        int stars = listData.get(recycler.getChildAdapterPosition(v)).getStars();
                        String artistUsername = listData.get(recycler.getChildAdapterPosition(v)).getUsername();
                        Intent infoProducto = new Intent(getActivity(), InfoProducto.class);
                        infoProducto.putExtra("name", name);
                        infoProducto.putExtra("image",image);
                        infoProducto.putExtra("stars", stars);
                        infoProducto.putExtra("biography", biography);
                        infoProducto.putExtra("username" , artistUsername);
                        startActivity(infoProducto);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}