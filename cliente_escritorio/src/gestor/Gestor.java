/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import entidades.Proceso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

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
		JOptionPane.showMessageDialog(null, "¡No se ha podido conectar con el servido, intentelo mas tarde!");
	}

	public String crearProceso(String nombre, String[] cedulas) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime ahora = LocalDateTime.now();
		String fecha = dtf.format(ahora);
		try {
			return this.conexion.crearProceso(nombre, fecha, cedulas);
		} catch (Exception ex) {
			errorConexion();
			return ex.toString();
		}
	}

	public String[][] getUsuarios() {
		try {
			return this.conexion.getUsuarios();
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public String[][] getProcesos() {
		try {
			return this.conexion.getProcesos();
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public String[][] getActivos(String cedula) {
		try {
			return this.conexion.getActivosUsuarios(cedula);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

	public Proceso getProceso(String idProceso) {
		try {
			return conexion.getProceso(idProceso);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}

	}

	public void eliminarUsuario(String idProceso, String cedula) {
		try{
			this.conexion.eliminarUsuarioDeProceso(idProceso, cedula);
		} catch (Exception ex) {
			errorConexion();
		}
	}

	public String[][] getProcesosUsuario(String cedula) {
		try {
			return conexion.getProcesosUsuarios(cedula);
		} catch (Exception ex) {
			errorConexion();
			return null;
		}
	}

}
