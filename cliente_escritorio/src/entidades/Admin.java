/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.Base64;

/**
 *
 * @author carri
 */
public class Admin {

	private static String cedula, nombre, apellido, password, rol;

	private static String auth;

	private static Admin instancia = null;

	private Admin() {

	}

	public static String getAuth(){
		return "Basic "+Base64.getEncoder().encodeToString((cedula+":"+password).getBytes()); 
	}

	public static Admin _getAdmin() {
		if (instancia == null) {
			instancia = new Admin();
		}
		return instancia;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

}
