package com.example.cliente_android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cliente_android.R;
import com.example.cliente_android.entidades.Activo;

import java.util.ArrayList;

public class ActivoAdapter extends RecyclerView.Adapter<ActivoAdapter.ViewHolder>{

    ArrayList<Activo> activos;

    public ActivoAdapter(ArrayList<Activo> activos){this.activos = activos;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomAct, tvDesItem, tvIdItem, tvIdAct, tvObsAct, tvEstadoAct, tvRevisionAct;
        ImageView ivCirculoEstado;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNomAct = (TextView) itemView.findViewById(R.id.tvNomAct);
            tvIdAct = (TextView) itemView.findViewById(R.id.tvIdAct);
            tvObsAct = (TextView) itemView.findViewById(R.id.tvObsAct);
            tvEstadoAct = (TextView) itemView.findViewById(R.id.tvEstadoRevisionAct);
            tvRevisionAct = (TextView) itemView.findViewById(R.id.tvRevisionAct);
            tvDesItem = (TextView) itemView.findViewById(R.id.tvDesItem);
            tvIdItem = (TextView) itemView.findViewById(R.id.tvIdItem);
            ivCirculoEstado = (ImageView)itemView.findViewById(R.id.ivCirEstadoAct);
        }

        public void asignarDatos(Activo activo){
           tvNomAct.setText(activo.getNombreItem());
           tvIdAct.setText("ACTIVO: "+activo.getId());
           tvObsAct.setText(activo.getObservacion());
           tvEstadoAct.setText(activo.getEstado());
           if (activo.getRevision() == 1) {
              tvRevisionAct.setText("REVISADO");
              tvRevisionAct.setTextColor(context.getColor(R.color.verde_oscuro));
           }else {
              tvRevisionAct.setText("POR REVISAR");
           }
           tvDesItem.setText(activo.getDesItem());
           tvIdItem.setText("ITEM: "+activo.getIdItem());
        }
        private void asignarColor(Activo activo){
            String estado = activo.getEstado();
            GradientDrawable background = (GradientDrawable) ivCirculoEstado.getBackground();
            if (estado.equals("CORRECTO") ) {
                tvEstadoAct.setTextColor(context.getColor(R.color.verde_oscuro));
                background.setColor(context.getColor(R.color.verde));
            }else if(estado.equals("OBSERVACION")) {
                tvEstadoAct.setTextColor(context.getColor(R.color.naranja));
                background.setColor(context.getColor(R.color.naranja));
            }
            else {
                background.setColor(Color.LTGRAY);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activo_item, parent, false);
        return new ActivoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.asignarDatos(activos.get(position));
        holder.asignarColor(activos.get(position));
    }

    @Override
    public int getItemCount() {
        return activos.size();
    }
}
