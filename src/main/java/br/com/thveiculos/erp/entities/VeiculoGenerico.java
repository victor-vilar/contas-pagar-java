package br.com.thveiculos.erp.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.OneToMany;

public class VeiculoGenerico implements Veiculo, Serializable{

	private String codigoFipe;
	private String nome;
	private String marca;
	private String ano;
	private String tipoCombustivel;
	private BigInteger preco;
	private String placa;
	private String chassi;
	private String tipo;
	
	//@OneToMany
	private List<Manutencao> manutencoes = new ArrayList<>();
	
	public String getCodigoFipe() {
		return codigoFipe;
	}
	public String getNome() {
		return nome;
	}
	public String getMarca() {
		return marca;
	}
	public String getAno() {
		return ano;
	}
	public String getTipoCombustivel() {
		return tipoCombustivel;
	}
	public BigInteger getPreco() {
		return preco;
	}
	public String getPlaca() {
		return placa;
	}
	public String getChassi() {
		return chassi;
	}
	public String getTipo() {
		return tipo;
	}

}
	
	
	