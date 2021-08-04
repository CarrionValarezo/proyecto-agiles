package com.example.cliente_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProcesosActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ClienteServicio cliente;
    ArrayList<Proceso> procesosViews = new ArrayList<Proceso>();
    RecyclerView recycler;
    ProcesoAdapter adapter;
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    Button btnIniciarSesion;
    EditText etCedula, etPassword;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("com.example.cliente_android", Context.MODE_PRIVATE);
        context = this.getApplicationContext();
        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeProceso);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (preferences.contains("session")) {
            fetchProcesos();
            btnSalir();
        } else {
            btnIniciar();
            Toast.makeText(context, "Debes iniciar sesion", Toast.LENGTH_LONG);
        }
    }

    private void btnSalir() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnIniciarSesion = (Button) findViewById(R.id.btnSesion);
                btnIniciarSesion.setText("Cerrar Sesion");
                btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferences.edit().remove("session").commit();
                        finish();
                    }
                });
            }
        });

    }

    private void btnIniciar() {
        btnIniciarSesion = (Button) findViewById(R.id.btnSesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProcesosActivity.this);
                View inflater = getLayoutInflater().inflate(R.layout.login_modal, null);
                builder.setView(inflater);
                builder.setPositiveButton("Iniciar Sesion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etCedula = (EditText) inflater.findViewById(R.id.editTextTextPersonName);
                        etPassword = (EditText) inflater.findViewById(R.id.editTextTextPassword);
                        String cedula = etCedula.getText().toString();
                        String password = etPassword.getText().toString();
                        login(cedula, password);
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

    private void login(String cedula, String password) {
        String auth = Credentials.basic(cedula, password);
        OkHttpClient cliente = new OkHttpClient();
        String url = "http://192.168.0.50:5000/admin/login";
        Request request = new Request.Builder()
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
                String texto = "";
                if (response.code() == 200) {
                    texto = "¡Inicio sesion correctamente!";
                    preferences.edit().putString("session", auth).commit();
                    fetchProcesos();
                    btnSalir();
                } else {
                    texto = "¡Usuario o contraseña incorrectos!";
                }
                String finalTexto = texto;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, finalTexto, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void fetchProcesos() {
        String auth = preferences.getString("session", null);
        if (auth != null) {
            OkHttpClient cliente = new OkHttpClient();
            String url = "http://192.168.0.50:5000/procesos";
            Request request = new Request.Builder()
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
        } else {
            Toast.makeText(context, "Debes iniciar sesion", Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onRefresh() {
        fetchProcesos();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void errorConexion() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "No se ha podido conectar con el servicio,\nintentelo más tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }

}