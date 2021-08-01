package com.example.cliente_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cliente_android.R;
import com.example.cliente_android.adapters.ActivoAdapter;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivosActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    TextView tvNombreUsuario, tvApellidoUsuario, tvCedulaUsuario, tvCantObsUsuario, tvCantActUsuario;
    ImageView ivCirculoEstado;
    String cedulaUsuario;
    int idProceso;
    Usuario usuario = null;
    ArrayList<Activo> activos = new ArrayList<Activo>();
    ActivoAdapter activoAdapter;
    RecyclerView rvActivos;
    Context context;
    Proceso proceso;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_detalle);
        context = getApplicationContext();
        cedulaUsuario = getIntent().getStringExtra("EXTRA_CEDULA_USUARIO");
        idProceso = getIntent().getIntExtra("EXTRA_ID_PROCESO", 0);
        tvNombreUsuario = (TextView) findViewById(R.id.tvNomUDetalle);
        tvApellidoUsuario = (TextView) findViewById(R.id.tvApeUDetalle);
        tvCedulaUsuario = (TextView) findViewById(R.id.tvCedUDetalle);
        tvCantObsUsuario = (TextView) findViewById(R.id.tvCantObsUDetalle);
        tvCantActUsuario = (TextView) findViewById(R.id.tvCantActUDetalle);
        ivCirculoEstado = (ImageView) findViewById(R.id.ivCirEstadoAct);
        rvActivos = (RecyclerView) findViewById(R.id.rvActivos);
        rvActivos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fetchUsuario();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeActivos);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void fetchUsuario(){
        OkHttpClient cliente = new OkHttpClient();
        String url = "http://192.168.0.50:5000/procesos/"+idProceso;
        Request request  = new Request.Builder()
                .url(url)
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
                    JSONArray jsonActivos = jsonObject.getJSONArray("activos");
                    JSONArray jsonUsuarios = jsonObject.getJSONArray("usuarios");
                    proceso = Proceso.fromJson(jsonObject.getJSONObject("proceso"));
                    usuario = Usuario.fromJson(jsonUsuarios, cedulaUsuario);
                    //Log.e("JSON USU", "onResponse: "+jsonObject.toString());
                    activos = Activo.fromJson(jsonActivos, cedulaUsuario);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLayout();
                        activoAdapter = new ActivoAdapter(activos, proceso);
                        rvActivos.setAdapter(activoAdapter);
                    }
                });
            }
        });
    }

    public void setLayout(){
       tvCedulaUsuario.setText(usuario.getCedula());
       tvNombreUsuario.setText(usuario.getNombre());
       tvApellidoUsuario.setText(usuario.getApellido());
       tvCantActUsuario.setText("Activos: "+String.valueOf(activos.size()));
       tvCantObsUsuario.setText("Observaciones: "+usuario.getCantObs());
    }

    @Override
    public void onRefresh() {
        fetchUsuario();
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