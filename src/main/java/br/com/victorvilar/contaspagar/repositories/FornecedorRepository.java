/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.repositories;

import br.com.victorvilar.contaspagar.entities.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author victor
 */
public interface FornecedorRepository extends JpaRepository<Fornecedor,Long> {
    
}
