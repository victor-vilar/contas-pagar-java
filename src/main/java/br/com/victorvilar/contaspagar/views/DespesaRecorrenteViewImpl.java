/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.victorvilar.contaspagar.views;

import br.com.victorvilar.contaspagar.controllers.DespesaRecorrenteController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.exceptions.DiaVencimentoInvalidoException;
import br.com.victorvilar.contaspagar.exceptions.FieldsEmBrancoException;
import br.com.victorvilar.contaspagar.exceptions.MesVencimentoInvalidoException;
import br.com.victorvilar.contaspagar.util.AppMensagens;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.interfaces.Subscriber;
import jakarta.annotation.PostConstruct;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
import br.com.victorvilar.contaspagar.views.interfaces.DespesaRecorrenteView;
import javax.swing.JButton;
import javax.swing.JCheckBox;

@Component
@Lazy
public class DespesaRecorrenteViewImpl extends javax.swing.JFrame implements Subscriber, DespesaRecorrenteView {
    
    private javax.swing.JComboBox<String> comboFormaPagamentoTabela;
    private final DespesaRecorrenteController controller;
    private final ApplicationContext context;
    
    @Autowired
    public DespesaRecorrenteViewImpl(DespesaRecorrenteController controller,ApplicationContext context) {
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
        return List.of(
                areaDescricao,
                btnDeletar,
                btnEditar,
                btnNovo,
                btnSalvar,
                btnLockTable,
                comboCategoria,
                comboFormaPagamento,
                comboParcelamento,
                fieldCodFornecedor,
                fieldDescricao,
                fieldId,
                fieldValor,
                tableParcelas,
                btnProcurarFormaPagamento,
                btnProcurarFornecedor,
                btnProcurarCategoria,
                fieldDiaVencimento,
                fieldMesVencimento,
                chequeAtivo);

    }

    @Override
    public List<JTextComponent> getTextFields() {
        return List.of(
                fieldCodFornecedor,
                fieldDescricao,
                fieldId, 
                fieldValor,
                areaDescricao,
                fieldDiaVencimento,
                fieldMesVencimento);
    }

