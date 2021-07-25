/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Activo;
import entidades.Proceso;
import gestor.Gestor;
import interfaces.IntDetalleProceso;
import interfaces.IntValidarActivo;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carri
 */
public class TablaActivosProceso extends JTable {

	ModeloTabla modelo;
	Gestor gestor;
	String[] titulos;
	JButton btnValidar;
	Proceso proceso;
	ListSelectionModel model;

	public TablaActivosProceso() {
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.model = this.getSelectionModel();
		this.titulos = new String[]{"ID ACTIVO", "ID OBJETO", "NOMBRE", "DESCRIPCION", "CEDULA USUARIO",
			"NOMBRE USUARIO", "APELLIDO USUARIO", "REVISADO", "ESTADO REVISION", "OBSERVACION REVISION", ""};
		this.gestor = Gestor._getGestor();
		crearBoton();
		tablaClick();
		tablaMoverCursor();
	}

	public Proceso getProceso() {
		return this.proceso;
	}

	private void crearBoton() {
		this.btnValidar = new JButton("Validar");
		btnValidar.setBackground(Color.green);
		btnValidar.setForeground(Color.white);
	}

	public void cargarTabla(String idProceso) {
		this.proceso = gestor.getProceso(idProceso);

		this.setDefaultRenderer(Object.class, new Render());

		this.modelo = new ModeloTabla(null, titulos);

		for (Activo activo : this.proceso.getActivos()) {
			String revision = "";
			if (activo.getRevisionActivo() == 1) {
				revision = "REVISADO";
			}
			Object[] datos = {activo.getIdPertinencia(),
				activo.getIdActivo(), activo.getNombreActivo(),
				activo.getDescripcionActivo(),
				activo.getUsuario().getCedula(),
				activo.getUsuario().getNombre(),
				activo.getUsuario().getApellido(),
				revision,
				activo.getEstadoRevisionActivo(),
				activo.getObservacionRevision(),
				this.btnValidar
			};
			this.modelo.addRow(datos);
		}
		this.setModel(this.modelo);
		this.setRowHeight(20);
	}

	private void tablaMoverCursor() {
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				cambiarCursor(evt);
			}
		});
	}

	private void cambiarCursor(java.awt.event.MouseEvent evt) {
		if (this.columnAtPoint(evt.getPoint()) == 10) {
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void tablaClick() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				accionValidar(evt);
			}
		});
	}

	private void accionValidar(MouseEvent evt) {
		int column = this.getColumnModel().getColumnIndexAtX(evt.getX());
		int row = evt.getY() / this.getRowHeight();
		if (row < this.getRowCount() && row >= 0 && column < this.getColumnCount() && column >= 0) {
			Object value = this.getValueAt(row, column);
			if (value instanceof JButton) {
				((JButton) value).doClick();
				JButton btn = (JButton) value;
				int id = Integer.parseInt(this.proceso.getIdProceso());

				IntDetalleProceso detalle = (IntDetalleProceso) SwingUtilities.getWindowAncestor(this);
				IntValidarActivo intValidacion = new IntValidarActivo(detalle, this, id, row);
				intValidacion.setVisible(true);
			}
		}
	}
}
