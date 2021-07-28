package com.example.cliente_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cliente_android.R;
import com.example.cliente_android.activities.DetalleProcesoActivity;
import com.example.cliente_android.activities.DetalleUsuarioActivity;
import com.example.cliente_android.entidades.Proceso;
import com.example.cliente_android.entidades.Usuario;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    ArrayList<Usuario> usuarios;
    Proceso proceso;
    public UsuarioAdapter(ArrayList<Usuario> usuarios, Proceso proceso){
        this.usuarios = usuarios;
        this.proceso = proceso;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvApellido, tvCantActivos, tvCedula, tvCantObs;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvCedula = (TextView) itemView.findViewById(R.id.tvCedulaUsuario);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreUsuario);
            tvApellido = (TextView) itemView.findViewById(R.id.tvApellidoUsuario);
            tvCantActivos = (TextView) itemView.findViewById(R.id.tvCantActivosUsuario);
            tvCantObs = (TextView) itemView.findViewById(R.id.tvCantObsUsuario);
        }

        public void verUsuario(Usuario usuario){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetalleUsuarioActivity.class);
                    intent.putExtra("EXTRA_ID_PROCESO", proceso.getIdProces());
                    intent.putExtra("EXTRA_CEDULA_USUARIO", usuario.getCedula());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
        public void asignarDatos(Usuario usuario){
            tvCedula.setText(usuario.getCedula());
            tvNombre.setText(usuario.getNombre());
            tvApellido.setText(usuario.getApellido());
        }
        public void asignarColor(Usuario usuario){
            String cantObs = String.valueOf(usuario.getCantObs());
            GradientDrawable circulo = (GradientDrawable)tvCantObs.getBackground();
            if(usuario.getCantObs() != 0) {
                tvCantObs.setText(cantObs);
                circulo.setColor(context.getColor(R.color.naranja));
            }else {
                tvCantObs.setText("");
                circulo.setColor(context.getColor(R.color.verde));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.asignarDatos(usuarios.get(position));
        holder.asignarColor(usuarios.get(position));
        holder.verUsuario(usuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

}
