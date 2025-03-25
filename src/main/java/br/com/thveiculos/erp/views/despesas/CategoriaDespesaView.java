package br.com.thveiculos.erp.views.despesas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.controllers.SimpleViewController;
import br.com.thveiculos.erp.controllers.despesas.CategoriaDespesaController;
import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;
import br.com.thveiculos.erp.views.SimpleView;

@Component
public class CategoriaDespesaView extends SimpleView {

	public CategoriaDespesaView(CategoriaDespesaController controller) {
		super("Categoria Despesas", "Categoria");
		this.controller = controller;
		controller.setView(this);

	}
}
