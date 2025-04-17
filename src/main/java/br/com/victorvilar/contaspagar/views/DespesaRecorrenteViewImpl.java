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
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.interfaces.Subscriber;
import jakarta.annotation.PostConstruct;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
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
                fieldDataInicio,
                fieldDataFim);
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
                fieldMesVencimento,
                fieldDataInicio,
                fieldDataFim);
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
    public JTextField getFieldDiaVencimento(){
        return fieldDiaVencimento;
    }
    
    @Override
    public JTextField getFieldMesVencimento(){
        return fieldMesVencimento;
    }
    
    @Override
    public JTextField getFieldDataInicio(){
        return fieldDataInicio;
    }
    
    @Override
    public JTextField getFieldDataFim(){
        return fieldDataFim;
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
                        
                        if (JOptionPane.showConfirmDialog(null, "Deseja remover os lançamentos selecionadas ?", "Atenção", JOptionPane.OK_CANCEL_OPTION) == 0) {
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
        jLabel6 = new javax.swing.JLabel();
        fieldDataInicio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        fieldDataFim = new javax.swing.JTextField();
        panelToolBar = new javax.swing.JPanel();
        btnDeletar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        panelParcelas = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboParcelamento = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        fieldValor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableParcelas = new javax.swing.JTable();
        btnLockTable = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        fieldDiaVencimento = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fieldMesVencimento = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Despesas Recorrentes");
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

        btnProcurarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos.png"))); // NOI18N
        btnProcurarCategoria.setToolTipText("Buscar Fornecedor");
        btnProcurarCategoria.setEnabled(false);
        btnProcurarCategoria.setName("btnProcurarCategoria"); // NOI18N
        btnProcurarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarCategoriaActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel9.setText("Forma de Pamento");

        comboFormaPagamento.setEnabled(false);
        comboFormaPagamento.setName("comboFormaPagamento"); // NOI18N

        btnProcurarFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icon-binoculos.png"))); // NOI18N
        btnProcurarFormaPagamento.setToolTipText("Buscar Fornecedor");
        btnProcurarFormaPagamento.setEnabled(false);
        btnProcurarFormaPagamento.setName("btnProcurarFormaPagamento"); // NOI18N
        btnProcurarFormaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarFormaPagamentoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel6.setText("Data Início");

        fieldDataInicio.setEnabled(false);
        fieldDataInicio.setName("fieldDataInicio"); // NOI18N
        fieldDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDataInicioFocusLost(evt);
            }
        });
        fieldDataInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDataInicioActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel14.setText("Data Fim");

        fieldDataFim.setEnabled(false);
        fieldDataFim.setName("fieldDataFim"); // NOI18N
        fieldDataFim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDataFimFocusLost(evt);
            }
        });
        fieldDataFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDataFimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(94, 94, 94)
                        .addComponent(jLabel7))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel2))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(comboFormaPagamento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboCategoria, javax.swing.GroupLayout.Alignment.LEADING, 0, 146, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addComponent(fieldDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fieldDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcurarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProcurarFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnProcurarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addContainerGap(10, Short.MAX_VALUE))
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
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

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
        jLabel8.setText("Periocidade");

        comboParcelamento.setEnabled(false);
        comboParcelamento.setName("comboParcelamento"); // NOI18N

        jLabel10.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel10.setText("Valor");

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

        btnLockTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lock.png"))); // NOI18N
        btnLockTable.setToolTipText("Destravar Tabela");
        btnLockTable.setEnabled(false);
        btnLockTable.setName("btnLockTable"); // NOI18N
        btnLockTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockTableActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel11.setText("Dia");

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

        jLabel12.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel12.setText("Mês ");

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
                                .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(fieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(fieldDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldMesVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(492, 492, 492)
                                .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelParcelasLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 7, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelParcelasLayout.setVerticalGroup(
            panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParcelasLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12)))
                .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(comboParcelamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldMesVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelParcelasLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnLockTable, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
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
                .addComponent(panelParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        controller.novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            controller.salvar();
            JOptionPane.showMessageDialog(null, "Despesa salva com sucesso !", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (FieldsEmBrancoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "O valor informado não é um número", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void btnProcurarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarCategoriaActionPerformed
        var categoriaView = context.getBean(CategoriaDespesaView.class);
        categoriaView.setVisible(true);
        categoriaView.adicionarSubscribers(this);
    }//GEN-LAST:event_btnProcurarCategoriaActionPerformed

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

    private void fieldMesVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldMesVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldMesVencimentoActionPerformed

    private void fieldMesVencimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldMesVencimentoFocusLost
        try {
            controller.mesVencimentoAoPerderFoco();
        } catch (MesVencimentoInvalidoException  d) {
            getFieldMesVencimento().setText("");
            getFieldMesVencimento().requestFocus();
            JOptionPane.showMessageDialog(null, d.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException d){
            getFieldMesVencimento().setText("");
            getFieldMesVencimento().requestFocus();
            JOptionPane.showMessageDialog(null, "O valor informado não é um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldMesVencimentoFocusLost

    private void fieldDiaVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDiaVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDiaVencimentoActionPerformed

    private void fieldDiaVencimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDiaVencimentoFocusLost
        try {
            controller.diaVencimentoAoPerderFoco();
        } catch (DiaVencimentoInvalidoException d) {
            getFieldDiaVencimento().setText("");
            getFieldDiaVencimento().requestFocus();
            JOptionPane.showMessageDialog(null, d.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException d) {
            getFieldDiaVencimento().setText("");
            getFieldDiaVencimento().requestFocus();
            JOptionPane.showMessageDialog(null, "O valor informado não é um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
               
    }//GEN-LAST:event_fieldDiaVencimentoFocusLost

    private void fieldDataFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDataFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDataFimActionPerformed

    private void fieldDataFimFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDataFimFocusLost
        try {
            fieldDataFim.setText(ConversorData.paraString(ConversorData.paraData(fieldDataFim.getText())));
        } catch (DateTimeParseException ex) {
            fieldDataFim.setText("");
            fieldDataFim.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldDataFimFocusLost

    private void fieldDataInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDataInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDataInicioActionPerformed

    private void fieldDataInicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDataInicioFocusLost
        try {
            fieldDataInicio.setText(ConversorData.paraString(ConversorData.paraData(fieldDataInicio.getText())));
        } catch (DateTimeParseException ex) {
            fieldDataInicio.setText("");
            fieldDataInicio.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldDataInicioFocusLost

    private void btnLockTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockTableActionPerformed

        if (!tableParcelas.isEnabled()) {
            tableParcelas.setEnabled(true);
            btnLockTable.setIcon(new ImageIcon(getClass().getResource("/img/icon-unlock.png")));

        } else {
            tableParcelas.setEnabled(false);
            btnLockTable.setIcon(new ImageIcon(getClass().getResource("/img/icon-lock.png")));

        }
    }//GEN-LAST:event_btnLockTableActionPerformed

    private void fieldValorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldValorFocusLost
        try {
            if(!fieldValor.getText().trim().isEmpty()){
                fieldValor.setText(ConversorMoeda.paraString(ConversorMoeda.paraBigDecimal(fieldValor.getText())));
            }
        } catch (DateTimeParseException ex) {
            fieldValor.setText("");
            fieldValor.requestFocus();
            JOptionPane.showMessageDialog(null, "A data informada não esta correta !", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_fieldValorFocusLost

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        controller.editar();
    }//GEN-LAST:event_btnEditarActionPerformed


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
    private java.awt.Choice choice1;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JComboBox<String> comboFormaPagamento;
    private javax.swing.JComboBox<String> comboParcelamento;
    private javax.swing.JTextField fieldCodFornecedor;
    private javax.swing.JTextField fieldDataFim;
    private javax.swing.JTextField fieldDataInicio;
    private javax.swing.JTextField fieldDescricao;
    private javax.swing.JTextField fieldDiaVencimento;
    private javax.swing.JTextField fieldId;
    private javax.swing.JTextField fieldMesVencimento;
    private javax.swing.JTextField fieldValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
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
