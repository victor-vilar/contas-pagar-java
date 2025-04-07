/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.thveiculos.erp.views.despesas;

import br.com.thveiculos.erp.controllers.despesas.DespesaViewController;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.util.ConversorMoeda;
import br.com.thveiculos.erp.views.interfaces.Subscriber;
import jakarta.annotation.PostConstruct;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

@Component
@Lazy
public class DespesaAvulsaView extends javax.swing.JFrame implements Subscriber {
    
    private javax.swing.JComboBox<String> comboFormaPagamentoTabela;
    private final DespesaViewController controller;
    private static final String TIPO_DESPESA = "AVULSA";
    private final ApplicationContext context;
    
    @Autowired
    public DespesaAvulsaView(DespesaViewController controller, ApplicationContext context) {
        this.controller = controller;
        this.controller.setView(this);
        this.context = context;
        
    }
    
    @PostConstruct
    public void configurarComponent() {
        initComponents();
        configureComponentes();
        
    }
    
    @Override
    public void atualizar(String valor, String tipo) {
        
        if (tipo.equals("Categoria Despesas")) {
            this.comboCategoria.getModel().setSelectedItem(valor);
        }
        
        if (tipo.equals("Formas Pagamento")) {
            this.comboFormaPagamento.getModel().setSelectedItem(valor);
        }
        
    }
    
    public void configureComponentes() {
        comboFormaPagamentoTabela = new javax.swing.JComboBox<>();
        configureTable();
    }
    
    public void configureTable() {
        
        tableParcelas.setEnabled(false);
        tableParcelas.setCellSelectionEnabled(false);
        tableParcelas.setRowSelectionAllowed(true);

        //Adicionando uma combo box para os valores disponiveis
        tableParcelas.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboFormaPagamentoTabela));

        //Adicionando evento que envia a linha que foi selecionada pra a lista
        //A linha pode ter colunas que sofreram alteração, por isso as linhas
        //serão guardadas para que seja possivel compara-las
