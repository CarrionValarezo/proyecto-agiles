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

	public String crearProceso(String nombre, String[] cedulas) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime ahora = LocalDateTime.now();
		String fecha = dtf.format(ahora);
		try {
			return this.conexion.crearProceso(nombre, fecha, cedulas);
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	public String[][] getUsuarios() {
		try {
			return this.conexion.getUsuarios();
		} catch (Exception ex) {
			return null;
		}
	}

	public String[][] getProcesos() {
		try {
			return this.conexion.getProcesos();
		} catch (Exception ex) {
			return null;
		}
	}

	public String[][] getActivos(String cedula) {
		try {
			return this.conexion.getActivosUsuarios(cedula);
		} catch (Exception ex) {
			return null;
		}
	}

	public Proceso getProceso(String idProceso) {
		try {
			return conexion.getProceso(idProceso);
		} catch (Exception ex) {
			return null;
		}

	}

	public void eliminarUsuario(String idProceso, String cedula) {
		try{
			this.conexion.eliminarUsuarioDeProceso(idProceso, cedula);
		} catch (Exception ex) {
			System.out.println("Ocurrio un ERROR");	
		}
	}

	public String[][] getProcesosUsuario(String cedula) {
		try {
			return conexion.getProcesosUsuarios(cedula);
		} catch (Exception ex) {
			return null;
		}
	}

}
