/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author victor
 */
@Entity
@Table(name="fornecedor")
public class Endereco implements Serializable{
 
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    
    @OneToOne
    @JoinColumn(name="fornecedor_id")
    private Fornecedor fornecedor;
    
    
    
    
    
}
