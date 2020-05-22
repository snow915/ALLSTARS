package com.store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.store.vo.DatosSolicitudVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterDatosSolicitudUsuario extends RecyclerView.Adapter<AdapterDatosSolicitudUsuario.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<DatosSolicitudVo> list_datos;
    private View.OnClickListener listener;
    private Context context;

    public AdapterDatosSolicitudUsuario(ArrayList<DatosSolicitudVo> list_datos, Context context) {
        this.list_datos = list_datos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_solicitud, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombreFamoso.setText(list_datos.get(position).getNombreFamoso());
        holder.fechaInicio.setText(list_datos.get(position).getFechaInicio());
        holder.horaInicio.setText(list_datos.get(position).getHoraInicio());
        Glide.with(context)
                .load(list_datos.get(position).getImagenFamoso())
                .fitCenter()
                .centerCrop()
                .into(holder.imagen);
        //holder.imagen.setImageResource(list_datos.get(position).getRutaImagen());
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

        TextView nombreFamoso;
        TextView fechaInicio;
        TextView horaInicio;
        ImageView imagen;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreFamoso = itemView.findViewById(R.id.idFechaInicio);
            fechaInicio = itemView.findViewById(R.id.idHoraInicio);
            horaInicio = itemView.findViewById(R.id.idTipoEvento);
            imagen = itemView.findViewById(R.id.idImagen);
        }
    }
}
