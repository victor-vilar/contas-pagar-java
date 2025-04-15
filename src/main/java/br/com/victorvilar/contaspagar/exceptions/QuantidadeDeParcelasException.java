/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.exceptions;

/**
 *
 * @author victor
 */
public class QuantidadeDeParcelasException extends RuntimeException{
    
    private static final String ERROR = "A quantidade de parcelas não pode ser zero ou negativa";
    
    public QuantidadeDeParcelasException(){
        super(ERROR);
    }
    
}
