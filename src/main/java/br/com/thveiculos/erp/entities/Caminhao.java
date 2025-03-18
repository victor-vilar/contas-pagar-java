package br.com.thveiculos.erp.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("caminhao")
public class Caminhao extends VeiculoAbstrato {

}
