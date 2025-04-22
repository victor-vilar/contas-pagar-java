package br.com.victorvilar.contaspagar.entities;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.victorvilar.contaspagar.enums.Periodo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "despesas_recorrentes")
public class DespesaRecorrente extends DespesaAbstrata implements Serializable {

    public final String tipo = "RECORRENTE";
    
    public String getTipo() {
        return tipo;
    }

    @Column(nullable = false)
    private Periodo periocidade;
    @Column(nullable = false)
    private LocalDate dataInicio;
    private LocalDate dataFim;
    @Column(nullable = false)
    private Integer diaPagamento;
    
    private Integer mesPagamento;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_padrao")
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
        return diaPagamento;
    }

    public void setDataPagamento(Integer dataPagamento) {
        this.diaPagamento = dataPagamento;
    }

    public FormaPagamento getFormaPagamentoPadrao() {
        return formaPagamentoPadrao;
    }

    public void setFormaPagamentoPadrao(FormaPagamento formaPagamentoPadrao) {
        this.formaPagamentoPadrao = formaPagamentoPadrao;
    }

    public Integer getDiaPagamento() {
        return diaPagamento;
    }

    public void setDiaPagamento(Integer diaPagamento) {
        this.diaPagamento = diaPagamento;
    }
    
    
    

    public Integer getMesPagamento() {
        return mesPagamento;
    }

    public void setMesPagamento(Integer mesPagamento) {
        this.mesPagamento = mesPagamento;
    }
    
    

}
