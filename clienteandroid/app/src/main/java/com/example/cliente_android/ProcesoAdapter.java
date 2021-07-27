package com.example.cliente_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            nombreProceso = (TextView) itemView.findViewById(R.id.nombreProceso);
            idProceso = (TextView) itemView.findViewById(R.id.idProceso);
            fechaProceso = (TextView) itemView.findViewById(R.id.fechaProceso);
            estadoProceso = (TextView) itemView.findViewById(R.id.estadoProceso);
            this.itemView = itemView;
        }
        public void asignarDatos(Proceso proceso) {
            nombreProceso.setText(proceso.getNombre());
            idProceso.setText(String.valueOf(proceso.getIdProces()));
            fechaProceso.setText(proceso.getFecha());
            estadoProceso.setText(proceso.getEstado());
            asignarColor(proceso);
        }
        public void asignarColor(Proceso proceso){
            String estado = proceso.getEstado();
            if (estado.equals("INICIADO")) {
                estadoProceso.setTextColor(Color.YELLOW);
                //itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.naranja));
            }else if(estado.equals("FINALIZADO")) {
                estadoProceso.setTextColor(Color.GREEN);
                //itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.verde));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proceso_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProcesoAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(procesos.get(position));
    }

    @Override
    public int getItemCount() {
        return procesos.size();
    }

}
