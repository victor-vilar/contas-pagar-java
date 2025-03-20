package br.com.thveiculos.erp.views.despesas;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import br.com.thveiculos.erp.configuration.ApplicationConfiguration;
import br.com.thveiculos.erp.controllers.despesas.FormaPagamentoController;


public class FormaPagamentoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField fieldFormaPagamento;
	private JButton btnSalvar;
	private FormaPagamentoController controller;
	
	public JTable getTable() {
		return this.table;
	}
	
	public JTextField getFieldFormaPagamento() {
		return this.fieldFormaPagamento;
	}
	
	public JButton getBtnSalvar() {
		return this.btnSalvar;
	}
	

	/**
	 * Create the frame.
	 */

	public FormaPagamentoView() {
		
		setTitle("Formas de Pagamento");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Criando o ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 60, 414, 150);
		contentPane.add(scrollPane);
		
		//Criando txt_formaPagamento
		fieldFormaPagamento = new JTextField();
		fieldFormaPagamento.setBounds(10, 24, 414, 25);
		contentPane.add(fieldFormaPagamento);
		fieldFormaPagamento.setColumns(10);
		
		//Criando Tabela
		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
	    
		//Adicionando método de duplo clique na tabela
		table.addMouseListener(new MouseAdapter() {
	          public void mouseClicked(MouseEvent me) {
	             //Se teve 2 clicks no mouse
	        	  if (me.getClickCount() == 2) {     // to detect doble click events
	                //Pega a tabela
	        		 JTable target = (JTable)me.getSource();
	                //pega a linha selecionada
	        		 int row = target.getSelectedRow(); // select a row
	                //Pega a coluna selecionada
	        		 int column = target.getSelectedColumn(); // select a column
	                //Adiciona o valor das coordenadas passadas para o txt_forma_pagamento
	        		 fieldFormaPagamento.setText((String)table.getValueAt(row,1));
	               //JOptionPane.showMessageDialog(null, table.getValueAt(row,column)); // get the value of a row and column.
	             }
	          }
	       });
		
	    table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Id", "Forma de Pagamento"
			}
		) {
			Class[] columnTypes = new Class[] {
				Long.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
	    //Impede que os dados na tabela sejam editaveis
	    table.setDefaultEditor(Object.class, null);
	    
	    //seta a primeira coluna com o tamanho 27
	    table.getColumnModel().getColumn(0).setPreferredWidth(27);
	    
	    //seta a ultima coluna para pegar toda a tabela
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
	    
	    //Criando o Label
		JLabel lbl_nome = new JLabel("Nome");
		lbl_nome.setBounds(10, 11, 125, 14);
		contentPane.add(lbl_nome);
		
		
		//Criando botao
		btnSalvar = new JButton("Salvar");
		
		//Adiciona um evento ao clique do botão
		btnSalvar.addActionListener((e) ->{
			this.salvar();
		});
		
		btnSalvar.setBounds(10, 214, 89, 36);
		contentPane.add(btnSalvar);
		
		
	}

	
	private void salvar() {
		if(!fieldFormaPagamento.getText().equals("")) {
			//this.controller.salvar();
		}
		
		fieldFormaPagamento.setText(null);		
	}
	
	private void exibirDialog(String msg) {
		JOptionPane.showInternalMessageDialog(null, msg);
	}
		
	
}
