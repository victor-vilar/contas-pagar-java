/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.victorvilar.contaspagar.views;

import br.com.victorvilar.contaspagar.controllers.MovimentoPagamentoController;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.interfaces.DespesaAvulsaView;
import br.com.victorvilar.contaspagar.views.interfaces.DespesaRecorrenteView;
import jakarta.annotation.PostConstruct;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author victor
 */
@Component
@Lazy
public class MovimentoPagamentoView extends javax.swing.JFrame{

    private ApplicationContext context;
    private MovimentoPagamentoController controller;
    
    @Autowired
    public MovimentoPagamentoView(ApplicationContext context, MovimentoPagamentoController controller) {
        this.context = context;
        this.controller = controller;
        controller.setView(this);
    }

    @PostConstruct
    public void configurarComponents() {
        initComponents();
        configureComponentes();
        setLocationRelativeTo(null);
    }

    void configureComponentes() {
        configurarTabela();
    }

    void configurarTabela() {

        tableMovimentos.getColumnModel().getColumn(0).setMaxWidth(50);
        tableMovimentos.getColumnModel().getColumn(1).setMaxWidth(90);
        tableMovimentos.getColumnModel().getColumn(3).setMaxWidth(80);
        tableMovimentos.getColumnModel().getColumn(4).setMaxWidth(100);
        tableMovimentos.getColumnModel().getColumn(5).setMaxWidth(100);
        tableMovimentos.getColumnModel().getColumn(6).setMaxWidth(90);

        //table.setAutoCreateRowSorter(true);;
        //cria um table sorter e já realiza um sorte pela coluna de data
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableMovimentos.getModel());
        tableMovimentos.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int colunaDataVencimento = 1;
        int colunaValorPagamento = 4;
        int colunaDataPagamento = 6;
        sortKeys.add(new RowSorter.SortKey(colunaDataVencimento, SortOrder.ASCENDING));
        sorter.setComparator(colunaDataVencimento, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                LocalDate d1 = ConversorData.paraData(o1);
                LocalDate d2 = ConversorData.paraData(o2);
                return d1.compareTo(d2);
            }
        });
        
        sorter.setComparator(colunaDataPagamento, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                LocalDate d1 = ConversorData.paraData(o1);
                LocalDate d2 = ConversorData.paraData(o2);
                if((d1 == null) && (d2 == null)){
                    return 0;
                }
                
                if(d1 == null){
                    return -1;
                }
                
                 if (d2 == null) {
                    return 1;
                }
                
                
                return d1.compareTo(d2);
            }
        });

        sorter.setComparator(colunaValorPagamento, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                BigDecimal d1 = ConversorMoeda.paraBigDecimal(o1);
                BigDecimal d2 = ConversorMoeda.paraBigDecimal(o2);
                return d1.compareTo(d2);
            }
        });
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        //------------------
        
        
        // evento de clicar duas vezes na linha
        tableMovimentos.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

                JTable table = (JTable) mouseEvent.getSource();
                int row = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row != -1) {
                    Long id = (Long) table.getValueAt(row, 0);
                    var finalizarView = context.getBean(FinalizarMovimentoPagamentoView.class);
                    finalizarView.buscar(id);
                    finalizarView.setVisible(true);

                }
            }
        });
        
        
        // evento de apertar f11
        tableMovimentos.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_F1) {

                    int row = tableMovimentos.getSelectedRow();
                    if (tableMovimentos.getSelectedRow() != -1 && row != -1) {
                        
                        Long id = (Long) tableMovimentos.getValueAt(row, 0);
                        DespesaAbstrata despesa = controller.buscarDespesa(id);
                        
                        if(despesa.getTipo().equals("AVULSA")){
                            var avulsa = context.getBean(DespesaAvulsaViewImpl.class);
                            avulsa.preencherView(despesa);
                            avulsa.setVisible(true);
                        }else{
                            var recorrente = context.getBean(DespesaRecorrenteViewImpl.class);
                            recorrente.preencherView(despesa);
                            recorrente.setVisible(true);
                        }
                    }
                }
            }

        });
        
        
        

    }
    
    public JTextField getFieldDataInicio(){
        return fieldDataInicio;
    }
    
    public JTextField getFieldDataFim(){ 
        return fieldDataFim;
    }
    
    public JTextField getFieldDespesa(){
        return fieldDespesa;
    }
    
    public JCheckBox getCheckboxPagas(){
        return checkboxPagas;
    }
    
    public JTable getTableMovimentos(){
        return tableMovimentos;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMovimentos = new javax.swing.JTable();
        panelToolBar1 = new javax.swing.JPanel();
        btnNovo1 = new javax.swing.JButton();
        btnEditar1 = new javax.swing.JButton();
        btnSalvar1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        fieldDespesa = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        fieldDataFim = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fieldDataInicio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        checkboxPagas = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pagamentos");
        setSize(new java.awt.Dimension(1200, 800));
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

        panelTabela.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tableMovimentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Data Vencimento", "Despesa", "Parcela", "Valor", "F. Pagamento", "Data Pagamento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableMovimentos);
        if (tableMovimentos.getColumnModel().getColumnCount() > 0) {
            tableMovimentos.getColumnModel().getColumn(0).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(0).setPreferredWidth(40);
            tableMovimentos.getColumnModel().getColumn(1).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(2).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(3).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(4).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(5).setResizable(false);
            tableMovimentos.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout panelTabelaLayout = new javax.swing.GroupLayout(panelTabela);
        panelTabela.setLayout(panelTabelaLayout);
        panelTabelaLayout.setHorizontalGroup(
            panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelTabelaLayout.setVerticalGroup(
            panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelToolBar1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnNovo1.setBackground(new java.awt.Color(242, 242, 242));
        btnNovo1.setText("Nova Despesa Avulsa");
        btnNovo1.setToolTipText("Novo");
        btnNovo1.setBorder(null);
        btnNovo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovo1ActionPerformed(evt);
            }
        });

        btnEditar1.setBackground(new java.awt.Color(242, 242, 242));
        btnEditar1.setText("Nova Despesa Recorrente");
        btnEditar1.setToolTipText("Editar");
        btnEditar1.setBorder(null);
        btnEditar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditar1ActionPerformed(evt);
            }
        });

        btnSalvar1.setBackground(new java.awt.Color(242, 242, 242));
        btnSalvar1.setText("Gerar Relatório");
        btnSalvar1.setToolTipText("");
        btnSalvar1.setBorder(null);
        btnSalvar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelToolBar1Layout = new javax.swing.GroupLayout(panelToolBar1);
        panelToolBar1.setLayout(panelToolBar1Layout);
        panelToolBar1Layout.setHorizontalGroup(
            panelToolBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToolBar1Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(btnEditar1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalvar1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(614, Short.MAX_VALUE))
            .addGroup(panelToolBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelToolBar1Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(btnNovo1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(953, Short.MAX_VALUE)))
        );
        panelToolBar1Layout.setVerticalGroup(
            panelToolBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToolBar1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelToolBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditar1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnSalvar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panelToolBar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelToolBar1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnNovo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jLabel4.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel4.setText("Despesa");

        fieldDespesa.setName("fieldDespesas"); // NOI18N
        fieldDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDespesaActionPerformed(evt);
            }
        });

        jButton1.setText("Pesquisar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fieldDataFim.setName("fieldDataFim"); // NOI18N
        fieldDataFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDataFimActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel3.setText("Fim");

        fieldDataInicio.setName("fieldDataInicio"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        jLabel2.setText("Início");

        checkboxPagas.setText("Pagas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTabela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(fieldDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fieldDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkboxPagas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel4))
                .addContainerGap(426, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(fieldDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxPagas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fieldDataFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDataFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDataFimActionPerformed

    private void btnNovo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovo1ActionPerformed
        var view = context.getBean(DespesaAvulsaViewImpl.class);
        view.setVisible(true);
        
    }//GEN-LAST:event_btnNovo1ActionPerformed

    private void btnSalvar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvar1ActionPerformed

    }//GEN-LAST:event_btnSalvar1ActionPerformed

    private void fieldDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDespesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDespesaActionPerformed

    private void btnEditar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditar1ActionPerformed

        var view = context.getBean(DespesaRecorrenteViewImpl.class);
        view.setVisible(true);

    }//GEN-LAST:event_btnEditar1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
         controller.inicializarTabela();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        controller.inicializarTabela();
    }//GEN-LAST:event_formWindowActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar1;
    private javax.swing.JButton btnNovo1;
    private javax.swing.JButton btnSalvar1;
    private javax.swing.JCheckBox checkboxPagas;
    private javax.swing.JTextField fieldDataFim;
    private javax.swing.JTextField fieldDataInicio;
    private javax.swing.JTextField fieldDespesa;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelTabela;
    private javax.swing.JPanel panelToolBar1;
    private javax.swing.JTable tableMovimentos;
    // End of variables declaration//GEN-END:variables
}
