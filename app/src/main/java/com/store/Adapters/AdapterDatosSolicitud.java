package com.store.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.store.InfoProducto;
import com.store.Vo.DatosSolicitudVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterDatosSolicitud extends RecyclerView.Adapter<AdapterDatosSolicitud.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<DatosSolicitudVo> list_datos;
    private View.OnClickListener listener;
    private Context context;

    public AdapterDatosSolicitud(ArrayList<DatosSolicitudVo> list_datos, Context context) {
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
        holder.fechaInicio.setText(list_datos.get(position).getFechaInicio());
        holder.horaInicio.setText(list_datos.get(position).getHoraInicio());
        holder.tipoEvento.setText(list_datos.get(position).getTipoEvento());
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

        TextView fechaInicio;
        TextView horaInicio;
        TextView tipoEvento;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            fechaInicio = itemView.findViewById(R.id.idFechaInicio);
            horaInicio = itemView.findViewById(R.id.idHoraInicio);
            tipoEvento = itemView.findViewById(R.id.idTipoEvento);
        }
    }
}
