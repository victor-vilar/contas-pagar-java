package br.com.thveiculos.erp.entities.veiculos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


/** 
 * Classe para reprensentar um caminhao e suas caracteristicas
 * */
@Entity
@DiscriminatorValue("caminhao")
public class Caminhao extends VeiculoAbstrato {

}
