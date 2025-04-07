/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.views.interfaces;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author victor
 */
public interface DespesaView {
   
   public JTextField getFieldId();
   public List<java.awt.Component> getAllComponentes();
   public List<JTextComponent> getTextFields();
   public List<JComboBox<String>> getComboBoxes();
   public List<JSpinner> getAllSpinners();
    
}
