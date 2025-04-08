/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.views.interfaces;

import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author victor
 */
public interface DespesaViewAvulsa extends DespesaView {
    
   JSpinner getSpinnerQuantidadeParcelas();
   JTextField getFieldNota();
   JTextField getFieldNotaEmissao();
   
    
}
