/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Proceso;
import entidades.Usuario;
import gestor.Gestor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author carri
 */
public class TablaUsuariosProceso extends JTable {

	ModeloTabla modelo;
	Gestor gestor;
	String[] titulos;
	JButton btnEliminar;
	Proceso proceso;
	TablaActivosProceso tablaActivos;

	public TablaUsuariosProceso() {
		this.gestor = Gestor._getGestor();
		this.titulos = new String[]{"CEDULA", "NOMBRE", "APELLIDO", ""};
		crearBoton();
		tablaMoverCursor();
		tablaClick();
	}

	

	public void setTablaActivos(TablaActivosProceso tablaActivos){ 
		this.tablaActivos = tablaActivos; 
	}

	public void cargarTabla(String idProceso) {
		this.proceso = this.gestor.getProceso(idProceso);
		this.setDefaultRenderer(Object.class, new Render());
		this.modelo = new ModeloTabla(null, titulos);
		for (Usuario usuario : proceso.getUsuarios()) {
			Object[] datos = {usuario.getCedula(), usuario.getNombre(), usuario.getApellido(), this.btnEliminar};
			this.modelo.addRow(datos);
		}
		this.setModel(this.modelo);
		this.setRowHeight(20);
	}

	private void crearBoton() {
		this.btnEliminar = new JButton("Eliminar");
		btnEliminar.setBackground(Color.red);
		btnEliminar.setForeground(Color.white);
	}

	private void tablaMoverCursor() {
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				cambiarCursor(evt);
			}
		});
	}

	private void tablaClick() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				accionEliminar(evt);
				seleccionarActivos(evt);
			}
		});
	}

	private void seleccionarActivos(java.awt.event.MouseEvent evt){
		int column = this.getColumnModel().getColumnIndexAtX(evt.getX());
		int row = evt.getY() / this.getRowHeight();
		if (row < this.getRowCount() && row >= 0 && column < this.getColumnCount() && column >= 0 && column != 3) {
			this.tablaActivos.model.clearSelection();
			String cedula = (String) this.getValueAt(row, 0);
			for(int i = 0; i < tablaActivos.getRowCount(); i++){
				if(tablaActivos.getValueAt(i,4).toString().equals(cedula)){
					this.tablaActivos.model.addSelectionInterval(i, i);
				}
			}
		}
	}

	private void accionEliminar(java.awt.event.MouseEvent evt) {
		int column = this.getColumnModel().getColumnIndexAtX(evt.getX());
		int row = evt.getY() / this.getRowHeight();
		if (row < this.getRowCount() && row >= 0 && column < this.getColumnCount() && column >= 0) {
			Object value = this.getValueAt(row, column);
			String cedula = (String) this.getValueAt(row, 0);
			String nombre = (String) this.getValueAt(row, 1);
			if (value instanceof JButton) {
				((JButton) value).doClick();
				JButton btn = (JButton) value;
				int opcion = JOptionPane.showConfirmDialog(null,
						"Esta seguro de eliminar al usuario: " + cedula + " " + nombre + "  del proceso: " + this.proceso.getNombre());
				if (opcion == 0) {
					this.gestor.eliminarUsuario(this.proceso.getIdProceso(), cedula);
					cargarTabla(this.proceso.getIdProceso());
					if (this.tablaActivos != null){
						this.tablaActivos.cargarTabla(this.proceso.getIdProceso());
					}
				}
			}
		}
	}

	private void cambiarCursor(MouseEvent evt) {
		if (this.columnAtPoint(evt.getPoint()) == 3) {
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
