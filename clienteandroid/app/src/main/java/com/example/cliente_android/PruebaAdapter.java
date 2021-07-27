package com.example.cliente_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PruebaAdapter extends RecyclerView.Adapter<PruebaAdapter.ViewHolder>{
    private ArrayList<String> datos;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.nombreProceso);
        }

        public TextView getTextView(){
            return textView;
        }
    }

    public PruebaAdapter(ArrayList<String> datos){
        this.datos = datos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proceso_layout, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView().setText(datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

}
