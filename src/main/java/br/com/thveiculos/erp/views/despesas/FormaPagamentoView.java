package br.com.thveiculos.erp.views.despesas;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import java.awt.Component;

public class FormaPagamentoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField txt_forma_pagamento;
	private JButton btn_salvar;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormaPagamentoView frame = new FormaPagamentoView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		txt_forma_pagamento = new JTextField();
		txt_forma_pagamento.setBounds(10, 24, 414, 25);
		contentPane.add(txt_forma_pagamento);
		txt_forma_pagamento.setColumns(10);
		
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
	        		 txt_forma_pagamento.setText((String)table.getValueAt(row,1));
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
		btn_salvar = new JButton("Salvar");
		
		//Adiciona um evento ao clique do botão
		btn_salvar.addActionListener((e) ->{
			if(!txt_forma_pagamento.getText().equals("")) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.addRow(new Object[] {"1",txt_forma_pagamento.getText()});
			JOptionPane.showInternalMessageDialog(null, "Forma de Pagamento Cadastrada com Sucesso");
			txt_forma_pagamento.setText(null);
			}
		});
		
		btn_salvar.setBounds(10, 214, 89, 36);
		contentPane.add(btn_salvar);
		
	}
}
