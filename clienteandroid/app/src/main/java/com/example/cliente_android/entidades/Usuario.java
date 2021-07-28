package com.example.cliente_android.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Usuario {
    private String cedula, nombre, apellido;
    int cantObs;

    public static Usuario fromJson(JSONArray jsonArray, String cedula) {
        Usuario u = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject usuarioJson = null;
            try {
                usuarioJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            u = Usuario.fromJson(usuarioJson);
            if (u.cedula.equals(cedula)) {
                return u;
            }
        }
        return null;
    }
    public static Usuario fromJson(JSONObject usuarioJson) {
        Usuario u = new Usuario();
        try {
            u.cedula = usuarioJson.getString("cedula_usuario");
            u.nombre = usuarioJson.getString("nombre_usuario");
            u.apellido = usuarioJson.getString("apellido_usuario");
            u.cantObs = usuarioJson.getInt("cantidad_observaciones_usuario");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static ArrayList<Usuario> fromJson(JSONArray jsonArray) {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject usuarioJson = null;
            try {
                usuarioJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Usuario usuario = Usuario.fromJson(usuarioJson);
            if (usuario != null) {
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getCantObs(){
        return cantObs;
    }
}
