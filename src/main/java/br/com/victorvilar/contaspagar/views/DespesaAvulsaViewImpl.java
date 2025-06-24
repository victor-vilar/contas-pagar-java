/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.victorvilar.contaspagar.views;

import br.com.victorvilar.contaspagar.controllers.DespesaAvulsaController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.exceptions.FieldsEmBrancoException;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.util.AppMensagens;
import br.com.victorvilar.contaspagar.views.interfaces.Subscriber;
import jakarta.annotation.PostConstruct;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import br.com.victorvilar.contaspagar.views.interfaces.DespesaAvulsaView;
import javax.swing.JButton;

@Component
@Lazy
public class DespesaAvulsaViewImpl extends javax.swing.JFrame implements Subscriber, DespesaAvulsaView {

    private javax.swing.JComboBox<String> comboFormaPagamentoTabela;
    private final DespesaAvulsaController controller;
    private static final String TIPO_DESPESA = "AVULSA";
    private final ApplicationContext context;

    @Autowired
    public DespesaAvulsaViewImpl(DespesaAvulsaController controller, ApplicationContext context) {
        this.controller = controller;
        this.controller.setView(this);
        this.context = context;

    }

    @PostConstruct
    public void configurarComponent() {
        initComponents();
        configureComponentes();
        setLocationRelativeTo(null);
        controller.inicializarComboBox();

    }

    @Override
    public void subscribe(String valor, String tipo) {

        controller.aoSusbscrever(valor, tipo);

    }
    
    @Override
    public void preencherView(DespesaAbstrata despesa) {
        controller.preencherView(despesa);
    }

    @Override
    public List<java.awt.Component> getAllComponentes() {
        return List.of(areaDescricao, btnDeletar, btnEditar, btnNovo, btnSalvar,
                btnLockTable, comboCategoria,
                comboFormaPagamento, comboParcelamento, fieldCodFornecedor,
                fieldDescricao, fieldId, fieldNota, fieldNotaEmissao, fieldValor,
                fieldVencimento, tableParcelas, btnProcurarFormaPagamento,
                btnProcurarFornecedor, btnProcurarCategoria, spinnerQuantidadeParcelas);
    }

    @Override
    public List<JTextComponent> getTextFields() {
        return List.of(fieldCodFornecedor, fieldDescricao, fieldId, fieldNota,
                fieldNotaEmissao, fieldValor, fieldVencimento, areaDescricao);
    }

    @Override
    public List<JComboBox<String>> getComboBoxes() {
        return List.of(comboCategoria, comboFormaPagamento,
                comboFormaPagamentoTabela, comboParcelamento);
    }

    @Override
    public JTextField getFieldId() {
        return fieldId;
    }

    @Override
    public JTextField getFieldDescricao() {
        return fieldDescricao;
    }

    @Override
    public JTextArea getAreaDescricao() {
        return areaDescricao;
    }

    @Override
    public JTextField getFieldVencimento() {
        return fieldVencimento;
    }

    @Override
    public JTextField getFieldValor() {
        return fieldValor;
    }

    @Override
    public JTextField getFieldCodFornecedor() {
        return fieldCodFornecedor;
    }

    @Override
    public JComboBox<String> getComboCategoria() {
        return comboCategoria;
    }

    @Override
    public JComboBox<String> getComboParcelamento() {
        return comboParcelamento;
    }

    @Override
    public JComboBox<String> getComboFormaPagamento() {
        return comboFormaPagamento;
    }

    @Override
    public JComboBox<String> getComboFormaPagamentoTabela() {
        return comboFormaPagamentoTabela;
    }

    @Override
    public JTable getTableParcelas() {
        return tableParcelas;
    }

    @Override
    public JSpinner getSpinnerQuantidadeParcelas() {
        return spinnerQuantidadeParcelas;
    }

    @Override
    public JTextField getFieldNota() {
        return fieldNota;
    }

    @Override
    public JTextField getFieldNotaEmissao() {
        return fieldNotaEmissao;
    }
    
    @Override
    public JButton getBtnLockTable(){
        return btnLockTable;
    }

    @Override
    public JButton getBtnGerarParcelas(){
        return btnGerarParcelas;
    }
    
    public JButton getBtnProcurarFormaPagamento(){
        return btnProcurarFormaPagamento;
    }
    
    public void configureComponentes() {
        comboFormaPagamentoTabela = new javax.swing.JComboBox<>();
        comboFormaPagamentoTabela.setName("comboFormaPagamentoTabela");
        configureTable();
    }

