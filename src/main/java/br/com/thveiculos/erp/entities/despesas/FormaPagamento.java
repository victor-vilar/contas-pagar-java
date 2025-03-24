package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;

import br.com.thveiculos.erp.entities.interfaces.SimpleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="formas_pagamento")
public class FormaPagamento implements Serializable, SimpleEntity {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, nullable = false)
	private String forma;
	
	public FormaPagamento(){}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}
	
	@Override
	public String getName() {
		return this.forma;
	}
	
	
}
