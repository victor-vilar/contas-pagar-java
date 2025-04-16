/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.views.interfaces;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author victor
 */
public interface DespesaView {
   
   public List<java.awt.Component> getAllComponentes();
   public List<JTextComponent> getTextFields();
   public List<JComboBox<String>> getComboBoxes();

   
   public JTextField getFieldId();
   public JTextField getFieldDescricao();
   public JTextArea getAreaDescricao();
   public JTextField getFieldValor();
   public JTextField getFieldCodFornecedor();
   
   
   public JComboBox<String> getComboCategoria();
   public JComboBox<String> getComboParcelamento();
   public JComboBox<String> getComboFormaPagamento();
   public JComboBox<String> getComboFormaPagamentoTabela();
   
   
  
   public JTable getTableParcelas();
   public void preencherView(DespesaAbstrata despesa);
}
