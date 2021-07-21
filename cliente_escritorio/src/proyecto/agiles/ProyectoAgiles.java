/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.agiles;

import gestor.Conexion;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carri
 */
public class ProyectoAgiles {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			// TODO code application logic here
			String[][] usuarios = new Conexion().getUsuarios();
		} catch (Exception ex) {
			Logger.getLogger(ProyectoAgiles.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
