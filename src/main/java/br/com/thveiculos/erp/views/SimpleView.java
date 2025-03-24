package br.com.thveiculos.erp.views;

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

import br.com.thveiculos.erp.controllers.SimpleViewController;
import br.com.thveiculos.erp.entities.interfaces.SimpleEntity;

/**
 * SimpleView são formularios que manipulam entidades do tipo
 * {@link SimpleEntity}. Essas entidades possuem poucas propriedades, fazendo
 * com que os formulários possuam poucos campos e de maneira geral sigam a mesma
 * estrutura. Por exemplo a classe
 * 
 * @{link FormaPagamento} é uma SimpleEntity possui somente o Id e a forma( que
 *        é o seu nome).
 * 
 */
public abstract class SimpleView extends JFrame {

	private static final long serialVersionUID = 1L;
	protected JPanel contentPane;
	protected JScrollPane scrollPane;
	protected JTable table;
	protected JTextField fieldNome;
	protected JTextField fieldId;
	protected JToolBar toolBar;
	protected JButton btnSalvar;
	protected JButton btnNovo;
	protected JButton btnDeletar;
	protected SimpleViewController controller;
	
	private static final String ERROR_HEADER = "Erro";
	private static final String ERROR_MESSAGE = "Não é possivel cadastrar outra forma de pagamento com o mesmo nome";

	public JTextField getFieldId() {
		return this.fieldId;
	}

	public JTextField getFieldNome() {
		return this.fieldNome;
	}

	public JTable getTable() {
		return this.table;
	}

	public SimpleView() {
		this.setUp();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				controller.atualizarTabela();
			}
		});
	}

	public void setUp() {

		setTitle("Formas de Pagamento");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 443, 329);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		configurarTollBar();
		configurarScrollPane();
		configurarTable();
		configurarLabels();
		configurarFields();
		configurarButtons();
	}

	private void configurarTollBar() {

		// TollBar
		toolBar = new JToolBar();
		toolBar.setEnabled(false);
		toolBar.setBounds(0, 0, 448, 36);
		toolBar.setFloatable(false);
		contentPane.add(toolBar);

	}

	private void configurarScrollPane() {

		// Criando o ScrollPane
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 143, 426, 150);
		contentPane.add(scrollPane);
	}

	private void configurarTable() {

		// Criando Tabela
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "No", "Forma de Pagamento" }) {
			Class[] columnTypes = new Class[] { Long.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		// seta a primeira coluna com o tamanho 27
		table.getColumnModel().getColumn(0).setMaxWidth(30);

		// Adicionando método de duplo clique na tabela
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				// to detect doble click events
				if (me.getClickCount() == 2) {
					// Pega a tabela
					JTable target = (JTable) me.getSource();

					// pega a linha selecionada
					int row = target.getSelectedRow(); // select a row

					// Pega a coluna selecionada
					int column = target.getSelectedColumn(); // select a column

					// Adicionando valor do formulário para os campos para edição
					fieldNome.setText((String) table.getValueAt(row, 1));
					fieldId.setText(String.valueOf(table.getValueAt(row, 0)));
					fieldNome.setEditable(true);
					fieldNome.requestFocus();
					// JOptionPane.showMessageDialog(null, table.getValueAt(row,column)); // get the
					// value of a row and column.
					target.clearSelection();
				}

			}
		});

	}

	private void configurarLabels() {

		// Id
		JLabel lblNewLabel = new JLabel("No");
		lblNewLabel.setBounds(360, 60, 60, 17);
		contentPane.add(lblNewLabel);

		// Nome
		JLabel lbl_nome = new JLabel("Nome");
		lbl_nome.setBounds(10, 92, 125, 14);
		contentPane.add(lbl_nome);
	}

	private void configurarFields() {

		// Id
		fieldId = new JTextField();
		fieldId.setEditable(false);
		fieldId.setBounds(360, 77, 76, 25);
		contentPane.add(fieldId);
		fieldId.setColumns(10);

		// Criando fieldNome
		fieldNome = new JTextField();
		fieldNome.setBounds(10, 106, 426, 25);
		fieldNome.setEditable(false);
		contentPane.add(fieldNome);
		fieldNome.setColumns(10);

	}

	private void configurarButtons() {

		// Botao de Adicionar Novo
		btnNovo = new JButton("Novo");
		btnNovo.setIcon(new ImageIcon("/home/victor/Downloads/novo-arquivo(1).png"));
		btnNovo.setToolTipText("Adicionar novo");
		toolBar.add(btnNovo);

		// Adiciona um evento ao clique do botão de novo
		btnNovo.addActionListener((e) -> {
			this.controller.novo();
		});

		// Botao de Salvar
		btnSalvar = new JButton("Salvar");
		btnSalvar.setIcon(new ImageIcon("/home/victor/Downloads/salve-.png"));
		toolBar.add(btnSalvar);
		btnSalvar.setToolTipText("Salvar");

		// Adiciona um evento ao clique do botão de salvar
		btnSalvar.addActionListener((e) -> {
			try {
				controller.salvar();
			} catch (ConstraintViolationException | DataIntegrityViolationException ex) {
				JOptionPane.showMessageDialog(null,
						"Não é possivel cadastrar outra forma de pagamento com o mesmo nome", "Erro",
						JOptionPane.ERROR_MESSAGE);

			}

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
					controller.deletar();
				}

			}
		});
	}

}
