package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.interfaces.Despesa;
import br.com.victorvilar.contaspagar.enums.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeradorDeDatasDespesasRecorrentesTest {

    @Spy
    GeradorDeDatasDespesasRecorrentes gerador;

    DespesaRecorrente despesa;

    @BeforeEach
    public void setUp(){
        despesa = new DespesaRecorrente();
       despesa.setId(1l);
       despesa.setNomeFornecedor("LIGTH");
       despesa.setDescricao("PAGAMENTO DE ENERGIA");
       despesa.setPeriocidade(Periodo.MENSAL);
       despesa.setDataInicio(LocalDate.now());
       despesa.setDataFim(LocalDate.of(9999,1,1));
       despesa.setDiaPagamento(15);
       despesa.setMesPagamento(12);
       despesa.setValorTotal(new BigDecimal(1000));


    }

    @Test
    @DisplayName("metodo criarDataVencimento")
    public void deveChamarMetodoGerarVencimentoAnual(){
        despesa.setPeriocidade(Periodo.ANUAL);
        gerador.criarDataVencimento(despesa);
        verify(gerador,times(1)).gerarVencimentoAnual(anyInt(),anyInt(),any());
    }
    @Test
    @DisplayName("metodo criarDataVencimento")
    public void deveChamarMetodoGerarVencimentoMensal(){
        despesa.setPeriocidade(Periodo.MENSAL);
        gerador.criarDataVencimento(despesa);
        verify(gerador,times(1)).gerarVencimentoMensal(anyInt(),any());
    }
    @Test
    @DisplayName("metodo criarDataVencimento")
    public void deveChamarMetodoGerarVencimentoQuinzenal(){
        despesa.setPeriocidade(Periodo.QUINZENAL);
        gerador.criarDataVencimento(despesa);
        verify(gerador,times(1)).gerarVencimentoQuinzenal(anyInt(),any());
    }
    @Test
    @DisplayName("metodo criarDataVencimento")
    public void deveChamarMetodoGerarVencimentoSemanal(){
        despesa.setPeriocidade(Periodo.SEMANAL);
        gerador.criarDataVencimento(despesa);
        verify(gerador,times(1)).gerarVencimentoSemanal(anyInt(),anyInt(),any());
    }

    @Test
    @DisplayName("metodo gerarVencimentoAnual")
    public void deveGerarUmaNovaDataParaUmPeriodoAnualSeOUltimoLancamentoNaoForNullo(){
        LocalDate dataGerada = gerador.gerarVencimentoAnual(30,1,LocalDate.of(2024,1,30));
        LocalDate dataEsperada = LocalDate.of(2025,1,30);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoAnual")
    public void deveGerarUmaNovaDataParaUmPeriodoAnualParaOMesmoAnoSeADataAindaNaoPassou(){
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,1,15));
        LocalDate dataGerada = gerador.gerarVencimentoAnual(30,1,null);
        LocalDate dataEsperada = LocalDate.of(2025,1,30);
        assertEquals(dataEsperada,dataGerada);

        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,12,29));
        dataGerada = gerador.gerarVencimentoAnual(30,12,null);
        dataEsperada = LocalDate.of(2025,12,30);
        assertEquals(dataEsperada,dataGerada);


    }

    @Test
    @DisplayName("metodo gerarVencimentoAnual")
    public void deveGerarUmaNovaDataDeUmPeriodoAnualParaOProximoAnoSeADataDeVencimentoJaPassouNoAnoAtual(){
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,1,15));
        LocalDate dataGerada = gerador.gerarVencimentoAnual(5,1,null);
        LocalDate dataEsperada = LocalDate.of(2026,1,5);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoAnual(1,1,null);
        dataEsperada = LocalDate.of(2026,1,1);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoMensal")
    public void deveGerarUmDataNoProximoMesLevandoEmContaOMesDoUltimoPagamento(){
        LocalDate dataGerada = gerador.gerarVencimentoMensal(1,LocalDate.of(2024,1,30));
        LocalDate dataEsperada = LocalDate.of(2024,2,29);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoMensal(29,LocalDate.of(2025,3,28));
        dataEsperada = LocalDate.of(2025,4,28);
        assertEquals(dataEsperada,dataGerada);

    }

    @Test
    @DisplayName("metodo gerarVencimentoMensal")
    public void deveGerarUmaDataParaOMesmoMesSeODiaDePagamentoNaoTiverPassado(){
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,1,1));
        LocalDate dataGerada = gerador.gerarVencimentoMensal(10,null);
        LocalDate dataEsperada = LocalDate.of(2025,1,10);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoMensal(29,null);
        dataEsperada = LocalDate.of(2025,1,29);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoMensal")
    public void deveGerarUmaDataParaOProximoMesSeODiaDePagamentoJaTiverPassado() {
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,1,15));
        LocalDate dataGerada = gerador.gerarVencimentoMensal(10,null);
        LocalDate dataEsperada = LocalDate.of(2025,2,10);
        assertEquals(dataEsperada,dataGerada);

        when(gerador.dataHoje()).thenReturn(LocalDate.of(2026,1,30));
        dataGerada = gerador.gerarVencimentoMensal(28,null);
        dataEsperada = LocalDate.of(2026,2,28);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVecimentoMensal")
    public void deveGerarVencimentoParaOOutroAnoQuandoDiaDePagamentoJaTiverPassadoEOMesForDezembro(){
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,12,15));
        LocalDate dataGerada = gerador.gerarVencimentoMensal(15,null);
        LocalDate dataEsperada = LocalDate.of(2026,1,15);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarVencimentoParaASegundaQuinzenalSeoUltimoLancamentoFoiNaPrimeiraQuinzena(){
        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,LocalDate.of(2025,1,2));
        LocalDate dataEsperada = LocalDate.of(2025,1,16);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(2,LocalDate.of(2025,1,14));
        dataEsperada = LocalDate.of(2025,1,28);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarOsMovimentosParaOProximoMesQuandoADataDoUltimoPagamentoForNSegundaQuinzenaMaiorQueDia14(){

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,LocalDate.of(2025,1,20));
        LocalDate dataEsperada = LocalDate.of(2025,2,2);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(2,LocalDate.of(2025,8,30));
        dataEsperada = LocalDate.of(2025,9,2);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarOsMovimentosParaOProximoMesQuandoADataDoUltimoPagamentoForNulaEODiaAtualEMaiorQue14(){
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025,1,18));

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,null);
        LocalDate dataEsperada = LocalDate.of(2025,2,2);
        assertEquals(dataEsperada,dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(2,null);
        dataEsperada = LocalDate.of(2025,2,2);
        assertEquals(dataEsperada,dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarOsMovimentosParaOProximoAnoQuandoOUltimoLancamentoForDepoisDoDia14eOMesForIgualADezembro() {
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025, 12, 18));

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2, LocalDate.of(2025,12,16));
        LocalDate dataEsperada = LocalDate.of(2026, 1, 2);
        assertEquals(dataEsperada, dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(12, LocalDate.of(2025,12,29));
        dataEsperada = LocalDate.of(2026, 1, 12);
        assertEquals(dataEsperada, dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarOsMovimentosParaOProximoAnoQuandoOUltimoLancamentoForNuloEODiaAtualEMaiorQue14EOMesForIgualADezembro() {
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025, 12, 18));

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,null);
        LocalDate dataEsperada = LocalDate.of(2026, 1, 2);
        assertEquals(dataEsperada, dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(12, null);
        dataEsperada = LocalDate.of(2026, 1, 12);
        assertEquals(dataEsperada, dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarOsDatasParaASegundaQuinzenaDoMesAutalQuandoODiaAtualEMaiorQueODiaProgramadoMasAindaEAntesDaSegundaQuinzena() {
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025, 1, 13));

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,null);
        LocalDate dataEsperada = LocalDate.of(2025, 1, 16);
        assertEquals(dataEsperada, dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(12, null);
        dataEsperada = LocalDate.of(2025, 1, 26);
        assertEquals(dataEsperada, dataGerada);
    }

    @Test
    @DisplayName("metodo gerarVencimentoQuinzenal")
    public void deveGerarADataParaAPrimeiraQuinzenaQuandoOUltimoPagamentoForNuloEADataAtualEMenorQueDataProgramada() {
        when(gerador.dataHoje()).thenReturn(LocalDate.of(2025, 1, 1));

        LocalDate dataGerada = gerador.gerarVencimentoQuinzenal(2,null);
        LocalDate dataEsperada = LocalDate.of(2025, 1, 2);
        assertEquals(dataEsperada, dataGerada);

        dataGerada = gerador.gerarVencimentoQuinzenal(12, null);
        dataEsperada = LocalDate.of(2025, 1, 12);
        assertEquals(dataEsperada, dataGerada);


    }
}