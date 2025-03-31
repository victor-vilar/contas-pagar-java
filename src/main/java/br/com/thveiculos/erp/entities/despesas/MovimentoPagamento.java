package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="movimento_pagamengo")
public class MovimentoPagamento implements Serializable {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String referenteParcela;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valorPagamento;
	
	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id", foreignKey = @ForeignKey(name="forma_pagamento_fk", foreignKeyDefinition = "FOREIGN KEY (forma_pagamento_id) REFERENCES formas_pagamento(id) ON DELETE SET NULL"))
	private FormaPagamento formaPagamento;
	
	
	public MovimentoPagamento () {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getReferenteParcela() {
		return referenteParcela;
	}


	public void setReferenteParcela(String referenteParcela) {
		this.referenteParcela = referenteParcela;
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

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}


	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	
	
	

}
