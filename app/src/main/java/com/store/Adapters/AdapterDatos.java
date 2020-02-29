package com.store.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.Vo.DatosVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<DatosVo> list_datos;
    private View.OnClickListener listener;

    public AdapterDatos(ArrayList<DatosVo> list_datos) {
        this.list_datos = list_datos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombre.setText(list_datos.get(position).getNombre());
        holder.precio.setText(list_datos.get(position).getPrecio());
        holder.imagen.setImageResource(list_datos.get(position).getImagen());
        holder.stars.setRating(list_datos.get(position).getStars());
    }

    @Override
    public int getItemCount() {
        return list_datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView precio;
        ImageView imagen;
        RatingBar stars;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            precio = (TextView) itemView.findViewById(R.id.idPrecio);
            imagen = (ImageView) itemView.findViewById(R.id.idImagen);
            stars = (RatingBar) itemView.findViewById(R.id.ratingStars);
        }
    }
}
