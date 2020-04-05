package com.store.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.Vo.CarritoVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterCarrito extends RecyclerView.Adapter<AdapterCarrito.ViewHolderCarrito> implements View.OnClickListener {

    ArrayList<CarritoVo> list_carrito;
    private View.OnClickListener listener;

    public AdapterCarrito(ArrayList<CarritoVo> list_carrito) {
        this.list_carrito = list_carrito;
    }

    @NonNull
    @Override
    public ViewHolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_carrito, null, false);
        view.setOnClickListener(this);
        return new ViewHolderCarrito(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, int position) {
        holder.nombreCarrito.setText(list_carrito.get(position).getNombreCarrito());
        holder.imagenCarrito.setImageResource(list_carrito.get(position).getImagenCarrito());
    }

    @Override
    public int getItemCount() {
        return list_carrito.size();
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

    public class ViewHolderCarrito extends RecyclerView.ViewHolder {

        TextView nombreCarrito;
        ImageView imagenCarrito;

        public ViewHolderCarrito(@NonNull View itemView) {
            super(itemView);
            nombreCarrito = itemView.findViewById(R.id.idNombre);
            imagenCarrito = itemView.findViewById(R.id.idImagen);
        }
    }
}
