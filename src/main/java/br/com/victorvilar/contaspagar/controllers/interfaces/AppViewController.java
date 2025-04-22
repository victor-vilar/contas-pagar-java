/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers.interfaces;

/**
 * Interface com os metodos padrões de todos os controladores de aplicação.
 * @author victor
 */
public interface AppViewController<T> {
    
    void setView(T view);
    void limparCampos();
    
}
