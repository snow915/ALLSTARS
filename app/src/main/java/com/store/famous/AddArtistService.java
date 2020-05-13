package com.store.famous;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.store.Carrusel;
import com.store.MainActivity;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.index;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddArtistService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddArtistService extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText serviceNameInput;
    private EditText serviceDescriptionInput;
    private EditText servicePriceInput;
    private EditText serviceMaximumTimeInput;
    private Button btnPublishService;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Service service;
    public AddArtistService() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddArtistService.
     */
    // TODO: Rename and change types and number of parameters
    public static AddArtistService newInstance(String param1, String param2) {
        AddArtistService fragment = new AddArtistService();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_artist_service, container, false);
        serviceNameInput = view.findViewById(R.id.serviceNameInput);
        serviceDescriptionInput = view.findViewById(R.id.serviceDescriptionInput);
        servicePriceInput = view.findViewById(R.id.servicePriceInput);
        serviceMaximumTimeInput = view.findViewById(R.id.serviceMaximumTimeInput);
        btnPublishService = view.findViewById(R.id.btnPublishService);
        btnPublishService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = serviceNameInput.getText().toString();
                String serviceDescription = serviceDescriptionInput.getText().toString();
                int servicePrice = Integer.valueOf(servicePriceInput.getText().toString());
                int serviceMaximumTime = Integer.valueOf(serviceMaximumTimeInput.getText().toString());
                publishService(serviceName, serviceDescription, servicePrice, serviceMaximumTime);
            }
        });
        return view;
    }

    private void publishService(String serviceName, String serviceDescription, int servicePrice, int serviceMaximumTime) {
        initFirebase();
        this.service = new Service();
        this.service.setNombre(serviceName);
        this.service.setDetalles(serviceDescription);
        this.service.setPrecio(servicePrice);
        this.service.setTiempoMaximo(serviceMaximumTime);
        this.databaseReference.push().setValue(service);
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Excelente!")
                .setContentText("Servicio publicado con éxito!")
                .setConfirmText("Ok!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
//                        Fragment index = new index();
//                        Fragment carousel = new Carrusel();
//                        AddArtistService current = (AddArtistService) getFragmentManager().findFragmentByTag("Current");
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
    private void initFirebase(){
        FirebaseApp.initializeApp(getContext());
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferencesApp preferences = new SharedPreferencesApp(getContext());
        preferences.loadPreferences();
        String artistName = preferences.getArtistUsername();
        this.databaseReference = this.firebaseDatabase.getReference("data").child(artistName).child("servicios");
    }
}
