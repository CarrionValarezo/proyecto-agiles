/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import entidades.Activo;
import entidades.ActivoProcesado;
import entidades.Proceso;
import gestor.Gestor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

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
	PnlDetalleProceso panel;

	public TablaActivosProceso() {
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.model = this.getSelectionModel();
		this.titulos = new String[]{"ID ACTIVO", "NOMBRE", "DESCRIPCION", "CEDULA USUARIO",
			 "REVISADO", "ESTADO REVISION", "OBSERVACION REVISION", "REVISOR", "NOMBRE REVISOR", "APELLIDO REVISOR"};
		this.gestor = Gestor._getGestor();
		crearBoton();
		tablaClick();
		tablaMoverCursor();

	}

	public void imprimir() {
		System.out.println(SwingUtilities.getRootPane(this));
	}

	public Proceso getProceso() {
		return this.proceso;
	}

	private void crearBoton() {
		this.btnValidar = new JButton("Validar");
		btnValidar.setBackground(Color.green);
		btnValidar.setForeground(Color.white);
	}

	public void cargarTabla(int idProceso) {
		this.proceso = gestor.getProceso(idProceso);

		this.setDefaultRenderer(Object.class, new Render());
		this.modelo = new ModeloTabla(null, titulos);

		if (proceso != null) {
			for (Activo activo : this.proceso.getActivos()) {
				String revision = "";
				if (((ActivoProcesado) activo).estaRevisado() == 1) {
					revision = "REVISADO";
				}
				Object[] datos = {activo.id(),
					activo.nombreItem(),
					activo.descripcionItem(),
					activo.getUsuario().getCedula(),
					revision,
					((ActivoProcesado) activo).estadoRevision(),
					((ActivoProcesado) activo).obsRevision(),
					((ActivoProcesado) activo).getAdminRevisor(),
					((ActivoProcesado) activo).getNombreRevisor(), 
					((ActivoProcesado) activo).getApellidoRevisor()
				};
				this.modelo.addRow(datos);
			}
			this.setModel(this.modelo);
			this.setRowHeight(20);

		}else{
			this.modelo = new ModeloTabla(null, this.titulos);
			this.setModel(modelo);
		}
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
				int id = proceso.id();
				//IntValidarActivo intValidacion = new IntValidarActivo(this.panel, this, id, row);
				//intValidacion.setVisible(true);
			}
		}
	}

	public void setPanel(PnlDetalleProceso panel) {
		this.panel = panel;
	}	
}
