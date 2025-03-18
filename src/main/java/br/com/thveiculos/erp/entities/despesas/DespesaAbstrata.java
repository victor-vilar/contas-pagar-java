package br.com.thveiculos.erp.entities.despesas;

import java.math.BigDecimal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public abstract class DespesaAbstrata implements Despesa{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nomeFornecedor;
	private String descricao;
	private boolean quitado;
	private BigDecimal valorTotal;
	
	@ManyToOne
	private CategoriaDespesa categoria;
	
	@ManyToOne
	private FormaPagamento formaPagamento;
	
	@Override
	public CategoriaDespesa getCategoria() {
		return this.categoria;
	}
	
	@Override
	public FormaPagamento getFormaPagamento() {
		return this.formaPagamento;
	}
	
	@Override
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	
	@Override
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	
}