//        tableParcelas.getSelectionModel().addListSelectionListener(new ListSelectionListener(){;
//            public void valueChanged(ListSelectionEvent e) {
//                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
//                
//                int[] linhas = lsm.getSelectedIndices();
//                for(int i = 0; i < linhas.length; i++ ){
//                    controller.adicionarLinhaAlterada(linhas[i]);
//                }
//            }
//            
//        });
        //Adicionando evento para deletar linhas selecionadas ao apertar delete
        tableParcelas.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    
                    int[] linhas = tableParcelas.getSelectedRows();
                    if (linhas[0] != -1) {
                        
                        if (JOptionPane.showConfirmDialog(null, "Deseja remover as parcelas selecionadas ?", "Atenção", JOptionPane.OK_CANCEL_OPTION) == 0) {
                            controller.deletarMovimentos(linhas);
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
                    try{
                        controller.eventoTableChanged(row, column,novoValor);
                    } catch (DateTimeParseException ex) {
                        tableParcelas.getModel().setValueAt(null, row, column);
                        JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro de Conversão", JOptionPane.ERROR_MESSAGE);
                    }   
                    
                }
                
            }
        
        });

    }

    public JTextArea getAreaDescricao() {
        return areaDescricao;
    }

    public JButton getBtnDeletar() {
        return btnDeletar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnTable() {
        return btnLockTable;
    }

    public JComboBox<String> getComboCategoria() {
        return comboCategoria;
    }

    public JComboBox<String> getComboFormaPagamento() {
        return comboFormaPagamento;
    }

    public JComboBox<String> getComboFormaPagamentoTabela() {
        return comboFormaPagamentoTabela;
    }

    public JComboBox<String> getComboParcelamento() {
        return comboParcelamento;
    }

    public JTextField getFieldCodFornecedor() {
        return fieldCodFornecedor;
    }

    public JTextField getFieldDescricao() {
        return fieldDescricao;
    }

    public JTextField getFieldId() {
        return fieldId;
    }

    public JTextField getFieldNota() {
        return fieldNota;
    }

    public JTextField getFieldNotaEmissao() {
        return fieldNotaEmissao;
    }

    public JTextField getFieldValorTotal() {
        return fieldValorTotal;
    }

    public JTextField getFieldVencimentoParcela() {
        return fieldVencimentoParcela;
    }

    public JSpinner getSpinnerQuantidadeParcelas() {
        return spinnerQuantidadeParcelas;
    }

    public JTable getTableParcelas() {
        return tableParcelas;
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JPanel getPanelParcelas() {
        return panelParcelas;

    }

    public String tipoDespesa() {
        return TIPO_DESPESA;
    }

    public List<java.awt.Component> listaDeComponentes() {
        return List.of(areaDescricao, btnDeletar, btnEditar, btnNovo, btnSalvar,
                btnLockTable, comboCategoria,
                comboFormaPagamento, comboParcelamento, fieldCodFornecedor,
                fieldDescricao, fieldId, fieldNota, fieldNotaEmissao, fieldValorTotal,
                fieldVencimentoParcela, tableParcelas, btnProcurarFormaPagamento,
                btnProcurarFornecedor, btnProcurarCategoria, spinnerQuantidadeParcelas);

    }

    public List<JTextComponent> getTextFields() {
        return List.of(fieldCodFornecedor, fieldDescricao, fieldId, fieldNota,
                fieldNotaEmissao, fieldValorTotal, fieldVencimentoParcela, areaDescricao);
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
        fieldNota = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fieldDescricao = new javax.swing.JTextField();
        fieldCodFornecedor = new javax.swing.JTextField();
        btnProcurarFornecedor = new javax.swing.JButton();
        comboCategoria = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fieldId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaDescricao = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        btnProcurarCategoria = new javax.swing.JButton();
        fieldNotaEmissao = new javax.swing.JTextField();
        panelToolBar = new javax.swing.JPanel();
        btnDeletar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        panelParcelas = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboParcelamento = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        comboFormaPagamento = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        fieldValorTotal = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableParcelas = new javax.swing.JTable();
        spinnerQuantidadeParcelas = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnLockTable = new javax.swing.JButton();
        btnProcurarFormaPagamento = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        fieldVencimentoParcela = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despesa");
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

        panelMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelMain.setName(""); // NOI18N

        jLabel1.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código");

        fieldNota.setEnabled(false);
        fieldNota.setName("fieldNota"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel2.setText("Despesa/Fornecedor");

        fieldDescricao.setEnabled(false);
        fieldDescricao.setName("fieldDescricao"); // NOI18N

        fieldCodFornecedor.setEnabled(false);
        fieldCodFornecedor.setName("fieldCodFornecedor"); // NOI18N

        btnProcurarFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos.png"))); // NOI18N
        btnProcurarFornecedor.setToolTipText("Buscar Fornecedor");
        btnProcurarFornecedor.setEnabled(false);
        btnProcurarFornecedor.setName("btnProcurarFornecedor"); // NOI18N

        comboCategoria.setEnabled(false);
        comboCategoria.setName("comboCategoria"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel3.setText("Categoria");

        jLabel4.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel4.setText("Cod. Fornecedor");

        jLabel5.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel5.setText("Nota Fiscal");

        fieldId.setBackground(new java.awt.Color(255, 255, 204));
        fieldId.setEnabled(false);
        fieldId.setName("fieldId"); // NOI18N

        jLabel7.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel7.setText("Descrição");

        areaDescricao.setColumns(20);
        areaDescricao.setRows(5);
        areaDescricao.setEnabled(false);
        areaDescricao.setName("areaDescricao"); // NOI18N
        jScrollPane1.setViewportView(areaDescricao);

        jLabel12.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel12.setText("Data Emissão");

        btnProcurarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos.png"))); // NOI18N
        btnProcurarCategoria.setToolTipText("Buscar Fornecedor");
        btnProcurarCategoria.setEnabled(false);
        btnProcurarCategoria.setName("btnProcurarCategoria"); // NOI18N
        btnProcurarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarCategoriaActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldNota, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldNotaEmissao))
                        .addGap(13, 13, 13)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldNotaEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelToolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDeletar.setBackground(new java.awt.Color(242, 242, 242));
        btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/3.png"))); // NOI18N
        btnDeletar.setToolTipText("Deletar");
        btnDeletar.setBorder(null);

        btnNovo.setBackground(new java.awt.Color(242, 242, 242));
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1.png"))); // NOI18N
        btnNovo.setToolTipText("Novo");
        btnNovo.setBorder(null);
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(242, 242, 242));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/2.png"))); // NOI18N
        btnEditar.setToolTipText("Editar");
        btnEditar.setBorder(null);

        btnSalvar.setBackground(new java.awt.Color(242, 242, 242));
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-save.png"))); // NOI18N
        btnSalvar.setToolTipText("Deletar");
        btnSalvar.setBorder(null);
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
                .addGap(76, 76, 76)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelToolBarLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(189, Short.MAX_VALUE)))
        );
        panelToolBarLayout.setVerticalGroup(
            panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToolBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panelToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelToolBarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        panelParcelas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel8.setText("Parcelamento");

        comboParcelamento.setEnabled(false);
        comboParcelamento.setName("comboParcelamento"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel9.setText("Forma de Pamento");

        comboFormaPagamento.setEnabled(false);
        comboFormaPagamento.setName("comboFormaPagamento"); // NOI18N

        jLabel10.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel10.setText("Valor");

        fieldValorTotal.setEnabled(false);
        fieldValorTotal.setName("fieldValorTotal"); // NOI18N
        fieldValorTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldValorTotalFocusLost(evt);
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

        jLabel11.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel11.setText("Nº Parcelas");

        jLabel6.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel6.setText("Vencimento");

        btnLockTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lock.png"))); // NOI18N
        btnLockTable.setToolTipText("Destravar Tabela");
        btnLockTable.setEnabled(false);
        btnLockTable.setName("btnLockTable"); // NOI18N
        btnLockTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockTableActionPerformed(evt);
            }
        });

        btnProcurarFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos.png"))); // NOI18N
        btnProcurarFormaPagamento.setToolTipText("Buscar Fornecedor");
        btnProcurarFormaPagamento.setEnabled(false);
        btnProcurarFormaPagamento.setName("btnProcurarFormaPagamento"); // NOI18N
        btnProcurarFormaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarFormaPagamentoActionPerformed(evt);
            }
        });

        jButton1.setText("Gerar Parcelas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fieldVencimentoParcela.setEnabled(false);
        fieldVencimentoParcela.setName("fieldVencimentoParcela"); // NOI18N
        fieldVencimentoParcela.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldVencimentoParcelaFocusLost(evt);
            }
        });
        fieldVencimentoParcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldVencimentoParcelaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelParcelasLayout = new javax.swing.GroupLayout(panelParcelas);
        panelParcelas.setLayout(panelParcelasLayout);
        panelParcelasLayout.setHorizontalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(spinnerQuantidadeParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldVencimentoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLockTable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        panelParcelasLayout.setVerticalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcelasLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldVencimentoParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23))
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jButton1))
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))))
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelParcelasLayout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(6, 6, 6)
                                    .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelParcelasLayout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(6, 6, 6)
                                    .addComponent(spinnerQuantidadeParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelParcelas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelParcelas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLockTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockTableActionPerformed

        if (!tableParcelas.isEnabled()) {
            tableParcelas.setEnabled(true);
            btnLockTable.setIcon(new ImageIcon(getClass().getResource("/img/icon-unlock.png")));

        } else {
            tableParcelas.setEnabled(false);
            btnLockTable.setIcon(new ImageIcon(getClass().getResource("/img/icon-lock.png")));

        }
    }//GEN-LAST:event_btnLockTableActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        controller.novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            controller.salvar();
            JOptionPane.showMessageDialog(null, "Despesa salva com sucesso !", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar", "erro", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        System.out.println("Iniciei...");
        controller.inicializarComboBox();

    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            controller.gerarParcelas();        // TODO add your handling code here:
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro, cheque a data do parcelamento e o valor e veja se estão nos formatos corretos !", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique se todos os campos referente as parcelas foram preenchidos corretamente !", "Erro na Geração das Parcelas", JOptionPane.ERROR_MESSAGE);

}

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnProcurarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarCategoriaActionPerformed
        var categoriaView = context.getBean(CategoriaDespesaView.class);
        categoriaView.setVisible(true);
        categoriaView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarCategoriaActionPerformed

    private void fieldVencimentoParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldVencimentoParcelaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldVencimentoParcelaActionPerformed

    private void fieldNotaEmissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNotaEmissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNotaEmissaoActionPerformed

    private void fieldNotaEmissaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNotaEmissaoFocusLost
        try {
            fieldNotaEmissao.setText(ConversorData.paraString(ConversorData.paraData(fieldNotaEmissao.getText())));
        } catch (DateTimeParseException ex) {
            fieldNotaEmissao.setText("");
            fieldNotaEmissao.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldNotaEmissaoFocusLost

    private void fieldVencimentoParcelaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldVencimentoParcelaFocusLost
        try {
            fieldVencimentoParcela.setText(ConversorData.paraString(ConversorData.paraData(fieldVencimentoParcela.getText())));
        } catch (DateTimeParseException ex) {
            fieldVencimentoParcela.setText("");
            fieldVencimentoParcela.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldVencimentoParcelaFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        controller.limparCampos();
    }//GEN-LAST:event_formWindowClosing

    private void btnProcurarFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarFormaPagamentoActionPerformed
        var formaPagamentoView = context.getBean(FormaPagamentoView.class  

);
        formaPagamentoView.setVisible(true);
        formaPagamentoView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarFormaPagamentoActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void fieldValorTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldValorTotalFocusLost
        try {
            fieldValorTotal.setText(ConversorMoeda.paraString(ConversorMoeda.paraBigDecimal(fieldValorTotal.getText())));
        } catch (DateTimeParseException ex) {
            fieldValorTotal.setText("");
            fieldValorTotal.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldValorTotalFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDescricao;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JButton btnEditar;
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
    private javax.swing.JTextField fieldValorTotal;
    private javax.swing.JTextField fieldVencimentoParcela;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelParcelas;
    private javax.swing.JPanel panelToolBar;
    private javax.swing.JSpinner spinnerQuantidadeParcelas;
    private javax.swing.JTable tableParcelas;
    // End of variables declaration//GEN-END:variables

}
