/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.enums;

/**
 *
 * @author victor
 */
public enum TipoDeExport {
    
    PDF("pdf"),
    CSV("csv");
    
    private String tipo;
    
    private TipoDeExport(String tipo){
        this.tipo = tipo;
    }
    
    private String getTipo(){
        return tipo;
    }
    
}