    public void configureTable() {

        tableParcelas.setEnabled(false);
        tableParcelas.setCellSelectionEnabled(false);
        tableParcelas.setRowSelectionAllowed(true);

        //Adicionando uma combo box para os valores disponiveis
        tableParcelas.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboFormaPagamentoTabela));

        //Adicionando evento para deletar linhas selecionadas ao apertar delete
        tableParcelas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int[] linhas = tableParcelas.getSelectedRows();
                    if (linhas[0] != -1) {
                        if (JOptionPane.showConfirmDialog(null, AppMensagens.REMOVER_ITENS, AppMensagens.HEADER_ATENCAO, JOptionPane.OK_CANCEL_OPTION) == 0) {
                            try {
                                controller.deletarMovimentos(linhas);
                            }catch(QuantidadeDeParcelasException q){
                                JOptionPane.showMessageDialog(null, q.getMessage(), AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    }
                }
            }

        });

        tableParcelas.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Verificando se a alteração foi de uma célula (não na estrutura da tabela)
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    Object novoValor = tableParcelas.getModel().getValueAt(row, column);
                    String nomeColuna = tableParcelas.getModel().getColumnName(column);
                    try {
                        controller.eventoTableChanged(row, column, novoValor);
                    } catch (DateTimeParseException ex) {
                        tableParcelas.getModel().setValueAt(null, row, column);
                        JOptionPane.showMessageDialog(null, AppMensagens.INFO_DATA_INCORRETA, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
                    }

                }

            }

        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupTipoDespesa = new javax.swing.ButtonGroup();
        panelMain = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fieldDescricao = new javax.swing.JTextField();
        fieldCodFornecedor = new javax.swing.JTextField();
        btnProcurarFornecedor = new javax.swing.JButton();
        comboCategoria = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaDescricao = new javax.swing.JTextArea();
        btnProcurarCategoria = new javax.swing.JButton();
        panelParcelas = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboParcelamento = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        comboFormaPagamento = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        fieldValor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableParcelas = new javax.swing.JTable();
        spinnerQuantidadeParcelas = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnLockTable = new javax.swing.JButton();
        btnProcurarFormaPagamento = new javax.swing.JButton();
        btnGerarParcelas = new javax.swing.JButton();
        fieldVencimento = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        fieldNota = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        fieldNotaEmissao = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        panelToolBar = new javax.swing.JPanel();
        btnDeletar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despesas Avulsas");
        setMaximumSize(null);
        setSize(new java.awt.Dimension(941, 752));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panelMain.setName(""); // NOI18N

        jLabel1.setText("Código");

        jLabel2.setText("Despesa/Fornecedor (*)");

        fieldDescricao.setEnabled(false);
        fieldDescricao.setName("fieldDescricao"); // NOI18N

        fieldCodFornecedor.setEnabled(false);
        fieldCodFornecedor.setName("fieldCodFornecedor"); // NOI18N

        btnProcurarFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos-24x.png"))); // NOI18N
        btnProcurarFornecedor.setToolTipText("Buscar Fornecedor");
        btnProcurarFornecedor.setEnabled(false);
        btnProcurarFornecedor.setName("btnProcurarFornecedor"); // NOI18N

        comboCategoria.setEnabled(false);
        comboCategoria.setName("comboCategoria"); // NOI18N

        jLabel3.setText("Categoria (*)");

        jLabel4.setText("Cod. Fornecedor");

        fieldId.setBackground(new java.awt.Color(255, 255, 204));
        fieldId.setEnabled(false);
        fieldId.setName("fieldId"); // NOI18N

        jLabel7.setText("Descrição (*)");

        areaDescricao.setColumns(20);
        areaDescricao.setRows(5);
        areaDescricao.setEnabled(false);
        areaDescricao.setName("areaDescricao"); // NOI18N
        jScrollPane1.setViewportView(areaDescricao);

        btnProcurarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-categoria-20x.png"))); // NOI18N
        btnProcurarCategoria.setToolTipText("Buscar Fornecedor");
        btnProcurarCategoria.setEnabled(false);
        btnProcurarCategoria.setName("btnProcurarCategoria"); // NOI18N
        btnProcurarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarCategoriaActionPerformed(evt);
            }
        });

        panelParcelas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel8.setText("Parcelamento");

        comboParcelamento.setEnabled(false);
        comboParcelamento.setName("comboParcelamento"); // NOI18N

        jLabel9.setText("Forma de Pamento");

        comboFormaPagamento.setEnabled(false);
        comboFormaPagamento.setName("comboFormaPagamento"); // NOI18N

        jLabel10.setText("Valor");
        jLabel10.setAlignmentY(0.0F);

        fieldValor.setAlignmentX(0.0F);
        fieldValor.setAlignmentY(0.0F);
        fieldValor.setEnabled(false);
        fieldValor.setName("fieldValor"); // NOI18N
        fieldValor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldValorFocusLost(evt);
            }
        });

        tableParcelas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableParcelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nº Parcela", "Data Vencimento", "Valor R$", "Data Pagamento", "F. Pagamento", "Observação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableParcelas.setColumnSelectionAllowed(true);
        tableParcelas.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tableParcelas);
        tableParcelas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (tableParcelas.getColumnModel().getColumnCount() > 0) {
            tableParcelas.getColumnModel().getColumn(0).setResizable(false);
            tableParcelas.getColumnModel().getColumn(0).setPreferredWidth(10);
            tableParcelas.getColumnModel().getColumn(1).setResizable(false);
            tableParcelas.getColumnModel().getColumn(1).setPreferredWidth(10);
            tableParcelas.getColumnModel().getColumn(2).setResizable(false);
            tableParcelas.getColumnModel().getColumn(3).setResizable(false);
            tableParcelas.getColumnModel().getColumn(4).setResizable(false);
            tableParcelas.getColumnModel().getColumn(5).setResizable(false);
        }

        spinnerQuantidadeParcelas.setEnabled(false);
        spinnerQuantidadeParcelas.setName("spinnerQuantidadeParcelas"); // NOI18N
        spinnerQuantidadeParcelas.setValue(1);

        jLabel11.setText("Parcelas");

        jLabel6.setText("Vencimento");
        jLabel6.setAlignmentY(0.0F);

        btnLockTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-lock.png"))); // NOI18N
        btnLockTable.setToolTipText("Destravar Tabela");
        btnLockTable.setEnabled(false);
        btnLockTable.setName("btnLockTable"); // NOI18N
        btnLockTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockTableActionPerformed(evt);
            }
        });

        btnProcurarFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos-24x.png"))); // NOI18N
        btnProcurarFormaPagamento.setToolTipText("Buscar Fornecedor");
        btnProcurarFormaPagamento.setEnabled(false);
        btnProcurarFormaPagamento.setName("btnProcurarFormaPagamento"); // NOI18N
        btnProcurarFormaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarFormaPagamentoActionPerformed(evt);
            }
        });

        btnGerarParcelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-parcela-24x.png"))); // NOI18N
        btnGerarParcelas.setText("Gerar Parcelas");
        btnGerarParcelas.setAlignmentY(0.0F);
        btnGerarParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarParcelasActionPerformed(evt);
            }
        });

        fieldVencimento.setEnabled(false);
        fieldVencimento.setName("fieldVencimento"); // NOI18N
        fieldVencimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldVencimentoFocusLost(evt);
            }
        });
        fieldVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldVencimentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelParcelasLayout = new javax.swing.GroupLayout(panelParcelas);
        panelParcelas.setLayout(panelParcelasLayout);
        panelParcelasLayout.setHorizontalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3))
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(spinnerQuantidadeParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(80, 80, 80)
                                .addComponent(jLabel11)))
                        .addGap(16, 16, 16)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addComponent(btnGerarParcelas)))
                .addContainerGap())
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelParcelasLayout.setVerticalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinnerQuantidadeParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel6)))))
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcelasLayout.createSequentialGroup()
                        .addComponent(btnGerarParcelas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nota Fiscal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Noto Sans", 0, 10))); // NOI18N

        fieldNota.setEnabled(false);
        fieldNota.setName("fieldNota"); // NOI18N

        jLabel5.setText("Número");

        fieldNotaEmissao.setEnabled(false);
        fieldNotaEmissao.setName("fieldNotaEmissao"); // NOI18N
        fieldNotaEmissao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNotaEmissaoFocusLost(evt);
            }
        });
        fieldNotaEmissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNotaEmissaoActionPerformed(evt);
            }
        });

        jLabel12.setText("Data Emissão");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldNota, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(fieldNotaEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldNotaEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jScrollPane1)))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fieldCodFornecedor))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9))))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(6, 6, 6)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelToolBar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnDeletar.setBackground(new java.awt.Color(242, 242, 242));
        btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-delete-24x.png"))); // NOI18N
        btnDeletar.setText("Deletar");
        btnDeletar.setToolTipText("Deletar");
        btnDeletar.setBorder(null);
        btnDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeletar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnNovo.setBackground(new java.awt.Color(242, 242, 242));
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-novo-24x.png"))); // NOI18N
        btnNovo.setText("Novo");
        btnNovo.setToolTipText("Novo");
        btnNovo.setBorder(null);
        btnNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(242, 242, 242));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-editar-24x.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setToolTipText("Editar");
        btnEditar.setBorder(null);
        btnEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnSalvar.setBackground(new java.awt.Color(242, 242, 242));
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-save-24x.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.setToolTipText("Salvar");
        btnSalvar.setBorder(null);
        btnSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelToolBarLayout = new javax.swing.GroupLayout(panelToolBar);
        panelToolBar.setLayout(panelToolBarLayout);
        panelToolBarLayout.setHorizontalGroup(
            panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToolBarLayout.createSequentialGroup()
                .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelToolBarLayout.setVerticalGroup(
            panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToolBarLayout.createSequentialGroup()
                .addGroup(panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLockTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockTableActionPerformed

        if (!tableParcelas.isEnabled()) {
            controller.ativarDesativarTabelaParcelas(true);
        } else {
            controller.ativarDesativarTabelaParcelas(false);
        }
    }//GEN-LAST:event_btnLockTableActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        controller.novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            controller.salvar();
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_SUCESSO, AppMensagens.HEADER_SUCESSO, JOptionPane.INFORMATION_MESSAGE);
        } catch(FieldsEmBrancoException e){
            JOptionPane.showMessageDialog(null,AppMensagens.INFO_PREENCHER_TODOS_CAMPOS,AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
        }catch(QuantidadeDeParcelasException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_ERRO_INESPERADO, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened



    }//GEN-LAST:event_formWindowOpened

    private void btnGerarParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarParcelasActionPerformed
        try {

            controller.gerarParcelas();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro, cheque a data do parcelamento e o valor e veja se estão nos formatos corretos !", AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldVencimento.requestFocus();
        } catch (NullPointerException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique se todos os campos referente as parcelas foram preenchidos corretamente !", AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldValor.requestFocus();
        }

    }//GEN-LAST:event_btnGerarParcelasActionPerformed

    private void btnProcurarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarCategoriaActionPerformed
        var categoriaView = context.getBean(CategoriaDespesaView.class);
        categoriaView.setVisible(true);
        categoriaView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarCategoriaActionPerformed

    private void fieldVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldVencimentoActionPerformed

    private void fieldNotaEmissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNotaEmissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNotaEmissaoActionPerformed

    private void fieldNotaEmissaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNotaEmissaoFocusLost
        try {
            fieldNotaEmissao.setText(ConversorData.paraString(ConversorData.paraData(fieldNotaEmissao.getText())));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_DATA_INCORRETA, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldNotaEmissao.setText("");
            fieldNotaEmissao.requestFocus();
        }
    }//GEN-LAST:event_fieldNotaEmissaoFocusLost

    private void fieldVencimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldVencimentoFocusLost
        try {
            fieldVencimento.setText(ConversorData.paraString(ConversorData.paraData(fieldVencimento.getText())));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_DATA_INCORRETA, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldVencimento.setText("");
            fieldVencimento.requestFocus();
        }
    }//GEN-LAST:event_fieldVencimentoFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        controller.limparCampos();
        controller.enableDisableComponents(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnProcurarFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarFormaPagamentoActionPerformed
        var formaPagamentoView = context.getBean(FormaPagamentoView.class);
        formaPagamentoView.setVisible(true);
        formaPagamentoView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarFormaPagamentoActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void fieldValorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldValorFocusLost
        try {
            fieldValor.setText(ConversorMoeda.paraString(ConversorMoeda.paraBigDecimal(fieldValor.getText())));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_VALOR_INCORRETO, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldValor.setText("");
            fieldValor.requestFocus();
        }
    }//GEN-LAST:event_fieldValorFocusLost

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        controller.editar();        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        String msg = "Deseja remover essa despesa ?\nTodos as parcelas referentes a ela serão excluídas !";
        if (JOptionPane.showConfirmDialog(null, msg, "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
            controller.deletar();
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_SUCESSO, AppMensagens.HEADER_SUCESSO, JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDeletarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDescricao;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGerarParcelas;
    private javax.swing.ButtonGroup btnGroupTipoDespesa;
    private javax.swing.JButton btnLockTable;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnProcurarCategoria;
    private javax.swing.JButton btnProcurarFormaPagamento;
    private javax.swing.JButton btnProcurarFornecedor;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JComboBox<String> comboFormaPagamento;
    private javax.swing.JComboBox<String> comboParcelamento;
    private javax.swing.JTextField fieldCodFornecedor;
    private javax.swing.JTextField fieldDescricao;
    private javax.swing.JTextField fieldId;
    private javax.swing.JTextField fieldNota;
    private javax.swing.JTextField fieldNotaEmissao;
    private javax.swing.JTextField fieldValor;
    private javax.swing.JTextField fieldVencimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelParcelas;
    private javax.swing.JPanel panelToolBar;
    private javax.swing.JSpinner spinnerQuantidadeParcelas;
    private javax.swing.JTable tableParcelas;
    // End of variables declaration//GEN-END:variables



}
