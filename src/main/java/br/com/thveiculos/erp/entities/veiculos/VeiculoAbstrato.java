package br.com.thveiculos.erp.entities.veiculos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * Classe que representa um veículo, todas as suas subclasses irão ser salvas na mesma tabela
 *  */
@Entity
@Table(name="veiculos")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType= DiscriminatorType.STRING)
public abstract class VeiculoAbstrato implements Veiculo, Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String codigoFipe;
	private String nome;
	private String marca;
	private String ano;
	private String tipoCombustivel;
	private BigInteger preco;
	private String placa;
	private String chassi;
	private String tipo;
	
	@OneToMany
	private List<Manutencao> manutencoes = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
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
	
	public List<Manutencao> getManutencoes(){
		return this.manutencoes;
	}
	
	public void setCodigoFipe(String codigoFipe) {
		this.codigoFipe = codigoFipe;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public void setTipoCombustivel(String tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}
	public void setPreco(BigInteger preco) {
		this.preco = preco;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setManutencao(List<Manutencao> manutencoes) {
		this.manutencoes = manutencoes;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void addManutencao(Manutencao manutencao) {
		
		if(!this.manutencoes.contains(manutencao)) {
			manutencoes.add(manutencao);
		}
	}
	
	

}
	
	
	