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
public class Usuario {

	private String cedula, nombre, apellido;
	private int cantObs, cantActivos; 

	public int cantObs() {
		return cantObs;
	}

	public int cantActivos() {
		return cantActivos;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setCantObs(int cantObs) {
		this.cantObs = cantObs;
	}

	public void setCantActivos(int cantActivos) {
		this.cantActivos = cantActivos;
	}

	public Usuario(String cedula, String nombre, String apellido) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public static Usuario fromJson(JSONObject json) {
		Usuario u = new Usuario();
		try {
			u.cedula = json.getString("cedula_usuario");
			u.nombre = json.getString("nombre_usuario");
			u.apellido = json.getString("apellido_usuario");
			if(json.has("cantidad_observaciones_usuario"))
				u.cantObs = json.getInt("cantidad_observaciones_usuario");
			if(json.has("cantidad_activos_usuario"))
				u.cantActivos = json.getInt("cantidad_activos_usuario");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return u;
	}

	public static ArrayList<Usuario> fromJson(JSONArray jsonArray) {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Usuario usuario = Usuario.fromJson(json);
			if (usuario != null) {
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}	
	public static Object[][] matriz(ArrayList<Usuario> usuarios){ 
		int contador = 0; 
		Object [][] datosActivos = new Object[usuarios.size()][];
		for(Usuario usuario: usuarios){ 
			datosActivos[contador] = new Object[]{usuario.cedula, usuario.nombre, usuario.apellido, usuario.cantActivos};
			contador++;
		}
		return datosActivos;
	}

	public Usuario() {
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
}
