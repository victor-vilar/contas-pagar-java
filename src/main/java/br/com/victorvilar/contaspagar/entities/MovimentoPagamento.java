package br.com.victorvilar.contaspagar.entities;

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
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valorPagamento;
    private BigDecimal valorPago;
    private String observacao;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if(id == null){
            return false;
        }
        
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovimentoPagamento other = (MovimentoPagamento) obj;
        return Objects.equals(this.id, other.id);
    }

        
}
