package com.store.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.Vo.CategoriasVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolderCategorias> implements View.OnClickListener {

    ArrayList<CategoriasVo> list_categorias;
    private View.OnClickListener listener;

    public AdapterCategorias(ArrayList<CategoriasVo> list_categorias) {
        this.list_categorias = list_categorias;
    }

    @NonNull
    @Override
    public ViewHolderCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_categorias, null, false);
        view.setOnClickListener(this);
        return new ViewHolderCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategorias holder, int position) {
        holder.nombreCategorias.setText(list_categorias.get(position).getNombreCategorias());
        holder.imagenCategorias.setImageResource(list_categorias.get(position).getImagenCategorias());
    }

    @Override
    public int getItemCount() {
        return list_categorias.size();
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

    public class ViewHolderCategorias extends RecyclerView.ViewHolder {

        TextView nombreCategorias;
        ImageView imagenCategorias;

        public ViewHolderCategorias(@NonNull View itemView) {
            super(itemView);
            nombreCategorias = itemView.findViewById(R.id.idNombreCategorias);
            imagenCategorias = itemView.findViewById(R.id.idImagenCategorias);
        }
    }
}
