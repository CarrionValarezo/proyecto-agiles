package com.example.cliente_android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetalleProcesoActivity extends Activity {
    TextView tvNombre, tvFecha, tvId, tvEstado, tvCantUsuarios, tvCantActivos, tvCantObs;
    int idProceso;
    ImageView ivCirculoDetalle;
    Proceso proceso = null;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceso_detalle);
        idProceso = getIntent().getIntExtra("EXTRA_ID_PROCESO", 0);
        context = getApplicationContext();
        tvNombre = (TextView)findViewById(R.id.tvNombreDetalle);
        tvId = (TextView)findViewById(R.id.tvIdDetalle);
        tvFecha = (TextView)findViewById(R.id.tvFechaDetalle);
        tvEstado = (TextView)findViewById(R.id.tvEstadoDetalle);
        tvCantUsuarios = (TextView)findViewById(R.id.tvCantUsuarios);
        tvCantActivos = (TextView)findViewById(R.id.tvCantActivos);
        tvCantObs = (TextView)findViewById(R.id.tvCantObservaciones);
        ivCirculoDetalle = (ImageView)findViewById(R.id.ivCirculoDetalle);
        fetchProceso();
    }

    public void fetchProceso(){
        OkHttpClient cliente = new OkHttpClient();
        String url = "http://192.168.0.50:5000/procesos/"+idProceso;
        Request request  = new Request.Builder()
                .url(url)
                .build();
        cliente.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    proceso = Proceso.fromJson(jsonObject.getJSONObject("proceso"));
                    Log.e("JSON", "onSuccess: "+proceso.getNombre());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLayout();
                        asignarColor(proceso);
                    }
                });
            }
        });
    }
    private void setLayout(){
        tvNombre.setText(proceso.getNombre());
        tvId.setText("ID: "+String.valueOf(proceso.getIdProces()));
        tvFecha.setText(proceso.getFecha());
        tvEstado.setText(proceso.getEstado());
        tvCantUsuarios.setText("Usuarios: "+proceso.getCantUsuarios());
        tvCantActivos.setText("Activos: "+proceso.getCantActivos());
        tvCantObs.setText("Observaciones: "+proceso.getCantObservaciones());

    }

    private void asignarColor(Proceso proceso){
        String estado = proceso.getEstado();
        GradientDrawable background = (GradientDrawable) ivCirculoDetalle.getBackground();
        if (estado.equals("INICIADO") ) {
            background.setColor(context.getColor(R.color.verde));
        }else if(estado.equals("FINALIZADO")) {
            background.setColor(Color.GREEN);
        }
        else {
            background.setColor(Color.LTGRAY);
        }
    }
}
