package br.com.victorvilar.contaspagar.entities;

import java.io.Serializable;

import br.com.victorvilar.contaspagar.entities.interfaces.SimpleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categorias_despesas")
public class CategoriaDespesa implements Serializable, SimpleEntity{

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true, nullable = false)
	private String categoria;
	
	public CategoriaDespesa() {
		
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public String getName() {
		return this.categoria;
	}
	
	

}
