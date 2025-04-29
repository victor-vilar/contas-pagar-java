package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DespesaControllerHelperTest {

    @InjectMocks
    private DespesaControllerHelper helper;

    FormaPagamento fp1;
    FormaPagamento fp2;

    DefaultTableModel model;
    List<MovimentoPagamento> movimentos = new ArrayList<>();
    MovimentoPagamento mp1;
    MovimentoPagamento mp2;
    MovimentoPagamento mp3;


    @BeforeEach
    public void setUp() {

        movimentos.clear();
        mp1 = new MovimentoPagamento();
        mp2 = new MovimentoPagamento();
        mp3 = new MovimentoPagamento();

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

        fp1 = new FormaPagamento();
        fp1.setForma("BOLETO BANCÁRIO");
        fp2 = new FormaPagamento();
        fp2.setForma("CARTÃO");

        model = new DefaultTableModel(new Object[][]{},
                new String[]{
                        "Código", "Nº Parcela", "Data Vencimento", "Valor R$", "Data Pagamento", "F. Pagamento", "Observação"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class,java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, true, true, true, true, true, true
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

        Assertions.assertThrows(QuantidadeDeParcelasException.class, ()
                -> helper.gerarMovimentos("ANUAL", 0, "31/03/2025", "2000", fp1));
    }

    @Test
    @DisplayName("Deve lançar um Quantidade De Parcelas Exception se a quantidade de parcelas for negativa")
    public void DeveLancarExceptionSeQuantidadeDeParcelasForNegativa() {

        Assertions.assertThrows(QuantidadeDeParcelasException.class, ()
                -> helper.gerarMovimentos("ANUAL", -10, "31/03/2025", "2000", fp1));
    }

    @Test
    @DisplayName("Se for passada o dia 31 de qualquer mes, a data deve ser reconfigurada para o dia 30")
    public void DeveReconfigurarDataParaDiaTrinta() {

        List<MovimentoPagamento> result = helper.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));

    }

    @Test
    @DisplayName("Deve gerar a parcela unica se a quantidade de parcelas for igual a 1 independente do perído passado")
    public void DeveGerarParcelasUnicasSeAQuantidadeForIgualAUm() {


        List<MovimentoPagamento> result = helper.gerarMovimentos("ANUAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));

        result = helper.gerarMovimentos("MENSAL", 1, "31/03/2025", "2000", fp1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp1.getName()));

        result = helper.gerarMovimentos("QUINZENAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));

        result = helper.gerarMovimentos("SEMANAL", 1, "31/03/2025", "2000", fp2);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReferenteParcela(), "UNICA");
        assertEquals(result.get(0).getDataVencimento(), LocalDate.of(2025, 03, 30));
        assertEquals(result.get(0).getValorPagamento(), new BigDecimal("2000"));
        assertTrue(result.get(0).getFormaPagamento().getName().equals(fp2.getName()));

    }

    @Test
    @DisplayName("Deve gerar parcelas anuais quando existem mais de uma parcela")
    public void DeveGerarQuantidadeDeParcelasAnuaisPassadas() {



        int qtdParcelas = 10;

        List<MovimentoPagamento> result
                = helper.gerarMovimentos("ANUAL",
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



        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = helper.gerarMovimentos("MENSAL",
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



        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = helper.gerarMovimentos("QUINZENAL",
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



        int qtdParcelas = 12;

        List<MovimentoPagamento> result
                = helper.gerarMovimentos("SEMANAL",
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

        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);

        model.addRow(new Object[]{null, "1/5", "0105", "2000", null,"",""});
        model.addRow(new Object[]{null, "1/5", "0106", "20000", null," ",""});
        model.addRow(new Object[]{null, "1/5", "0109", "21000", null," ",""});

        //when(formaPagmaento)

        helper.atualizarMovimentosTabela(movimentos, 0, model);
        helper.atualizarMovimentosTabela(movimentos, 2, model);

        //Objetos que devem ser alterados
        assertEquals(mp1.getDataVencimento(), LocalDate.of(2025, 5, 1));
        assertEquals(mp3.getDataVencimento(), LocalDate.of(2025, 9, 1));

        assertEquals(mp1.getDataPagamento(), null);
        assertEquals(mp3.getDataPagamento(), null);

        assertEquals(mp1.getValorPagamento(), ConversorMoeda.paraBigDecimal("2000"));
        assertEquals(mp3.getValorPagamento(), ConversorMoeda.paraBigDecimal("21000"));
        //

        //Objeto que não deve ser alterado pelo metodo
        assertEquals(mp2.getDataVencimento(), LocalDate.of(2025, 4, 1));
        assertEquals(mp2.getDataPagamento(), LocalDate.of(2025, 4, 1));
        assertEquals(mp2.getValorPagamento(), ConversorMoeda.paraBigDecimal("1000"));

    }

    @Test
    @DisplayName("Deve remover os movimentos das linhas selecionandas na view")
    public void removerMovimentoLinhasTabela() {



        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);

        mp1.setReferenteParcela("1/3");
        mp2.setReferenteParcela("2/3");
        mp3.setReferenteParcela("3/3");

        int[] linhas = {0, 2};

        helper.deletarMovimentosTabela(movimentos, linhas);

        assertEquals(movimentos.size(), 1);
        assertEquals(movimentos.get(0).getReferenteParcela(), "UNICA");

        movimentos.clear();

        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);

        mp1.setReferenteParcela("1/3");
        mp2.setReferenteParcela("2/3");
        mp3.setReferenteParcela("3/3");

        int[] outrasLinhas = {1};

        helper.deletarMovimentosTabela(movimentos, outrasLinhas);

        assertEquals(movimentos.size(), 2);
        assertEquals(movimentos.get(0).getReferenteParcela(), "1/2");
        assertEquals(movimentos.get(1).getReferenteParcela(), "2/2");

    }



}