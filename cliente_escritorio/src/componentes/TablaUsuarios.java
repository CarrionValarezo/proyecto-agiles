/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Usuario;
import gestor.Conexion;
import gestor.Gestor;
import interfaces.IntDetalleUsuario;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carri
 */
public class TablaUsuarios extends JTable {

	ModeloTabla modelo;
	Gestor gestor;
	String[] titulos;

	public TablaUsuarios() {
		this.gestor = Gestor._getGestor();
		this.titulos = new String[]{"CEDULA", "NOMBRE", "APELLIDO", "CANTIDAD DE ACTIVOS"};
	}

	public void cargarTablaSinDatos() {
		this.modelo = new ModeloTabla(null, this.titulos);
		this.setModel(this.modelo);
	}

	public void cargarTabla() {
		ArrayList<Usuario> usuarios = this.gestor.getUsuarios();
		if (usuarios != null) {
			this.modelo = new ModeloTabla(Usuario.matriz(usuarios), titulos);
			this.setModel(this.modelo);
		}else{ 
			cargarTablaSinDatos();
		}
	}

	public void cargarTablaFaltantes(int idProceso) {
		ArrayList<Usuario> usuarios = this.gestor.getUsuariosFaltantes(idProceso);
		if (usuarios != null) {
			this.modelo = new ModeloTabla(Usuario.matriz(usuarios), titulos);
			this.setModel(this.modelo);
		}else{ 
			cargarTablaSinDatos();
		}
	}

	public void accionDobleClick() {
		TablaUsuarios tabla = this;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					String cedula = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
					String nombre = tabla.getValueAt(tabla.getSelectedRow(), 1).toString();
					String apellido = tabla.getValueAt(tabla.getSelectedRow(), 2).toString();
					Usuario usuarioSeleccionado = new Usuario(cedula, nombre, apellido);
					IntDetalleUsuario intActivosUsuario = new IntDetalleUsuario(usuarioSeleccionado);
					intActivosUsuario.setVisible(true);
				}
			}
		});
	}

	public void dobleClickAgregarUsuario(int idProceso, PnlDetalleProceso panel){ 
		TablaUsuarios tabla = this;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					String cedula = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
					if(gestor.agregarUsuario(idProceso, cedula)){ 
						((DefaultTableModel) tabla.getModel()).removeRow(tabla.getSelectedRow());
						panel.actualizarTablas(idProceso);
					} 
				}
			}
		});
	}

	public void cambioTablas(JTable derecha, JTextField cantidadUsuarios, JTextField cantidadActivos) {
		TablaUsuarios izquierda = this;
		izquierda.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 1 && table.getSelectedRow() != -1) {
					cambiarUsuario(izquierda, derecha);
					cantidadUsuarios.setText(String.valueOf(derecha.getRowCount()));
					cantidadActivos.setText(String.valueOf(getSumaActivos(derecha)));
				}
			}
		});
		derecha.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 1 && table.getSelectedRow() != -1) {
					cambiarUsuario(derecha, izquierda);
					cantidadUsuarios.setText(String.valueOf(derecha.getRowCount()));
					cantidadActivos.setText(String.valueOf(getSumaActivos(derecha)));
				}
			}
		});
	}

	private int getSumaActivos(JTable tabla) {
		int suma = 0;
		for (int i = 0; i < tabla.getRowCount(); i++) {
			suma += Integer.parseInt((String) tabla.getValueAt(i, 3));
		}
		return suma;
	}

	private void cambiarUsuario(JTable origen, JTable destino) {
		String cedula = origen.getValueAt(origen.getSelectedRow(), 0).toString();
		String nombre = origen.getValueAt(origen.getSelectedRow(), 1).toString();
		String apellido = origen.getValueAt(origen.getSelectedRow(), 2).toString();
		String cantidad_activos = origen.getValueAt(origen.getSelectedRow(), 3).toString();
		String[] usuarioTabla = {cedula, nombre, apellido, cantidad_activos};
		((DefaultTableModel) origen.getModel()).removeRow(origen.getSelectedRow());
		((DefaultTableModel) destino.getModel()).addRow(usuarioTabla);
	}

}
