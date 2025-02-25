package com.example.cliente_android.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activo {
    private String id, idItem, nombreItem, desItem,  estado, observacion;

    private Usuario usuario;

    public void setId(String id) {
        this.id = id;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

    public void setDesItem(String desItem) {
        this.desItem = desItem;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    private int revision;

    public Usuario getUsuario() {
        return usuario;
    }

    public Activo() {

    }

    public static Activo fromJson(JSONObject activoJson) {
        Activo a = new Activo();
        a.usuario = new Usuario();
        try {
            JSONObject jsonUsuario = activoJson.getJSONObject("usuario");
            a.id = activoJson.getString("id_activo");
            a.idItem = "0";
            a.nombreItem = activoJson.getString("nombre_activo");
            a.desItem = activoJson.getString("descripcion_activo");
            a.revision = activoJson.getInt("revision_activo");
            a.estado = activoJson.getString("estado_revision_activo");
            a.observacion = activoJson.getString("observacion_revision");
            a.usuario.setCedula(jsonUsuario.getString("cedula_usuario"));
            a.usuario.setNombre(jsonUsuario.getString("nombre_usuario"));
            a.usuario.setApellido(jsonUsuario.getString("apellido_usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static ArrayList<Activo> fromJson(JSONArray jsonArray) {
        ArrayList<Activo> activos = new ArrayList<Activo>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject activoJson = null;
            try {
                activoJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Activo activo = Activo.fromJson(activoJson);
            if (activo != null) {
                activos.add(activo);
            }
        }
        return activos;
    }
    public static ArrayList<Activo> fromJson(JSONArray jsonArray, String cedula) {
        ArrayList<Activo> activos = new ArrayList<Activo>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject activoJson = null;
            try {
                activoJson = jsonArray.getJSONObject(i);
                JSONObject jsonUsuario = activoJson.getJSONObject("usuario");
                Activo activo = Activo.fromJson(activoJson);
                if (activo != null && jsonUsuario.getString("cedula_usuario").equals(cedula)) {
                    activos.add(activo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return activos;
    }

    public String getId() {
        return id;
    }

    public String getIdItem() {
        return idItem;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public String getDesItem() {
        return desItem;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public int getRevision() {
        return revision;
    }

}
