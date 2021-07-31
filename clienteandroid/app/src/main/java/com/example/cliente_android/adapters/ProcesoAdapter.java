package com.example.cliente_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cliente_android.R;
import com.example.cliente_android.activities.DetalleProcesoActivity;
import com.example.cliente_android.entidades.Proceso;

import java.util.ArrayList;

public class ProcesoAdapter extends RecyclerView.Adapter<ProcesoAdapter.ViewHolder> {

    ArrayList<Proceso> procesos;
    private Context context;

    public ProcesoAdapter(ArrayList<Proceso> procesos) {
        this.procesos = procesos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProceso;
        TextView idProceso;
        TextView fechaProceso;
        TextView estadoProceso;
        ImageView ivCirculo;
        TextView barraLateral;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            nombreProceso = (TextView) itemView.findViewById(R.id.nombreProceso);
            idProceso = (TextView) itemView.findViewById(R.id.idProceso);
            fechaProceso = (TextView) itemView.findViewById(R.id.fechaProceso);
            estadoProceso = (TextView) itemView.findViewById(R.id.estadoProceso);
            ivCirculo = (ImageView) itemView.findViewById(R.id.circuloEstado);
            barraLateral = (TextView) itemView.findViewById(R.id.barraLateral);
            context = itemView.getContext();
        }

        public void verProceso(Proceso proceso) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetalleProcesoActivity.class);
                    intent.putExtra("EXTRA_ID_PROCESO", proceso.getIdProces());
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void asignarDatos(Proceso proceso) {
            nombreProceso.setText(proceso.getNombre());
            idProceso.setText("ID: " + String.valueOf(proceso.getIdProces()));
            fechaProceso.setText(proceso.getFecha());
            estadoProceso.setText(proceso.getEstado());
            asignarColor(proceso);
        }

        public void asignarColor(Proceso proceso) {
            String estado = proceso.getEstado();
            int cantObs = proceso.getCantObservaciones();
            if(cantObs != 0) {
                barraLateral.setBackgroundColor(context.getColor(R.color.naranja));
            }else {
                barraLateral.setBackgroundColor(Color.GREEN);
            }
            GradientDrawable background = (GradientDrawable) ivCirculo.getBackground();
            if (estado.equals("INICIADO") ) {
                background.setColor(context.getColor(R.color.verde));
            }else if(estado.equals("FINALIZADO")) {
                background.setColor(Color.GREEN);
            }
            else {
                barraLateral.setBackgroundColor(Color.LTGRAY);
                background.setColor(Color.LTGRAY);
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proceso_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProcesoAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(procesos.get(position));
        holder.verProceso(procesos.get(position));
    }

    @Override
    public int getItemCount() {
        return procesos.size();
    }

}
