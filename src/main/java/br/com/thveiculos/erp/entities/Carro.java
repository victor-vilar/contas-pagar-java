package br.com.thveiculos.erp.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("carro")
public class Carro extends VeiculoGenerico{

	
}
