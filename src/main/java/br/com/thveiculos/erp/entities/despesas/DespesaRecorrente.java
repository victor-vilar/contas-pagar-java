package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.thveiculos.erp.enums.despesas.Periodo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="despesas_recorrentes")
public class DespesaRecorrente extends DespesaAbstrata implements Serializable {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private Periodo periocidade;
	@Column(nullable=false)
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private Integer dataPagamento;
	
	
	public DespesaRecorrente() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Periodo getPeriocidade() {
		return periocidade;
	}
	public void setPeriocidade(Periodo periocidade) {
		this.periocidade = periocidade;
	}
	public LocalDate getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDate getDataFim() {
		return dataFim;
	}
	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}
	public Integer getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Integer dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	

	


}
