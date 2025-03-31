/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.thveiculos.erp.services.despesas.implementation;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.exceptions.despesas.QuantidadeDeParcelasException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author victor
 */
public class GeradorMovimentosTest {
    
    
    FormaPagamento fp1 ;
    FormaPagamento fp2 ;
  
  
    
    public GeradorMovimentosTest() {
    }
    
    @BeforeEach
    public void setUp(){
         fp1 = new FormaPagamento();
         fp1.setForma("BOLETO BANCÁRIO");
         fp2 = new FormaPagamento();
         fp2.setForma("CARTÃO");
    }
    

    @Test
    @DisplayName("Deve lançar um Quantidade De Parcelas Exception se a quantidade de parcelas for 0")
    public void DeveLancarExceptionSeParcelasForZero(){
        GeradorMovimentos instance = new GeradorMovimentos();
        Assertions.assertThrows(QuantidadeDeParcelasException.class, () ->
                instance.gerarMovimentos("ANUAL", 0, "31/03/2025", "2000", fp1));
    }
    
    @Test
    @DisplayName("Deve lançar um Quantidade De Parcelas Exception se a quantidade de parcelas for negativa")
    public void DeveLancarExceptionSeQuantidadeDeParcelasForNegativa(){
        GeradorMovimentos instance = new GeradorMovimentos();
        Assertions.assertThrows(QuantidadeDeParcelasException.class, () ->
                instance.gerarMovimentos("ANUAL", -10, "31/03/2025", "2000", fp1));
    }
    
    
    @Test
    @DisplayName("Se for passada o dia 31 de qualquer mes, a data deve ser reconfigurada para o dia 30")
    public void DeveReconfigurarDataParaDiaTrinta(){
        GeradorMovimentos instance = new GeradorMovimentos();            
        List<MovimentoPagamento> result = instance.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
    
    }


    @Test
    @DisplayName("Deve gerar a parcela unica se a quantidade de parcelas for igual a 1 independente do perído passado")
    public void DeveGerarParcelasUnicasSeAQuantidadeForIgualAUm() {

        GeradorMovimentos instance = new GeradorMovimentos();
        List<MovimentoPagamento> result = instance.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025,03,30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        
        result = instance.gerarMovimentos("MENSAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025,03,30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        
        result = instance.gerarMovimentos("QUINZENAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025,03,30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));
        
        result = instance.gerarMovimentos("SEMANAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025,03,30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));
     
    }
    

    @Test
    @DisplayName("Deve gerar parcelas anuais quando existem mais de uma parcela")
    public void DeveGerarQuantidadeDeParcelasAnuaisPassadas() {

        GeradorMovimentos instance = new GeradorMovimentos();
        
        int qtdParcelas = 10;
        
        List<MovimentoPagamento> result =
                instance.gerarMovimentos("ANUAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp1);
        
        assertEquals(result.size(), 10);
        
        for(int i = 0; i < result.size();i++){
        
            
            assertEquals(result.get(i).getReferenteParcela(), i+1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), LocalDate.of(2025,03,30).plusYears(i));
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        }
        
     
     
    }
    
    @Test
    @DisplayName("Deve gerar parcelas mensais quando existem mais de uma parcela"
            + "e o periodo for mensal")
    public void DeveGerarQuantidadeDeParcelasMensais() {

        GeradorMovimentos instance = new GeradorMovimentos();
        
        int qtdParcelas = 12;
        
        List<MovimentoPagamento> result =
                instance.gerarMovimentos("MENSAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp1);
        
        assertEquals(result.size(), qtdParcelas);
        
        for(int i = 0; i < result.size();i++){
        
            
            assertEquals(result.get(i).getReferenteParcela(), i+1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), LocalDate.of(2025,03,30).plusMonths(i));
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        }
        
     
     
    }
    
    
   @Test
    @DisplayName("Deve gerar parcelas mensais quando existem mais de uma parcela"
            + "e o periodo for quinzenal")
    public void DeveGerarQuantidadeDeParcelasQuinzenais() {

        GeradorMovimentos instance = new GeradorMovimentos();
        
        int qtdParcelas = 12;
        
        List<MovimentoPagamento> result =
                instance.gerarMovimentos("QUINZENAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp2);
        
        assertEquals(result.size(), qtdParcelas);
        LocalDate dt = LocalDate.parse("2025-03-30");
        
        for(int i = 0; i < result.size();i++){
            
            
        
            
            assertEquals(result.get(i).getReferenteParcela(), i+1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), dt);
            dt = dt.plusWeeks(2);
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));
        }
        
     
     
    }

    
       @Test
    @DisplayName("Deve gerar parcelas mensais quando existem mais de uma parcela"
            + "e o periodo for semanal")
    public void DeveGerarQuantidadeDeParcelasSemanais() {

        GeradorMovimentos instance = new GeradorMovimentos();
        
        int qtdParcelas = 12;
        
        List<MovimentoPagamento> result =
                instance.gerarMovimentos("SEMANAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp2);
        
        assertEquals(result.size(), qtdParcelas);
        LocalDate dt = LocalDate.parse("2025-03-30");
        
        for(int i = 0; i < result.size();i++){
            
            
        
            
            assertEquals(result.get(i).getReferenteParcela(), i+1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), dt);
            dt = dt.plusWeeks(1);
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));
        }
        
     
     
    }
    
}
