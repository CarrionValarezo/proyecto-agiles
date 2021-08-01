/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Proceso;
import gestor.Gestor;
import interfaces.IntDetalleProceso;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author carri
 */
public class TablaProcesos extends JTable {

	ModeloTabla modelo;
	Gestor gestor;
	String[] titulos;
	String cedula;

	public TablaProcesos() {
		this.modelo = new ModeloTabla();
		this.gestor = Gestor._getGestor();
		this.titulos = new String[]{"OBS", "ID PROCESO", "NOMBRE PROCESO", "FECHA CREACION PROCESO", "ESTADO PROCESO"};
		accionClick();
	}

	public void setUsuario(String cedula) {
		this.cedula = cedula;
	}

	public void cargarTabla() {
		ArrayList<Proceso> procesos = (this.cedula == null) ? gestor.getProcesos() : gestor.getProcesosUsuario(this.cedula);
		this.setDefaultRenderer(Object.class, new Render());
		this.modelo = new ModeloTabla(null, this.titulos);
		if (procesos != null) {
			this.getColumnModel().getColumn(0).setMaxWidth(5);
			for (Proceso proceso : procesos) {
				JButton btn = new JButton();
				if (proceso.estado().equals("CREADO")) {
					btn.setBackground(Color.white);
				} else if (proceso.cantObs() != 0) {
					btn.setBackground(new Color(255, 103, 0));
					btn.setText(String.valueOf(proceso.cantObs()));
					btn.setForeground(Color.white);
				} else {
					btn.setBackground(Color.GREEN);
				}
				Object[] datos = {btn, proceso.id(), proceso.nombre(), proceso.fecha(), proceso.estado()};
				this.modelo.addRow(datos);
			}
			this.setModel(modelo);
		} else {
			this.modelo = new ModeloTabla(null, this.titulos);
			this.setModel(modelo);
		}
	}

	public void accionClick() {
		TablaProcesos tabla = this;
		tabla.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int idProceso = Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 1).toString());
					IntDetalleProceso intDetalleProceso = IntDetalleProceso._getVentana();
					intDetalleProceso.agregarDetalle(idProceso);
				}
			}
		});	
	}

}
