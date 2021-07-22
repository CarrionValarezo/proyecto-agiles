/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author carri
 */
public class Proceso {
	String idProceso, nombre, fecha, estado; 	
	Usuario [] usuarios; 
	Activo [] activos; 

	public Proceso(String idProceso, String nombre, String fecha, String estado, Usuario[] usuarios, Activo[] activos) {
		this.idProceso = idProceso;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado; 
		this.usuarios = usuarios;
		this.activos = activos;
	}

	public String getEstado() {
		return estado;
	}


	public Proceso() {
	}


	public String getNombre() {
		return nombre;
	}

	public String getFecha() {
		return fecha;
	}

	public String getIdProceso() {
		return idProceso;
	}

	public Usuario[] getUsuarios() {
		return usuarios;
	}

	public Activo[] getActivos() {
		return activos;
	}
}
