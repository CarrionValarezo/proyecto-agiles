package com.example.cliente_android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cliente_android.R;
import com.example.cliente_android.adapters.UsuarioAdapter;
import com.example.cliente_android.entidades.Proceso;
import com.example.cliente_android.entidades.Usuario;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UsuariosActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    TextView tvNombre, tvFecha, tvId, tvEstado, tvCantUsuarios, tvCantActivos, tvCantObs;
    int idProceso;
    ImageView ivCirculoDetalle;
    Proceso proceso = null;
    Context context;
    ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
    RecyclerView rvUsuarios;
    UsuarioAdapter usuarioAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceso_detalle);
        preferences = getSharedPreferences("com.example.cliente_android", Context.MODE_PRIVATE);
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
        rvUsuarios = (RecyclerView)findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fetchProceso();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeUsuarios);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void fetchProceso(){
        String auth = preferences.getString("session", null);
        if (auth != null ) {
            OkHttpClient cliente = new OkHttpClient();
            String url = "http://192.168.0.50:5000/procesos/"+idProceso;
            Request request  = new Request.Builder()
                    .url(url)
                    .header("Authorization", auth)
                    .build();
            cliente.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    errorConexion();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        proceso = Proceso.fromJson(jsonObject.getJSONObject("proceso"));
                        usuarios = Usuario.fromJson(jsonObject.getJSONArray("usuarios"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setLayout();
                            asignarColor(proceso);
                            usuarioAdapter = new UsuarioAdapter(usuarios, proceso);
                            rvUsuarios.setAdapter(usuarioAdapter);
                        }
                    });
                }
            });
        }

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

    @Override
    public void onRefresh() {
        fetchProceso();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void errorConexion(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "No se ha podido conectar con el servicio,\nintentelo más tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
