/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.thveiculos.erp.services.despesas.implementation;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.exceptions.despesas.QuantidadeDeParcelasException;
import br.com.thveiculos.erp.util.ConversorData;
import br.com.thveiculos.erp.util.ConversorMoeda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author victor
 */
public class MovimentoPagamentoServiceTest {

    FormaPagamento fp1;
    FormaPagamento fp2;

    DefaultTableModel model;
    List<MovimentoPagamento> movimentos = new ArrayList<>();

    public MovimentoPagamentoServiceTest() {
    }

    @BeforeEach
    public void setUp() {

        fp1 = new FormaPagamento();
        fp1.setForma("BOLETO BANCÁRIO");
        fp2 = new FormaPagamento();
        fp2.setForma("CARTÃO");

        model = new DefaultTableModel(new Object[][]{},
                new String[]{
                    "Código", "Nº Parcela", "Data Vencimento", "Valor R$", "Data Pagamento", "F. Pagamento"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

    }

    @Test
    @DisplayName("Deve lançar um Quantidade De Parcelas Exception se a quantidade de parcelas for 0")
    public void DeveLancarExceptionSeParcelasForZero() {
        MovimentoPagamentoService instance = new MovimentoPagamentoService();
        Assertions.assertThrows(QuantidadeDeParcelasException.class, ()
                -> instance.gerarMovimentos("ANUAL", 0, "31/03/2025", "2000", fp1));
    }

    @Test
    @DisplayName("Deve lançar um Quantidade De Parcelas Exception se a quantidade de parcelas for negativa")
    public void DeveLancarExceptionSeQuantidadeDeParcelasForNegativa() {
        MovimentoPagamentoService instance = new MovimentoPagamentoService();
        Assertions.assertThrows(QuantidadeDeParcelasException.class, ()
                -> instance.gerarMovimentos("ANUAL", -10, "31/03/2025", "2000", fp1));
    }

    @Test
    @DisplayName("Se for passada o dia 31 de qualquer mes, a data deve ser reconfigurada para o dia 30")
    public void DeveReconfigurarDataParaDiaTrinta() {
        MovimentoPagamentoService instance = new MovimentoPagamentoService();
        List<MovimentoPagamento> result = instance.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));

    }

