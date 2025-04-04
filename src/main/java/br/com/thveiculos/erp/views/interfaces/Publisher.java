/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.views.interfaces;

/**
 * Interface para todos os publicadores, devem possuir uma lista de subscribers
 * para que possam ser notificados, se operaçoẽs de seu interesse.
 * @author victor
 */
public interface Publisher {
    
 public void adicionarSubscribers(Subscriber obj);
}
