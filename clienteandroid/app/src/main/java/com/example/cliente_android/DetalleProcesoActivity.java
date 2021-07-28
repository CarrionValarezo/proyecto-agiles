package com.example.cliente_android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class DetalleProcesoActivity extends Activity {
    TextView tvNombre, tvFecha, tvId, tvEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceso_detalle);
        int idProceso = getIntent().getIntExtra("EXTRA_ID_PROCESO", 0);
    }
}
