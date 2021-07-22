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
public class Activo {
	String idPertenencia, idActivo, nombreActivo, descripcionActivo,estadoRevisionActivo,observacionRevision; 
	int revisionActivo; 
	Usuario usuario; 

	public Activo(String idPertinencia, String idActivo, String nombreActivo, String descripcionActivo, 
					int revisionActivo, String estadoRevisionActivo, String observacionRevision) {
		this.idPertenencia = idPertinencia; 
		this.idActivo = idActivo;
		this.nombreActivo = nombreActivo;
		this.descripcionActivo = descripcionActivo;
		this.revisionActivo = revisionActivo; 
		this.estadoRevisionActivo = estadoRevisionActivo; 
		this.observacionRevision = observacionRevision; 
	}

	public String getIdPertenencia() {
		return idPertenencia;
	}

	public String getEstadoRevisionActivo() {
		return estadoRevisionActivo;
	}

	public String getObservacionRevision() {
		return observacionRevision;
	}

	public int getRevisionActivo() {
		return revisionActivo;
	}

	public String getIdActivo() {
		return idActivo;
	}

	public String getNombreActivo() {
		return nombreActivo;
	}

	public String getDescripcionActivo() {
		return descripcionActivo;
	}

	public String getIdPertinencia(){
		return idPertenencia; 
	}

	public void setUsuario(Usuario usuario){
		this.usuario = usuario;
	}

	public Usuario getUsuario(){
		return this.usuario; 
	}
	@Override
	public String toString(){
		return this.idPertenencia +" "+this.idActivo+" "+this.nombreActivo + " "+this.revisionActivo+ " "+this.estadoRevisionActivo+" "+this.observacionRevision;  
	}	
}
