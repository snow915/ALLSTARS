package com.store.famous;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.viewHolders.ViewHolderServices;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDeleteService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDeleteService extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recycler;
    private DatabaseReference reference;
    private SharedPreferencesApp preferencesApp;
    private String artistUserName;
    private FirebaseRecyclerAdapter<Service, ViewHolderServices> adapter;
    private FirebaseRecyclerOptions<Service> options;

    public FragmentDeleteService() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDeleteService.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDeleteService newInstance(String param1, String param2) {
        FragmentDeleteService fragment = new FragmentDeleteService();
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
        View view = inflater.inflate(R.layout.fragment_delete_service, container, false);
        fillData(view);
        return view;
    }

    private void fillData(View v){
        final View view = v;
        preferencesApp = new SharedPreferencesApp(getContext());
        preferencesApp.loadPreferences();
        artistUserName = preferencesApp.getArtistUsername();
        recycler = v.findViewById(R.id.recycler_delete_services);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reference = FirebaseDatabase.getInstance().getReference().child("data").child(artistUserName).child("servicios");
        options = new FirebaseRecyclerOptions.Builder<Service>().setQuery(reference, Service.class).build();
        adapter = new FirebaseRecyclerAdapter<Service, ViewHolderServices>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderServices holder, int position, @NonNull Service model) {
                holder.serviceName.setText(model.getNombre());
                final String name = model.getNombre();
                holder.servicePrice.setText(""+model.getPrecio());
                holder.serviceDescription.setText(model.getDetalles());
                holder.serviceMaximumTime.setText(""+model.getTiempoMaximo());
                holder.btnDeleteService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("¿Realmente deseas eliminar este servicio?")
                                .setContentText("Esta acción no se puede revertir")
                                .setCancelText("No")
                                .setConfirmText("Sí")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        DatabaseReference df = FirebaseDatabase.getInstance()
                                                .getReference("data")
                                                .child(artistUserName)
                                                .child("servicios");
                                        Query query = df.orderByChild("nombre").equalTo(name);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ds.getRef().removeValue();
                                                }
                                                Toast.makeText(view.getContext(), "Eliminación satisfactoria", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(view.getContext(), "ERROR "+databaseError.getMessage() , Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderServices onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_services_item, parent, false);
                return new ViewHolderServices(v);
            }
        };
        adapter.startListening();
        recycler.setAdapter(adapter);
        //        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot element : dataSnapshot.getChildren()) {
//                    itemNames.add(element.child("nombre").getValue().toString());
//                    itemPrices.add(element.child("precio").getValue().toString());
//                    itemDescriptions.add(element.child("detalles").getValue().toString());
//                    itemMaximumTimes.add(element.child("tiempoMaximo").getValue().toString());
//                    itemIds.add(element.getKey());
//                }
//                recycler = view.findViewById(R.id.recycler_delete_services);
//                recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                AdapterServices adapter = new AdapterServices(itemNames, itemPrices, itemDescriptions, itemMaximumTimes, itemIds, getContext(), CONTEXT);
//                recycler.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
