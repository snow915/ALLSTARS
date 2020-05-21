package com.store.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.R;

public class ViewHolderArtistas extends RecyclerView.ViewHolder {
    public TextView nombre;
    public TextView precio;
    public ImageView imagen;
    public RatingBar stars;

    public ViewHolderArtistas(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.idNombre);
        precio = itemView.findViewById(R.id.idPrecio);
        imagen = itemView.findViewById(R.id.idImagen);
        stars = itemView.findViewById(R.id.ratingStars);
    }
}
