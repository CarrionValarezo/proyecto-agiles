package com.example.cliente_android.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Proceso {
    private int idProces, cantObservaciones, cantUsuarios, cantActivos;
    private String nombre, fecha, estado;

    public Proceso(int idProceso, String nombre, String fecha, String estado, int cantObs) {
        this.idProces = idProceso;
        this.nombre = nombre;
        this.fecha = fecha;
        this.estado = estado;
        this.cantObservaciones = cantObs;
    }

    public Proceso() {

    }

    public static Proceso fromJson(JSONObject procesoJson) {
        Proceso p = new Proceso();
        try {
            p.idProces = procesoJson.getInt("id_proceso");
            p.nombre = procesoJson.getString("nombre_proceso");
            p.fecha = procesoJson.getString("fecha_creacion_proceso");
            p.estado = procesoJson.getString("estado_proceso");
            p.cantObservaciones = procesoJson.getInt("cantidad_observaciones");
            if(procesoJson.has("cantidad_usuarios_proceso") &&
                    procesoJson.has("cantidad_activos_proceso")) {
                p.cantUsuarios = procesoJson.getInt("cantidad_usuarios_proceso");
                p.cantActivos = procesoJson.getInt("cantidad_activos_proceso");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static ArrayList<Proceso> fromJson(JSONArray jsonArray) {
        ArrayList<Proceso> procesos = new ArrayList<Proceso>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject procesoJson = null;
            try {
                procesoJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Proceso proceso = Proceso.fromJson(procesoJson);
            if (proceso != null) {
                procesos.add(proceso);
            }
        }
        return procesos;
    }

    public int getIdProces() {
        return idProces;
    }

    public String getEstado() {
        return estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantObservaciones() {
        return cantObservaciones;
    }
    public int getCantUsuarios() {
        return cantUsuarios;
    }

    public int getCantActivos() {
        return cantActivos;
    }

}
