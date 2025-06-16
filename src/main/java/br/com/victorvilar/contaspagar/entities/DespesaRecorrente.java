package br.com.victorvilar.contaspagar.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.victorvilar.contaspagar.enums.Periodo;
import jakarta.persistence.*;

@Entity
@Table(name = "despesas_recorrentes")
public class DespesaRecorrente extends DespesaAbstrata implements Serializable {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodo periocidade;
    private Boolean ativo;
    //Data programada para o lançamento do próximo {@link MovimentoPagamento} para essa DespesaRecorrente
    private LocalDate dataProximoLancamento;
    //Data de vencimento do ultimo {@link MovimentoPagamento} pertencente a essa DespesaRecorrente
    private LocalDate dataUltimoLancamento;
    @Column(nullable = false)
    private Integer diaPagamento;
    private Integer mesPagamento;
    

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_padrao")
    private FormaPagamento formaPagamentoPadrao;

    public DespesaRecorrente() {
        super();
        this.setTipo("RECORRENTE");
        
    }

    public Periodo getPeriocidade() {
        return periocidade;
    }

    public void setPeriocidade(Periodo periocidade) {
        this.periocidade = periocidade;
    }

    public Integer getDataPagamento() {
        return diaPagamento;
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

    @Override
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal){
        this.valorTotal = valorTotal;
    }
    

    public Integer getMesPagamento() {
        return mesPagamento;
    }

    public void setMesPagamento(Integer mesPagamento) {
        this.mesPagamento = mesPagamento;
    }

    public LocalDate getDataProximoLancamento() {
        return dataProximoLancamento;
    }

    public void setDataProximoLancamento(LocalDate dataProximoLancamento) {
        this.dataProximoLancamento = dataProximoLancamento;
    }

    public LocalDate getDataUltimoLancamento() {
        return dataUltimoLancamento;
    }

    public void setDataUltimoLancamento(LocalDate dataUltimoLancamento) {
        this.dataUltimoLancamento = dataUltimoLancamento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
