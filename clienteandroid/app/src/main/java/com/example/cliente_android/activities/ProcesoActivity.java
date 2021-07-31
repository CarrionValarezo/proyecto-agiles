package com.example.cliente_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.cliente_android.ClienteServicio;
import com.example.cliente_android.adapters.ProcesoAdapter;
import com.example.cliente_android.R;
import com.example.cliente_android.entidades.Proceso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProcesoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ClienteServicio cliente;
    ArrayList<Proceso> procesosViews = new ArrayList<Proceso>();
    RecyclerView recycler;
    ProcesoAdapter adapter;
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        fetchProcesos();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeProceso);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void fetchProcesos() {
        OkHttpClient cliente = new OkHttpClient();
        String url = "http://192.168.0.50:5000/procesos";
        Request request = new Request.Builder()
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
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    procesosViews = Proceso.fromJson(jsonArray);
                    for (Proceso proceso : procesosViews) {
                        //Log.e("JSON", "onSuccess: "+proceso.getNombre());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ProcesoAdapter(procesosViews);
                        recycler.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchProcesos();
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