    @Test
    @DisplayName("Deve gerar a parcela unica se a quantidade de parcelas for igual a 1 independente do perído passado")
    public void DeveGerarParcelasUnicasSeAQuantidadeForIgualAUm() {

        MovimentoPagamentoService instance = new MovimentoPagamentoService();
        List<MovimentoPagamento> result = instance.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));

        result = instance.gerarMovimentos("MENSAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));

        result = instance.gerarMovimentos("QUINZENAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));

        result = instance.gerarMovimentos("SEMANAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));

    }

    @Test
    @DisplayName("Deve gerar parcelas anuais quando existem mais de uma parcela")
    public void DeveGerarQuantidadeDeParcelasAnuaisPassadas() {

        MovimentoPagamentoService instance = new MovimentoPagamentoService();

        int qtdParcelas = 10;

        List<MovimentoPagamento> result
                = instance.gerarMovimentos("ANUAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp1);

        assertEquals(result.size(), 10);

        for (int i = 0; i < result.size(); i++) {

            assertEquals(result.get(i).getReferenteParcela(), i + 1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), LocalDate.of(2025, 03, 30).plusYears(i));
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        }

    }

    @Test
    @DisplayName("Deve gerar parcelas mensais quando existem mais de uma parcela"
            + "e o periodo for mensal")
    public void DeveGerarQuantidadeDeParcelasMensais() {

        MovimentoPagamentoService instance = new MovimentoPagamentoService();

        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = instance.gerarMovimentos("MENSAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp1);

        assertEquals(result.size(), qtdParcelas);

        for (int i = 0; i < result.size(); i++) {

            assertEquals(result.get(i).getReferenteParcela(), i + 1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), LocalDate.of(2025, 03, 30).plusMonths(i));
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));
        }

    }

    @Test
    @DisplayName("Deve gerar parcelas mensais quando existem mais de uma parcela"
            + "e o periodo for quinzenal")
    public void DeveGerarQuantidadeDeParcelasQuinzenais() {

        MovimentoPagamentoService instance = new MovimentoPagamentoService();

        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = instance.gerarMovimentos("QUINZENAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp2);

        assertEquals(result.size(), qtdParcelas);
        LocalDate dt = LocalDate.parse("2025-03-30");

        for (int i = 0; i < result.size(); i++) {

            assertEquals(result.get(i).getReferenteParcela(), i + 1 + "/" + qtdParcelas);
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

        MovimentoPagamentoService instance = new MovimentoPagamentoService();

        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = instance.gerarMovimentos("SEMANAL",
                        qtdParcelas, "31/03/2025",
                        "2000", fp2);

        assertEquals(result.size(), qtdParcelas);
        LocalDate dt = LocalDate.parse("2025-03-30");

        for (int i = 0; i < result.size(); i++) {

            assertEquals(result.get(i).getReferenteParcela(), i + 1 + "/" + qtdParcelas);
            assertEquals(result.get(i).getDataVencimento(), dt);
            dt = dt.plusWeeks(1);
            assertEquals(result.get(i).getValorPagamento(), new BigDecimal("2000"));
            assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));
        }

    }

    
    @Test
    @DisplayName("Deve atualizar os valores da lista de movimento de acordo com os valores que se encontram no table model")
    public void deveAtualizarOsMovimentosDeAcordoComOsValoresNoTableModel() {

        MovimentoPagamento mp1 = new MovimentoPagamento();
        MovimentoPagamento mp2 = new MovimentoPagamento();
        MovimentoPagamento mp3 = new MovimentoPagamento();
        
        mp1.setId(1L);
        mp1.setReferenteParcela("1/5");
        mp1.setDataVencimento(ConversorData.paraData("0104"));
        mp1.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp1.setDataPagamento(ConversorData.paraData("0104"));
        
        mp2.setId(2L);
        mp2.setReferenteParcela("1/5");
        mp2.setDataVencimento(ConversorData.paraData("0104"));
        mp2.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp2.setDataPagamento(ConversorData.paraData("0104"));
        
        mp3.setId(2L);
        mp3.setReferenteParcela("1/5");
        mp3.setDataVencimento(ConversorData.paraData("0104"));
        mp3.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp3.setDataPagamento(ConversorData.paraData("0104"));
        
        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);
        
        
        model.addRow(new Object[]{null,"1/5","0105","2000",null});
        model.addRow(new Object[]{null,"1/5","0106","20000",null});
        model.addRow(new Object[]{null,"1/5","0109","21000",null});
       
        MovimentoPagamentoService gm = new MovimentoPagamentoService();
        gm.atualizarMovimentos(movimentos, 0, model);
        gm.atualizarMovimentos(movimentos, 2, model);
        
        
        //Objetos que devem ser alterados
        assertEquals(mp1.getDataVencimento(),LocalDate.of(2025, 5, 1));
        assertEquals(mp3.getDataVencimento(),LocalDate.of(2025, 9, 1));
        
        assertEquals(mp1.getDataPagamento(),null);
        assertEquals(mp3.getDataPagamento(),null);
        
        
        assertEquals(mp1.getValorPagamento(),ConversorMoeda.paraBigDecimal("2000"));
        assertEquals(mp3.getValorPagamento(),ConversorMoeda.paraBigDecimal("21000"));
        //
        
        //Objeto que não deve ser alterado pelo metodo
        assertEquals(mp2.getDataVencimento(),LocalDate.of(2025, 4, 1));
        assertEquals(mp2.getDataPagamento(),LocalDate.of(2025, 4, 1));
        assertEquals(mp2.getValorPagamento(),ConversorMoeda.paraBigDecimal("1000"));
       
    }
    
    @Test
    @DisplayName("Deve remover os movimentos das linhas selecionandas na view")
    public void removerMovimentoLinhasTabela(){
    }

}
