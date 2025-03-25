package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="despesas_parcelas")
public class Parcela implements Serializable {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String numero;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valorPagamento;
	
	
	public Parcela () {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public LocalDate getDataPagamento() {
		return dataPagamento;
	}


	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	public BigDecimal getValorPagamento() {
		return valorPagamento;
	}


	public void setValorPagamento(BigDecimal valorPagamento) {
		this.valorPagamento = valorPagamento;
	}


	public LocalDate getDataVencimento() {
		return dataVencimento;
	}


	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	
	
	

}
