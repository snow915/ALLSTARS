package com.store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.EGLDisplay;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfile extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button update = null;
    private OnFragmentInteractionListener mListener;

    public EditProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfile newInstance(String param1, String param2) {
        EditProfile fragment = new EditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String user_id = getArguments().getString("user_id");
        get_user_data(user_id);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        update = view.findViewById(R.id.apply_changes);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = getView().findViewById(R.id.id_nombre);
                EditText user = getView().findViewById(R.id.username);
                EditText pass = getView().findViewById(R.id.password);
                EditText last_name = getView().findViewById(R.id.id_apellido);
                EditText email = getView().findViewById(R.id.id_email);
                Spinner sex = getView().findViewById(R.id.id_sexo);
                EditText phone = getView().findViewById(R.id.id_telefono);
                if (validations(name, user, pass, last_name, email, sex, phone)){
                    updateUser(name, user, pass, last_name, email, sex, phone);
                }
            }
        });
        return view;
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
    public void get_user_data(String userd){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userd);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String user_name = dataSnapshot.child("nombre").getValue().toString();
                    String user = dataSnapshot.child("user").getValue().toString();
                    String pass = dataSnapshot.child("pass").getValue().toString();
                    String user_last_name = dataSnapshot.child("apellido").getValue().toString();
                    String user_email = dataSnapshot.child("correo").getValue().toString();
                    String user_sex = dataSnapshot.child("sexo").getValue().toString();
                    String user_phone = dataSnapshot.child("telefono").getValue().toString();
                    EditText name = getView().findViewById(R.id.id_nombre);
                    EditText username = getView().findViewById(R.id.username);
                    EditText password = getView().findViewById(R.id.password);
                    EditText last_name = getView().findViewById(R.id.id_apellido);
                    EditText email = getView().findViewById(R.id.id_email);
                    Spinner sex = getView().findViewById(R.id.id_sexo);
                    EditText phone = getView().findViewById(R.id.id_telefono);
                    name.setText(user_name);
                    username.setText(user);
                    password.setText(pass);
                    last_name.setText(user_last_name);
                    email.setText(user_email);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.array_sex, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sex.setAdapter(adapter);
                    int index = user_sex.indexOf(",");
                    int item_pos = Integer.parseInt(user_sex.substring(index+1));
                    sex.setSelection(item_pos);
                    phone.setText(user_phone);
                }
                else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void updateUser(EditText name, EditText user, EditText pass, EditText last_name, EditText email, Spinner sex, EditText phone){
        String name_value = name.getText().toString();
        String user_value = user.getText().toString();
        String pass_value = pass.getText().toString();
        String last_name_value = last_name.getText().toString();
        String email_value = email.getText().toString();
        String sex_value = sex.getSelectedItem().toString()+","+sex.getSelectedItemPosition();
        String phone_value = phone.getText().toString();
        String user_id = getArguments().getString("user_id");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios").child(user_id);
        Usuarios usuario= new Usuarios();
        usuario.setNombre(name_value);
        usuario.setUser(user_value);
        usuario.setApellido(last_name_value);
        usuario.setPass(pass_value);
        usuario.setCorreo(email_value);
        usuario.setSexo(sex_value);
        usuario.setTelefono(phone_value);
        ref.setValue(usuario);
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Congratulations!")
                .setContentText("Data updated successfully!")
                .setConfirmText("Ok!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        EditProfile myFragment = (EditProfile)getFragmentManager().findFragmentByTag("Current");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(myFragment).attach(myFragment).commit();
                    }
                })
                .show();
    }
    private boolean validations(EditText nombre, EditText user, EditText pass, EditText apellido, EditText email, Spinner sexo, EditText telefono){
        if(nombre.getText().toString().equals("")){
            nombre.setError("No puede estar vacío");
            return false;
        }
        if(apellido.getText().toString().equals("")){
            apellido.setError("No puede estar vacío");
            return false;
        }
        if(email.getText().toString().equals("")){
            email.setError("No puede estar vacío");
            return false;
        }
        if(telefono.getText().toString().equals("")){
            telefono.setError("No puede estar vacío");
            return false;
        }
        if(telefono.getText().toString().length() < 10){
            telefono.setError("Faltan dígitos");
            return false;
        }
        if(sexo.getSelectedItem().toString().equals("Gender")){
            TextView errorTextview = (TextView) sexo.getSelectedView();
            errorTextview.setError("Opción inválida");
            return false;
        }
        if(user.getText().toString().equals("")){
            user.setError("No puede estar vacío");
            return false;
        }
        if(user.getText().toString().length() < 4){
            user.setError("4 caracteres mínimo");
            return false;
        }
        if(pass.getText().toString().equals("")){
            pass.setError("No puede estar vacío");
            return false;
        }
        if(pass.getText().toString().length() < 8){
            pass.setError("8 caracteres mínimo");
            return false;
        }
        return true;
    }
}
