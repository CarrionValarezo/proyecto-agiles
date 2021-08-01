/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
public class Proceso {
	String nombre, fecha, estado, cedulaCreador; 	
	ArrayList<Usuario>  usuarios; 
	ArrayList<Activo> activos; 
	int cantObs, cantUsuarios, cantActivos, id;

	public Proceso(int id, String nombre, String fecha, String estado, ArrayList<Usuario> usuarios, ArrayList<Activo> activos, int cantObs) {
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado; 
		this.usuarios = usuarios;
		this.activos = activos;
		this.cantObs = cantObs; 
	}

    public static Proceso fromJson(JSONObject json) {
        Proceso p = new Proceso();
        try {
            p.id = json.getInt("id_proceso");
            p.nombre = json.getString("nombre_proceso");
            p.fecha = json.getString("fecha_creacion_proceso");
            p.estado = json.getString("estado_proceso");
            p.cantObs = json.getInt("cantidad_observaciones");
			p.cedulaCreador = json.getString("cedula_admin_creador");
            if(json.has("cantidad_usuarios_proceso") &&
                    json.has("cantidad_activos_proceso")) {
                p.cantUsuarios = json.getInt("cantidad_usuarios_proceso");
                p.cantActivos = json.getInt("cantidad_activos_proceso");
            }
        } catch (JSONException e) {
        }
        return p;
    }

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void setActivos(ArrayList<Activo> activos) {
		this.activos = activos;
	}

    public static ArrayList<Proceso> fromJson(JSONArray jsonArray) {
        ArrayList<Proceso> procesos = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Proceso proceso = Proceso.fromJson(json);
            if (proceso != null) {
                procesos.add(proceso);
            }
        }
        return procesos;
    }

	public int cantObs() {
		return cantObs;
	}

	public void setCantObs(int cantObs) {
		this.cantObs = cantObs;
	}

	public String estado() {
		return estado;
	}

	public Proceso() {
	}

	public String nombre() {
		return nombre;
	}

	public String fecha() {
		return fecha;
	}

	public int id() {
		return id;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public ArrayList<Activo> getActivos() {
		return activos;
	}

}