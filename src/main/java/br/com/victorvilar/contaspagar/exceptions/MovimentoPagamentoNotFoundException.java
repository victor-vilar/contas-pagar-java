/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.exceptions;

/**
 *
 * @author victor
 */
public class MovimentoPagamentoNotFoundException extends RuntimeException {
    
    public MovimentoPagamentoNotFoundException(String msg){
        super(msg);
    }
    
}
