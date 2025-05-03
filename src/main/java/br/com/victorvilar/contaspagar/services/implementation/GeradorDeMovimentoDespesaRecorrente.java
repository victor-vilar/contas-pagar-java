/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que sera usa para gerar os movimentos das despesas recorrentes.
 * @author victor
 */
@Component
public class GeradorDeMovimentoDespesaRecorrente {
    
    private final MovimentoPagamentoRepository repository;
    
    @Autowired
    public GeradorDeMovimentoDespesaRecorrente(MovimentoPagamentoRepository repository){
        this.repository = repository;
    }
    
}
