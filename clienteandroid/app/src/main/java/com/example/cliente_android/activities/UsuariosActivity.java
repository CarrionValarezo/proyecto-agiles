package com.example.cliente_android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cliente_android.R;
import com.example.cliente_android.adapters.UsuarioAdapter;
import com.example.cliente_android.entidades.Activo;
import com.example.cliente_android.entidades.Proceso;
import com.example.cliente_android.entidades.Usuario;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UsuariosActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    TextView tvNombre, tvFecha, tvId, tvEstado, tvCantUsuarios, tvCantActivos, tvCantObs;
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    int idProceso;
    ImageView ivCirculoDetalle;
    Proceso proceso = null;
    Context context;
    ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
    ArrayList<Activo> activos = new ArrayList<Activo>();
    RecyclerView rvUsuarios;
    UsuarioAdapter usuarioAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;
    String m_Text = "";

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

        Button btnScanner = (Button)findViewById(R.id.btnScanner);
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               scanCode();
            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Escanee un codigo de barras");
        integrator.setOrientationLocked(false);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        boolean encontrado = false;
        if (resultado != null) {
            if (resultado.getContents() != null) {
                for(Activo activo : activos) {
                    if(resultado.getContents().equals(activo.getId())) {
                        Usuario usuario = activo.getUsuario();
                        View inflater = LayoutInflater.from(this).
                                inflate(R.layout.validar_scanner, null);
                        TextView cedula, nombre, apellido, idAct, nombreItem, desItem;
                        EditText input;
                        cedula = (TextView)inflater.findViewById(R.id.tvCedUsuVal);
                        nombre = (TextView)inflater.findViewById(R.id.tvNomUsuVal);
                        apellido = (TextView)inflater.findViewById(R.id.tvApeUsuVal);
                        idAct = (TextView)inflater.findViewById(R.id.tvIdActVal);
                        nombreItem = (TextView)inflater.findViewById(R.id.tvNomItemVal);
                        desItem = (TextView)inflater.findViewById(R.id.tvDesItemVal);
                        input = (EditText)inflater.findViewById(R.id.etObservacionValScanner);


                        cedula.setText(usuario.getCedula());
                        nombre.setText(usuario.getNombre());
                        apellido.setText(usuario.getApellido());

                        idAct.setText(activo.getId());
                        nombreItem.setText(activo.getNombreItem());
                        desItem.setText(activo.getDesItem());

                        builder.setView(inflater);
                        builder.setTitle("Resultados");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                                validarActivo(activo);
                            }

                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        encontrado = true;
                    }
                }
                if(!encontrado) {
                    builder.setMessage("No se han encontrado resultados");
                    builder.setTitle("Resultados");
                    builder.setPositiveButton("Escanear de nuevo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scanCode();
                        }

                    }).setNegativeButton("Terminado", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                Toast.makeText(this, "No se encuentran resultados", Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                        activos = Activo.fromJson(jsonObject.getJSONArray("activos"));
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
    public void validarActivo(Activo activo){
        String auth = preferences.getString("session", null);
        if( auth != null) {
            String estado = "CORRECTO";
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
            activo.setObservacion(m_Text);
            activo.setEstado(estado);
            RequestBody requestBody = RequestBody.create(enviar.toString(), JSON);
            Request request  = new Request.Builder()
                    .url(url)
                    .header("Authorization", auth)
                    .put(requestBody)
                    .build();
            cliente.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    errorConexion();
                }
            });
        }
    }
}
