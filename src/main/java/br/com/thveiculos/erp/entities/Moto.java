package br.com.thveiculos.erp.entities;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("moto")
public class Moto extends VeiculoAbstrato{


}
