package com.store.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.store.vo.DatosSolicitudVo;
import com.store.R;

import java.util.ArrayList;

public class AdapterDatosSolicitud extends RecyclerView.Adapter<AdapterDatosSolicitud.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<DatosSolicitudVo> list_datos;
    private View.OnClickListener listener;
    private Context context;
    private String typeRequest;

    public AdapterDatosSolicitud(ArrayList<DatosSolicitudVo> list_datos, Context context, String typeRequest) {
        this.list_datos = list_datos;
        this.context = context;
        this.typeRequest = typeRequest;
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
        holder.imagen.getLayoutParams().width = 300;
        if(typeRequest.equals("PAYED")){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_accept24dp);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.colorSuccess));
            holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_accept24dp));
        } else if(typeRequest.equals("ACCEPTED")){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_payment_black_24dp);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.colorSuccess));
            holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_payment_black_24dp));
        } else if(typeRequest.equals("REJECTED")){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_rejected24dp);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.colorCancel));
            holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rejected24dp));
        }
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
        ImageView imagen;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            fechaInicio = itemView.findViewById(R.id.idFechaInicio);
            horaInicio = itemView.findViewById(R.id.idHoraInicio);
            tipoEvento = itemView.findViewById(R.id.idTipoEvento);
            imagen = itemView.findViewById(R.id.idImagen);
        }
    }
}
