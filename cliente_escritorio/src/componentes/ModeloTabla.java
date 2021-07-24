/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carri
 */
public class ModeloTabla extends DefaultTableModel {


	public ModeloTabla() {
	}

	public ModeloTabla( Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
