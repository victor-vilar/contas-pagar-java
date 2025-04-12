package br.com.thveiculos.erp.views.despesas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.border.EtchedBorder;
//import com.jgoodies.forms.layout.FormLayout;;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
//import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextField;
import org.springframework.stereotype.Component;

@Component
public class MovimentoPagamentoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JScrollPane scrollPane;
	private JTable table;
	private JToolBar toolBar;
	private JButton btnDeletar;
	private JButton btnNovoDesapesa;
	private JButton btnNovoRecorrente;
	private String[] tableHeaders = {"No","Data Vencimento","Nome/Fornecedor","Parcela","Valor a Pagar"};
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel;

	
	private JTable getTable() {
		return this.table;
	}
	
	private JToolBar getTollBar() {
		return this.toolBar;
	}
	
	
	public MovimentoPagamentoView() {	
		this.setUp();
	}

	/**
	 * Create the frame.
	 */
	public void setUp() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		setTitle("Despesas");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblNewLabel = new JLabel("Consulta por Período");
		lblNewLabel.setBounds(12, 45, 134, 17);
		contentPane.add(lblNewLabel);


		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 126, 1174, 637);
		contentPane.add(scrollPane);
		
		configurarTollBar();
		configurarLabels();
		configurarFields();
		configurarBotoes();
		configurarTabela();
		
	}
	
	private void configurarTollBar() {

		// TollBar
		toolBar = new JToolBar();
		toolBar.setEnabled(false);
		toolBar.setBounds(0, 0, 1198, 36);
		toolBar.setFloatable(false);
		contentPane.add(toolBar);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(12, 59, 253, 55);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblIncio = new JLabel("Início");
		lblIncio.setBounds(10, 7, 34, 17);
		panel.add(lblIncio);
		
		JLabel lblFim = new JLabel("Fim");
		lblFim.setBounds(136, 7, 60, 17);
		panel.add(lblFim);
		
		textField = new JTextField();
		textField.setBounds(10, 22, 89, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(135, 22, 89, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);

	}
	private void configurarLabels() {}
	private void configurarFields() {}
	
	private void configurarBotoes() {
		
		// Botao de Adicionar Nova Despesa
		btnNovoDesapesa = new JButton("Nova Despesa");
		btnNovoDesapesa.setIcon(new ImageIcon("/home/victor/Downloads/novo-arquivo(1).png"));
		btnNovoDesapesa.setToolTipText("Nova Despesa");
		toolBar.add(btnNovoDesapesa);

		// Adiciona um evento ao clique do botão de novo
		btnNovoDesapesa.addActionListener((e) -> {
			
		});

		// Botao de Adicionar Despesa Recorrente
		btnNovoRecorrente = new JButton("Nova Despesa Recorrente");
		btnNovoRecorrente.setIcon(new ImageIcon("/home/victor/Downloads/novo-arquivo(1).png"));
		btnNovoRecorrente.setToolTipText("Nova Despesa Recorrente");
		toolBar.add(btnNovoRecorrente);

		// Adiciona um evento ao clique do botão de novo
		btnNovoRecorrente.addActionListener((e) -> {
			
		});

		// Botão de Deletar
		btnDeletar = new JButton("Deletar");
		btnDeletar.setIcon(new ImageIcon("/home/victor/Downloads/delete.png"));
		toolBar.add(btnDeletar);

		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = getTable().getSelectedRow();
				String msg = "Deseja remover a forma \"" + getTable().getValueAt(row, 1) + "\"";
				if (JOptionPane.showConfirmDialog(null, msg, "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
					
				}

			}
		});
	}
	
	private void configurarTabela() {
		table = new JTable();
		table.setBounds(0, 0, 1, 1);
		scrollPane.setViewportView(table);
		
		table.setModel(new DefaultTableModel(new Object[][] {},tableHeaders) {
			Class[] columnTypes = new Class[] { Long.class, LocalDate.class,String.class,String.class,String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		
				
				table.getColumnModel().getColumn(0).setMaxWidth(40);
				table.getColumnModel().getColumn(1).setMaxWidth(80);
				table.getColumnModel().getColumn(3).setMaxWidth(50);
				table.getColumnModel().getColumn(4).setMaxWidth(100);
				
				//table.setAutoCreateRowSorter(true);
				
				//cria um table sorter e já realiza um sorte pela coluna de data
				TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
				table.setRowSorter(sorter);
				List<RowSorter.SortKey> sortKeys = new ArrayList<>();
				 
				int columnIndexToSort = 1;
				sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
				 
				sorter.setSortKeys(sortKeys);
				sorter.sort();
				//------------------
				
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				model.addRow(new Object[] {1L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1990, 10, 23)),"Manutençaõ veiculos KKK 3030","única","R$1200,00"});
				model.addRow(new Object[] {2L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Faxineira","1/3","R$1200,00"});
				model.addRow(new Object[] {3L,DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.of(1989, 1, 10)),"Emprestimo Bancário","7/10","R$100,00"});
				
				
	}
}
