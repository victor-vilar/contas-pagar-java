/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.views.interfaces;

import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author victor
 */
public interface DespesaAvulsaView extends DespesaView {
    
   JSpinner getSpinnerQuantidadeParcelas();
   JTextField getFieldNota();
   JTextField getFieldNotaEmissao();
   public JTextField getFieldVencimento();
    JButton getBtnGerarParcelas();
   
    
}
