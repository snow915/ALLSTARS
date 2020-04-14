package com.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistProfile newInstance(String param1, String param2) {
        ArtistProfile fragment = new ArtistProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String user_id = getArguments().getString("artist_id");
        get_artist_data(user_id);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void get_artist_data(String user_id) {
        final List user_data = new ArrayList();
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("data").child(user_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String artist_name = dataSnapshot.child("nombre").getValue().toString();
                    String artist_categories = dataSnapshot.child("categoria").getValue().toString();
                    String artist_image = dataSnapshot.child("imagen").getValue().toString();
                    String artist_biography = dataSnapshot.child("biografia").getValue().toString();
                    String artist_pass = dataSnapshot.child("pass").getValue().toString();
                    String artist_rating = dataSnapshot.child("puntaje").getValue().toString();
                    TextView name = getView().findViewById(R.id.artist_name);
                    TextView categories = getView().findViewById(R.id.artist_categories);
                    ImageView image = getView().findViewById(R.id.imageView3);
                    TextView biography = getView().findViewById(R.id.artist_biography);
                    TextView pass = getView().findViewById(R.id.artist_password);
                    TextView rating = getView().findViewById(R.id.artist_rating);
                    name.setText(artist_name);
                    categories.setText(artist_categories);
                    biography.setText(artist_biography);
                    if (artist_image.equals("")) {
                        image.setImageResource(R.drawable.blank_profile_picture_973460_640);
                    }

//                    int index = artist_sex.indexOf(',');
//                    artist_sex = artist_sex.substring(0, index);
                    pass.setText(artist_pass);
                    rating.setText(artist_rating);
                }
                else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_profile, container, false);
    }
}
