/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers;

/**
 * Interface com os metodos padrões de todos os controladores de aplicação.
 * @author victor
 */
public interface AppViewController<T> {
    
    void setView(T view);
    void novo();
    void salvar();
    void editar();
    void deletar();
    void limparCampos();
    
}
