/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.enums;

/**
 * Enum para identificar o tipo de despesa
 * @author victor
 */
public enum DespesaTipo {

    DESPESA_RECORRENTE("Despesa Recorrente"),
    DESPESA_AVULSA("Despesa Avulsa");
    
    private final String tipo;
    
    DespesaTipo(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo;
    }
}
