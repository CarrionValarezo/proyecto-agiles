package com.example.cliente_android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Proceso {
    private int idProces;
    private String nombre, fecha, estado;

    public Proceso(int idProceso, String nombre, String fecha, String estado){
        this.idProces = idProceso;
        this.nombre = nombre;
        this.fecha = fecha;
        this.estado = estado;
    }
    public Proceso(){

    }

    private static Proceso fromJson(JSONObject procesoJson) {
        Proceso p = new Proceso();
        try {
            p.idProces = procesoJson.getInt("id_proceso");
            p.nombre = procesoJson.getString("nombre_proceso");
            p.fecha = procesoJson.getString("fecha_creacion_proceso");
            p.estado = procesoJson.getString("estado_proceso");
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
}
