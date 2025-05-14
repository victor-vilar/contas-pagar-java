/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.views.interfaces;

import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author victor
 */
public interface DespesaRecorrenteView extends DespesaView {
    
    public JTextField getFieldDiaVencimento();
    public JTextField getFieldMesVencimento();
    public JCheckBox getAtivoBox();

    
}
