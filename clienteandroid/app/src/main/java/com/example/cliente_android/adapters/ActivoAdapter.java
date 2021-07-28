package com.example.cliente_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cliente_android.R;
import com.example.cliente_android.activities.DetalleUsuarioActivity;
import com.example.cliente_android.entidades.Activo;
import com.example.cliente_android.entidades.Proceso;
import com.example.cliente_android.entidades.Usuario;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivoAdapter extends RecyclerView.Adapter<ActivoAdapter.ViewHolder>{

    ArrayList<Activo> activos;
    Proceso proceso;
    ActivoAdapter activoAdapter;

    public ActivoAdapter(ArrayList<Activo> activos, Proceso proceso){
        this.activos = activos;
        this.proceso = proceso;
        activoAdapter = this;
    }
    public interface AdapterInteracciones{
        public void refrescar();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomAct, tvDesItem, tvIdItem, tvIdAct, tvObsAct, tvEstadoAct, tvRevisionAct;
        ImageView ivCirculoEstado;
        Context context;
        View itemView;
        String estado = "CORRECTO";
        EditText editText;
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String m_Text = "";
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.itemView = itemView;
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
        private void accionClick(Activo activo, Proceso proceso, int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    View inflater = LayoutInflater.from(itemView.getContext()).
                            inflate(R.layout.validar_layout, null);
                    builder.setTitle("Validar Activo");
                    final EditText input = (EditText)inflater.findViewById(R.id.etObservacion);
                    builder.setView(inflater);

                    // Set up the buttons
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                            validarActivo( activo, proceso, position);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    builder.show();
                }
            });
        }
        public void validarActivo(Activo activo, Proceso proceso, int position){
            OkHttpClient cliente = new OkHttpClient();
            String url = "http://192.168.0.50:5000/procesos/"+proceso.getIdProces()+"/activos/"+activo.getId();
            JSONObject enviar = new JSONObject();
            if (!m_Text.equals("")) {
                estado = "OBSERVACION";
            }
            try {
                enviar.put("estado_activo", estado);
                enviar.put("observacion_activo", m_Text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(enviar.toString(), JSON);
            Request request  = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();
            cliente.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }
            });
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
        holder.accionClick(activos.get(position), proceso, position);
    }

    @Override
    public int getItemCount() {
        return activos.size();
    }
}
