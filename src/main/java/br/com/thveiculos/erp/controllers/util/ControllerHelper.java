package br.com.thveiculos.erp.controllers.util;

import javax.swing.table.DefaultTableModel;

import javax.swing.table.DefaultTableModel;

public abstract class ControllerHelper {

	public static void limparTabela(DefaultTableModel model) {
	    for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
	        model.removeRow(i);
	    }
	}
	
}
