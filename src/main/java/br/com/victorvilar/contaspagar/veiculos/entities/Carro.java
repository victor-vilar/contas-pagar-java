package br.com.victorvilar.contaspagar.veiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** 
 * Classe para reprensentar um carro e suas caracteristicas
 * */
@Entity
@DiscriminatorValue("carro")
public class Carro extends VeiculoAbstrato{

	
}
