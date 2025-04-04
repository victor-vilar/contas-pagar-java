/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.views.interfaces;

/**
 *  Subscriber para se inscrever em eventos para que possam receber atualizações.
 * @author victor
 */
public interface Subscriber {
    
    public void atualizar(String valor , String tipo);
    
}