    @Override
    public List<JComboBox<String>> getComboBoxes() {
        return List.of(comboCategoria,
                comboFormaPagamento,
                comboFormaPagamentoTabela,
                comboParcelamento);
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
    public JButton getBtnLockTable() {
        return btnLockTable;
    }
    
    @Override
    public JTextField getFieldDiaVencimento(){
        return fieldDiaVencimento;
    }
    
    @Override
    public JTextField getFieldMesVencimento(){
        return fieldMesVencimento;
    }
    

    @Override
    public JCheckBox getAtivoBox(){
        return chequeAtivo;
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
        choice1 = new java.awt.Choice();
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
        jLabel9 = new javax.swing.JLabel();
        comboFormaPagamento = new javax.swing.JComboBox<>();
        btnProcurarFormaPagamento = new javax.swing.JButton();
        comboParcelamento = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        fieldValor = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        fieldDiaVencimento = new javax.swing.JTextField();
        fieldMesVencimento = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        chequeAtivo = new javax.swing.JCheckBox();
        panelToolBar = new javax.swing.JPanel();
        btnDeletar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        panelParcelas = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableParcelas = new javax.swing.JTable();
        btnLockTable = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despesas Recorrentes");
        setSize(new java.awt.Dimension(941, 945));
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

        jLabel9.setText("Forma de Pamento (*)");

        comboFormaPagamento.setEnabled(false);
        comboFormaPagamento.setName("comboFormaPagamento"); // NOI18N

        btnProcurarFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-pagamento-20x.png"))); // NOI18N
        btnProcurarFormaPagamento.setToolTipText("Buscar Fornecedor");
        btnProcurarFormaPagamento.setEnabled(false);
        btnProcurarFormaPagamento.setName("btnProcurarFormaPagamento"); // NOI18N
        btnProcurarFormaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarFormaPagamentoActionPerformed(evt);
            }
        });

        comboParcelamento.setEnabled(false);
        comboParcelamento.setName("comboParcelamento"); // NOI18N
        comboParcelamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboParcelamentoItemStateChanged(evt);
            }
        });
        comboParcelamento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboParcelamentoFocusLost(evt);
            }
        });

        jLabel8.setText("Periocidade (*)");

        jLabel10.setText("Valor (*)");

        fieldValor.setEnabled(false);
        fieldValor.setName("fieldValor"); // NOI18N
        fieldValor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldValorFocusLost(evt);
            }
        });

        jLabel11.setText("Dia (*)");

        fieldDiaVencimento.setEnabled(false);
        fieldDiaVencimento.setName("fieldDiaVencimento"); // NOI18N
        fieldDiaVencimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDiaVencimentoFocusLost(evt);
            }
        });
        fieldDiaVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDiaVencimentoActionPerformed(evt);
            }
        });

        fieldMesVencimento.setEnabled(false);
        fieldMesVencimento.setName("fieldMesVencimento"); // NOI18N
        fieldMesVencimento.setVerifyInputWhenFocusTarget(false);
        fieldMesVencimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldMesVencimentoFocusLost(evt);
            }
        });
        fieldMesVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldMesVencimentoActionPerformed(evt);
            }
        });

        jLabel12.setText("Mês ");

        chequeAtivo.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        chequeAtivo.setText("Ativo");
        chequeAtivo.setEnabled(false);
        chequeAtivo.setName("chequeAtivo"); // NOI18N
        chequeAtivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequeAtivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chequeAtivo))))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fieldCodFornecedor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboParcelamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldValor)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 105, Short.MAX_VALUE))
                            .addComponent(comboCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboFormaPagamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(fieldDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldMesVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)))
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chequeAtivo))
                .addGap(8, 8, 8)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldMesVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMainLayout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(jLabel12)
                                .addGap(35, 35, 35)))
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fieldDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79)
                                .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
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
                .addGap(1, 1, 1)
                .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        btnLockTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-lock.png"))); // NOI18N
        btnLockTable.setToolTipText("Destravar Tabela");
        btnLockTable.setEnabled(false);
        btnLockTable.setName("btnLockTable"); // NOI18N
        btnLockTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockTableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelParcelasLayout = new javax.swing.GroupLayout(panelParcelas);
        panelParcelas.setLayout(panelParcelasLayout);
        panelParcelasLayout.setHorizontalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelParcelasLayout.setVerticalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelParcelas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        controller.novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            controller.salvar();
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_SUCESSO, AppMensagens.HEADER_SUCESSO, JOptionPane.INFORMATION_MESSAGE);
        } catch (FieldsEmBrancoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_VALOR_INCORRETO, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        controller.limparCampos();
        controller.enableDisableComponents(false);
    }//GEN-LAST:event_formWindowClosing

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void btnLockTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockTableActionPerformed

       if (!tableParcelas.isEnabled()) {
            controller.ativarDesativarTabelaParcelas(true);
        } else {
            controller.ativarDesativarTabelaParcelas(false);
        }
    }//GEN-LAST:event_btnLockTableActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        controller.editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        String msg = "Deseja remover essa despesa ?\nTodos as parcelas referentes a ela serão excluídas !";
        if (JOptionPane.showConfirmDialog(null, msg, "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
            controller.deletar();
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_SUCESSO, AppMensagens.HEADER_SUCESSO, JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void chequeAtivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequeAtivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequeAtivoActionPerformed

    private void fieldMesVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldMesVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldMesVencimentoActionPerformed

    private void fieldMesVencimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldMesVencimentoFocusLost
        try {
            controller.mesVencimentoAoPerderFoco();
        } catch (MesVencimentoInvalidoException  d) {
            JOptionPane.showMessageDialog(null, d.getMessage(), AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            getFieldMesVencimento().setText("");
            getFieldMesVencimento().requestFocus();
          
        } catch (NumberFormatException d){
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_DATA_INCORRETA, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            getFieldMesVencimento().setText("");
            getFieldMesVencimento().requestFocus();
            
        }
    }//GEN-LAST:event_fieldMesVencimentoFocusLost

    private void fieldDiaVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDiaVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDiaVencimentoActionPerformed

    private void fieldDiaVencimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDiaVencimentoFocusLost
        try {
            controller.diaVencimentoAoPerderFoco();
        } catch (DiaVencimentoInvalidoException d) {
            JOptionPane.showMessageDialog(null, d.getMessage(), AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            getFieldDiaVencimento().setText("");
            getFieldDiaVencimento().requestFocus();
        } catch (NumberFormatException d) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_DATA_INCORRETA, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            getFieldDiaVencimento().setText("");
            getFieldDiaVencimento().requestFocus();
        }

    }//GEN-LAST:event_fieldDiaVencimentoFocusLost

    private void fieldValorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldValorFocusLost
        try {
            if(!fieldValor.getText().trim().isEmpty()){
                fieldValor.setText(ConversorMoeda.paraString(ConversorMoeda.paraBigDecimal(fieldValor.getText())));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, AppMensagens.INFO_VALOR_INCORRETO, AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            fieldValor.setText("");
            fieldValor.requestFocus();
        }
    }//GEN-LAST:event_fieldValorFocusLost

    private void btnProcurarFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarFormaPagamentoActionPerformed
        var formaPagamentoView = context.getBean(FormaPagamentoView.class);
        formaPagamentoView.setVisible(true);
        formaPagamentoView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarFormaPagamentoActionPerformed

    private void btnProcurarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarCategoriaActionPerformed
        var categoriaView = context.getBean(CategoriaDespesaView.class);
        categoriaView.setVisible(true);
        categoriaView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarCategoriaActionPerformed

    private void comboParcelamentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboParcelamentoFocusLost
        
    }//GEN-LAST:event_comboParcelamentoFocusLost

    private void comboParcelamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboParcelamentoItemStateChanged
        String parcelamento = (String)comboParcelamento.getSelectedItem();
        if(parcelamento == null){
            fieldMesVencimento.setEnabled(false);
            return;
        }
        
        if(!parcelamento.equals("ANUAL")){
            fieldMesVencimento.setText("");
            fieldMesVencimento.setEnabled(false);
        }else{
            fieldMesVencimento.setEnabled(true);
        }
        
        try{
            controller.checarQuinzena();
        }catch(DiaVencimentoInvalidoException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), AppMensagens.HEADER_ERRO, JOptionPane.ERROR_MESSAGE);
            comboParcelamento.setSelectedIndex(-1);
            comboParcelamento.requestFocus();
        }
        
    }//GEN-LAST:event_comboParcelamentoItemStateChanged


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
    private javax.swing.JCheckBox chequeAtivo;
    private java.awt.Choice choice1;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JComboBox<String> comboFormaPagamento;
    private javax.swing.JComboBox<String> comboParcelamento;
    private javax.swing.JTextField fieldCodFornecedor;
    private javax.swing.JTextField fieldDescricao;
    private javax.swing.JTextField fieldDiaVencimento;
    private javax.swing.JTextField fieldId;
    private javax.swing.JTextField fieldMesVencimento;
    private javax.swing.JTextField fieldValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelParcelas;
    private javax.swing.JPanel panelToolBar;
    private javax.swing.JTable tableParcelas;
    // End of variables declaration//GEN-END:variables

}
