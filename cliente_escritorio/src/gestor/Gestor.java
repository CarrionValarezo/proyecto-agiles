/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import entidades.Activo;
import entidades.Proceso;
import entidades.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
public class Gestor {

	private static Gestor instancia = null;

	

	public static Gestor _getGestor() {
		if (instancia == null) {
			return new Gestor();
		}
		return instancia;
	}

	Conexion conexion = new Conexion();

	private void errorConexion(){
		JOptionPane.showMessageDialog(null, "¡No se ha podido conectar con el servicio, intentelo mas tarde!");
	}

	private JSONArray cedulasToJson(String [] cedulas){ 
		JSONArray jsonCedulas = new JSONArray();

		for (String cedula : cedulas) {
			JSONObject jCedula = new JSONObject();
			jCedula.put("cedula_usuario", cedula);
			jsonCedulas.put(jCedula);
		}
		return jsonCedulas;
	}

	public int crearProceso(String nombre, String[] cedulas) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime ahora = LocalDateTime.now();
		String fecha = dtf.format(ahora);

		JSONObject json = new JSONObject();
		JSONObject jsonProceso = new JSONObject()
				.put("nombre_proceso", nombre)
				.put("fecha_proceso", fecha);
		
		json.put("proceso", jsonProceso);
		json.put("usuarios_proceso", cedulasToJson(cedulas));

		try {
			return this.conexion.crearProceso(json);
		} catch (Exception ex) {
			errorConexion();
			return -1;
		}
	}

	public ArrayList<Usuario> getUsuarios() {
		try {
			return this.conexion.getUsuarios();
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public ArrayList<Proceso> getProcesos() {
		try {
			return this.conexion.getProcesos();
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public ArrayList<Activo> getActivos(String cedula) {
		try {
			return this.conexion.getActivosUsuarios(cedula);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public Proceso getProceso(int idProceso) {
		try {
			return conexion.getProceso(idProceso);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}

	}

	public void eliminarUsuario(int idProceso, String cedula) {
		try{
			this.conexion.eliminarUsuarioDeProceso(idProceso, cedula);
		} catch (Exception ex) {
			errorConexion();
		}
	}

	public ArrayList<Proceso> getProcesosUsuario(String cedula) {
		try {
			return conexion.getProcesosUsuarios(cedula);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

}
