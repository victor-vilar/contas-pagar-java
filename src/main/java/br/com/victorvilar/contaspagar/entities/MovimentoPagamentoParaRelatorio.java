/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.entities;

import br.com.victorvilar.contaspagar.util.ConversorData;
import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author victor
 */
public class MovimentoPagamentoParaRelatorio {
    
    private Long id;
    private String referenteParcela;
    private String dataVencimento;
    private String dataPagamento;
    private BigDecimal valorPagamento;   
    private BigDecimal valorPago;
    private String observacao;
    private String integridade;
    private String despesa;
    private String formaPagamento;
    
    public MovimentoPagamentoParaRelatorio(MovimentoPagamento movimento){
        this.id = movimento.getId();
        this.referenteParcela = movimento.getReferenteParcela();
        this.dataVencimento = ConversorData.paraString(movimento.getDataVencimento());
        this.dataPagamento = ConversorData.paraString(movimento.getDataPagamento());
        this.valorPagamento = movimento.getValorPagamento();
        this.valorPago = movimento.getValorPago();
        this.observacao = movimento.getObservacao();
        this.integridade = movimento.getIntegridade();
        this.despesa = movimento.getDespesa().getNome();
        this.formaPagamento = movimento.getFormaPagamento().getForma();          
        
    }

    public Long getId() {
        return id;
    }

    public String getReferenteParcela() {
        return referenteParcela;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getIntegridade() {
        return integridade;
    }

    public String getDespesa() {
        return despesa;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    
    
    
    
}
