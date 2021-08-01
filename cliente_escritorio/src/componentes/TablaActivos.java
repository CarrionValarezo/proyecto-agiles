/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Activo;
import gestor.Gestor;
import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author carri
 */
public class TablaActivos extends JTable{
	
	ModeloTabla modelo;
	Gestor gestor;
	String[] titulos;

	public TablaActivos() {
		this.modelo = new ModeloTabla();
		this.gestor = Gestor._getGestor();
		this.titulos = new String[]{"ID", "ID ITEM", "NOMBRE", "DESCRIPCION"};
	}

	public void cargarTabla(String cedula) {
		ArrayList<Activo> activos = gestor.getActivos(cedula);
		this.modelo = new ModeloTabla(Activo.matriz(activos), this.titulos);
		this.setModel(modelo);
	}
}
