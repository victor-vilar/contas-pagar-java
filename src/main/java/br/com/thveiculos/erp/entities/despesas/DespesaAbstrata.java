package br.com.thveiculos.erp.entities.despesas;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class DespesaAbstrata implements Despesa{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String nomeFornecedor;
	@Column(nullable=false)
	private String descricao;
	private boolean quitado = false;
	private BigDecimal valorTotal;
	
	@ManyToOne
	@JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name="categoria_fk",foreignKeyDefinition="FOREIGN KEY (categoria_id) REFERENCES categorias_despesas(id) ON DELETE SET NULL"))
	private CategoriaDespesa categoria;
	
	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id", foreignKey = @ForeignKey(name="forma_pagamento_fk", foreignKeyDefinition = "FOREIGN KEY (forma_pagamento_id) REFERENCES formas_pagamento(id) ON DELETE SET NULL"))
	private FormaPagamento formaPagamento;
	
	public Long getId() {
		return id;
	}
	
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
	public boolean isQuitado() {
		return this.quitado;
	}
	
	@Override
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setCategoria(CategoriaDespesa categoria) {
		this.categoria = categoria;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}


	
	
	
	
}
