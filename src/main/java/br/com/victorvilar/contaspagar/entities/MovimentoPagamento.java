package br.com.victorvilar.contaspagar.entities;

import jakarta.persistence.Column;
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
import java.util.Objects;

@Entity
@Table(name="movimento_pagamento")
public class MovimentoPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenteParcela;
    @Column(nullable = false)
    private LocalDate dataVencimento;
    @Column(nullable = false)
    private BigDecimal valorPagamento;
    private LocalDate dataPagamento;
    private BigDecimal valorPago;
    private String observacao;
    @Column(unique=true)
    private String integridade;

    @ManyToOne
    @JoinColumn(name = "despesa_id")
    private DespesaAbstrata despesa;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", foreignKey = @ForeignKey(name = "forma_pagamento_fk", foreignKeyDefinition = "FOREIGN KEY (forma_pagamento_id) REFERENCES formas_pagamento(id) ON DELETE SET NULL"))
    private FormaPagamento formaPagamento;

    public MovimentoPagamento() {

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

    public void setObservacao(String obs) {
        this.observacao = obs;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setDespesa(DespesaAbstrata despesa) {
        this.despesa = despesa;

    }

    public DespesaAbstrata getDespesa() {
        return despesa;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public String getIntegridade() {
        return integridade;
    }

    public void setIntegridade(String integridade) {
        this.integridade = integridade;
    }
    
    

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoPagamento that = (MovimentoPagamento) o;
        if(that.id == null) return false;
        return Objects.equals(id, that.id);
    }
}
