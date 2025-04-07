package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.thveiculos.erp.enums.despesas.Periodo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "despesas_recorrentes")
public class DespesaRecorrente extends DespesaAbstrata implements Serializable {

    @Column(nullable = false)
    private Periodo periocidade;
    @Column(nullable = false)
    private LocalDate dataInicio;
    private LocalDate dataFim;
    @Column(nullable = false)
    private Integer dataPagamento;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_pradrão")
    private FormaPagamento formaPagamentoPadrao;

    public DespesaRecorrente() {
        super();
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

    public FormaPagamento getFormaPagamentoPadrao() {
        return formaPagamentoPadrao;
    }

    public void setFormaPagamentoPadrao(FormaPagamento formaPagamentoPadrao) {
        this.formaPagamentoPadrao = formaPagamentoPadrao;
    }

}
