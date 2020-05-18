package com.store.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;

public class Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView name;
    private TextView txtLastName;
    private TextView txtEmail;
    private TextView txtSex;
    private TextView txtPhone;
    private int index;

    private OnFragmentInteractionListener mListener;

    public Profile() {}

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userID = getArguments().getString("userID");
        getUserData(userID);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getUserData(String userID){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try{
                        String userFirstName = dataSnapshot.child("nombre").getValue().toString();
                        String userLastName = dataSnapshot.child("apellido").getValue().toString();
                        String userEmail = dataSnapshot.child("correo").getValue().toString();

                        name = getView().findViewById(R.id.id_nombre);
                        txtLastName = getView().findViewById(R.id.id_apellido);
                        txtEmail = getView().findViewById(R.id.id_email);
                        name.setText(userFirstName);
                        txtLastName.setText(userLastName);
                        txtEmail.setText(userEmail);

                        /**
                         * Explicacion del por que esta organizado de esta forma las variables,
                         * views y setText en EditProfile.java linea 135
                         * */
                        String userSex = dataSnapshot.child("sexo").getValue().toString();
                        txtSex = getView().findViewById(R.id.id_sexo);
                        index = userSex.indexOf(',');
                        userSex = userSex.substring(0, index);
                        txtSex.setText(userSex);

                        String userPhone = dataSnapshot.child("telefono").getValue().toString();
                        txtPhone = getView().findViewById(R.id.id_telefono);
                        txtPhone.setText(userPhone);
                    } catch (Exception e){}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
