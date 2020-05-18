package com.store.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.store.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfile extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button update = null;
    private EditText edtxtName;
    private EditText edtxtUser;
    private EditText edtxtPass;
    private EditText edtxtLastName;
    private EditText edtxtEmail;
    private Spinner spinSex;
    private EditText edtxtPhone;

    private OnFragmentInteractionListener mListener;

    public EditProfile() { }

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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        update = view.findViewById(R.id.apply_changes);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                associateIds();
                String nameValue = edtxtName.getText().toString();
                String userValue = edtxtUser.getText().toString();
                String passValue = edtxtPass.getText().toString();
                String lastNameValue = edtxtLastName.getText().toString();
                String emailValue = edtxtEmail.getText().toString();
                String sexValue = spinSex.getSelectedItem().toString()+","+spinSex.getSelectedItemPosition();
                String phoneValue = edtxtPhone.getText().toString();
                if (validations(nameValue, userValue, passValue, lastNameValue, emailValue, sexValue, phoneValue)){
                    updateUser(nameValue, userValue, passValue, lastNameValue, emailValue, sexValue, phoneValue);
                }
            }
        });
        return view;
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

    public void getUserData(String userID){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try{
                        String userName = dataSnapshot.child("nombre").getValue().toString();
                        String userLastName = dataSnapshot.child("apellido").getValue().toString();
                        String userEmail = dataSnapshot.child("correo").getValue().toString();

                        associateIds();
                        edtxtName.setText(userName);
                        edtxtLastName.setText(userLastName);
                        edtxtEmail.setText(userEmail);

                        /*
                        userSex, userPhone y pass lo mandamos a traer en este punto
                        porque cuando te logueas con Google esos datos no se guardan
                        en RealtimeDatabase, por lo tanto son nulos, y se declaran
                        en esta parte para cuando nos logueamos con cuenta Google se carguen
                        los datos existentes (userName, userLastName, userEmail (arriba)),
                        y cuando llegue a este punto (que son nulos) se vaya al catch.
                        De lo contrario cuando encuentre un nulo va a ir al catch y no mostraria
                        los datos en pantalla
                        */
                        String userSex = dataSnapshot.child("sexo").getValue().toString();
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                                R.array.array_sex, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinSex.setAdapter(adapter);
                        int index = userSex.indexOf(",");
                        int item_pos = Integer.parseInt(userSex.substring(index+1));
                        spinSex.setSelection(item_pos);

                        String userPhone = dataSnapshot.child("telefono").getValue().toString();
                        String pass = dataSnapshot.child("pass").getValue().toString();

                        edtxtPhone.setText(userPhone);
                        edtxtPass.setText(pass);
                    } catch (Exception e){

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    //checar esta funcion para actualizar los datos, tambien en firebase auth
    private void updateUser(String name, String user, String pass, String lastName, String email, String sex, String phone){
        String username = getArguments().getString("username");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios").child(username);
        Usuarios usuario= new Usuarios();
        usuario.setNombre(name);
        usuario.setUser(user);
        usuario.setApellido(lastName);
        usuario.setPass(pass);
        usuario.setCorreo(email);
        usuario.setSexo(sex);
        usuario.setTelefono(phone);
        ref.setValue(usuario);
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Datos actualizados")
                .setConfirmText("Entendido")
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

    private boolean validations(String nombre, String user, String pass, String apellido, String email, String sexo, String telefono){
        if(nombre.equals("")){
            edtxtName.setError("No puede estar vacío");
            return false;
        }
        if(apellido.equals("")){
            edtxtLastName.setError("No puede estar vacío");
            return false;
        }
        if(email.equals("")){
            edtxtEmail.setError("No puede estar vacío");
            return false;
        }
        if(telefono.equals("")){
            edtxtPhone.setError("No puede estar vacío");
            return false;
        }
        if(telefono.length() < 10){
            edtxtPhone.setError("Faltan dígitos");
            return false;
        }
        if(spinSex.getSelectedItem().toString().equals("Gender")){
            TextView errorTextview = (TextView) spinSex.getSelectedView();
            errorTextview.setError("Opción inválida");
            return false;
        }
        if(user.equals("")){
            edtxtUser.setError("No puede estar vacío");
            return false;
        }
        if(edtxtUser.length() < 4){
            edtxtUser.setError("4 caracteres mínimo");
            return false;
        }
        if(pass.equals("")){
            edtxtPass.setError("No puede estar vacío");
            return false;
        }
        if(pass.length() < 8){
            edtxtPass.setError("8 caracteres mínimo");
            return false;
        }
        return true;
    }

    private void associateIds(){
        edtxtName = getView().findViewById(R.id.id_nombre);
        edtxtUser = getView().findViewById(R.id.username);
        edtxtPass = getView().findViewById(R.id.password);
        edtxtLastName = getView().findViewById(R.id.id_apellido);
        edtxtEmail = getView().findViewById(R.id.id_email);
        spinSex = getView().findViewById(R.id.id_sexo);
        edtxtPhone = getView().findViewById(R.id.id_telefono);
    }
}
