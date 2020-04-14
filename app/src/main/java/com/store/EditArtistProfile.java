package com.store;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditArtistProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditArtistProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button update_artist = null;
    private ImageView artist_image = null;
    public StorageReference sref;
    public DatabaseReference dref;
    public static String artist_id;
    public static String artist_image_route;
    public static ConstraintLayout edit_artist_layout;
    private static final int GALLERY_INTENT = 1;
    public static Uri uri = null;
    public static boolean image_picked = false;
    public EditArtistProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditArtistProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditArtistProfile newInstance(String param1, String param2) {
        EditArtistProfile fragment = new EditArtistProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist_id = getArguments().getString("artist_id");;
        get_artist_data(artist_id);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void get_artist_data(String user_id) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("data").child(user_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String artist_name = dataSnapshot.child("nombre").getValue().toString();
                    String artist_categories = dataSnapshot.child("categoria").getValue().toString();
                    ArrayList<String> cat_list = new ArrayList<String>(Arrays.asList(artist_categories.split(", ")));
                    int total = edit_artist_layout.getChildCount();
                    for (int i = 0 ; i<total; i++) {
                        View v = edit_artist_layout.getChildAt(i);
                        if (v instanceof CheckBox) {
                            String element = ((CheckBox) v).getText().toString();
                            if (cat_list.contains(element))
                                ((CheckBox) v).setChecked(true);
                        }
                    }
                    artist_image_route = dataSnapshot.child("imagen").getValue().toString();
                    String artist_biography = dataSnapshot.child("biografia").getValue().toString();
                    String artist_pass = dataSnapshot.child("pass").getValue().toString();
                    TextView name = getView().findViewById(R.id.edit_artist_name);
//                    TextView categories = getView().findViewById(R.id.edit_ar);
                    artist_image = getView().findViewById(R.id.edit_artist_image);
                    TextView biography = getView().findViewById(R.id.edit_artist_biography);
                    TextView pass = getView().findViewById(R.id.edit_artist_password);
                    name.setText(artist_name);
                    biography.setText(artist_biography);
                    if (artist_image_route.equals("")) {
                        artist_image.setImageResource(R.drawable.blank_profile_picture_973460_640);
                    }
                    else{
                        Glide.with(getContext())
                                .load(artist_image_route)
                                .fitCenter()
                                .centerCrop()
                                .into(artist_image);
                    }
//                    int index = artist_sex.indexOf(',');
//                    artist_sex = artist_sex.substring(0, index);
                    pass.setText(artist_pass);
                } else {

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
        View view = inflater.inflate(R.layout.fragment_edit_artist_profile, container, false);
        edit_artist_layout = view.findViewById(R.id.edit_artist_layout);
        update_artist = view.findViewById(R.id.edit_artist_apply_changes);
        artist_image = view.findViewById(R.id.edit_artist_image);
        artist_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WE INDICATE THAT WE WANT TO OPEN THE PHOTO GALLERY
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // WE SET THE IMAGE TYPE THAT WE WANT
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Seleccione una aplicación"), 10);
            }
        });
        update_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText artist_name = getView().findViewById(R.id.edit_artist_name);
                EditText artist_password = getView().findViewById(R.id.edit_artist_password);
                TextInputEditText artist_biography = getView().findViewById(R.id.edit_artist_biography);
                update_artist_data(artist_name, artist_password, artist_biography, edit_artist_layout, uri, artist_image);
            }
        });
        return  view;
    }

    private void update_artist_data(EditText artist_name, EditText artist_password, TextInputEditText artist_biography, ConstraintLayout edit_artist_layout, Uri u, ImageView image) {
        final String artist_name_val = artist_name.getText().toString();
        final String artist_password_val = artist_password.getText().toString();
        final  String artist_biography_val = artist_biography.getText().toString();
        final ConstraintLayout layout = edit_artist_layout;
        final ImageView img = image;
        sref = FirebaseStorage.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference();
        if (u != null && image_picked){
            img.setDrawingCacheEnabled(true);
            img.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            final StorageReference file_reference = sref.child(System.currentTimeMillis()+"."+getFileExtension(u));
            UploadTask uploadTask = file_reference.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return file_reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String categories = "";
                        int total = layout.getChildCount();
                        for (int i = 0 ; i<total; i++) {
                            View v = layout.getChildAt(i);
                            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                                String element = ((CheckBox) v).getText().toString();
                                categories += element+", ";
                            }
                        }
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data").child(artist_id);
                        final Artistas artista = new Artistas();
                        artista.setNombre(artist_name_val);
                        artista.setPass(artist_password_val);
                        artista.setBiografia(artist_biography_val);
                        artista.setCategoria(categories);
                        artista.setUser(artist_id);
                        artista.setImagen(downloadUri.toString());
                        ref.setValue(artista);
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Congratulations!")
                                .setContentText("Data updated successfully!")
                                .setConfirmText("Ok!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(getContext(),MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            String categories = "";
            int total = layout.getChildCount();
            for (int i = 0 ; i<total; i++) {
                View v = layout.getChildAt(i);
                if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                    String element = ((CheckBox) v).getText().toString();
                    categories += element+", ";
                }
            }
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data").child(artist_id);
            final Artistas artista = new Artistas();
            artista.setNombre(artist_name_val);
            artista.setPass(artist_password_val);
            artista.setBiografia(artist_biography_val);
            artista.setCategoria(categories);
            artista.setUser(artist_id);
            artista.setImagen(artist_image_route);
            ref.setValue(artista);
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Congratulations!")
                    .setContentText("Data updated successfully!")
                    .setConfirmText("Ok!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            uri = data.getData();
            Glide.with(getContext())
                    .load(uri)
                    .fitCenter()
                    .centerCrop()
                    .into(artist_image);
            image_picked = true;
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
