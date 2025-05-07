package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeradorDeMovimentoDespesaRecorrenteTest {

    @InjectMocks
    @Spy
    private GeradorDeMovimentoDespesaRecorrente gerador;

    @Mock
    private MovimentoPagamentoRepository movimentoRepository;

    @Mock
    private DespesaRepository despesaRepository;


    DespesaRecorrente dr1;
    DespesaRecorrente dr2;

    @BeforeEach
    public void setUp() {
        dr1 = new DespesaRecorrente();
        dr2 = new DespesaRecorrente();

        dr1.setId(1L);
        dr2.setId(2L);
        dr1.setDiaPagamento(1);
        dr2.setDiaPagamento(10);
        dr1.setPeriocidade(Periodo.MENSAL);
        dr2.setPeriocidade(Periodo.MENSAL);

    }

    @Test
    @DisplayName("Metodo criarMovimento")
    public void deveCriarUmMovimento() {
        MovimentoPagamento movimento1 = gerador.criarMovimento(dr1);
        assertEquals(movimento1.getDataVencimento(), LocalDate.of(2025, 6, 1));
        assertEquals(movimento1.getValorPagamento(), dr1.getValorTotal());
        assertEquals(movimento1.getIntegridade(), "1/2025-06-01");
        assertEquals(movimento1.getReferenteParcela(), "JUNHO DE 2025");

        MovimentoPagamento movimento2 = gerador.criarMovimento(dr2);
        assertEquals(movimento2.getDataVencimento(), LocalDate.of(2025, 5, 10));
        assertEquals(movimento2.getValorPagamento(), dr1.getValorTotal());
        assertEquals(movimento2.getIntegridade(), "2/2025-05-10");
        assertEquals(movimento2.getReferenteParcela(), "MAIO DE 2025");

    }

    @Test
    @DisplayName("metodo run")
    public void deveChamarMetodoFindDespesasRecorrentesWhereDataProximoLancamentoLowerThanNowDoDespesaRepository() {
        gerador.run();
        verify(despesaRepository, times(1)).findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(any(LocalDate.class));
    }

    @Test
    @DisplayName("metodo run")
    public void deveChamarMetodoRealizarLancamentosDeAcordoComAQuantidadeDeDespesaRecorrenteDaLista() {
        when(despesaRepository.findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(any(LocalDate.class))).thenReturn(List.of(dr1, dr2));
        gerador.run();
        verify(gerador, times(2)).realizarLancamentos(any(DespesaRecorrente.class));

    }

    @Test
    @DisplayName("Metodo gerarDescricaoReferencia")
    public void deveGerarUmReferenciaDeAcordoComPeriodoEDataPassada() {
        LocalDate data = LocalDate.of(2025, 1, 1);
        String referenciaAnual = gerador.gerarDescricaoReferencia(data, Periodo.ANUAL);
        String referenciaAnualEsperada = "ANUIDADE 2025";
        String referenciaMensal = gerador.gerarDescricaoReferencia(data, Periodo.MENSAL);
        String referenciaMensalEsperada = "JANEIRO DE 2025";
        String referencia1Quinzena = gerador.gerarDescricaoReferencia(data, Periodo.QUINZENAL);
        String referencia1QuinzenaEsperada = "1ª QUINZENA DE JANEIRO DE 2025";
        String referencia2Quinzena = gerador.gerarDescricaoReferencia(data.plusDays(15), Periodo.QUINZENAL);
        String referencia2QuinzenaEsperada = "2ª QUINZENA DE JANEIRO DE 2025";

        assertEquals(referenciaAnual, referenciaAnualEsperada);
        assertEquals(referenciaMensal, referenciaMensalEsperada);
        assertEquals(referencia1Quinzena, referencia1QuinzenaEsperada);
        assertEquals(referencia2Quinzena, referencia2QuinzenaEsperada);

    }

    @Test
    @DisplayName("metodo gerarIntegridade")
    public void deveGerarUmaInntegridadeComOIdDaDespesaEADataDeVencimentoDoMovimento() {
        String int1 = gerador.gerarIntegridade(1L, LocalDate.of(2025, 1, 1));
        String int1Esperada = "1/2025-01-01";
        String int2 = gerador.gerarIntegridade(50L, LocalDate.of(2025, 1, 1));
        String int2Esperada = "50/2025-01-01";
        String int3 = gerador.gerarIntegridade(50L, LocalDate.of(2025, 8, 26));
        String int3Esperada = "50/2025-08-26";
        assertEquals(int1Esperada, int1);
        assertEquals(int2Esperada, int2);
        assertEquals(int3Esperada, int3);


    }


    @Test
    @DisplayName("Metodo criarMovimento")
    public void deveChamarMetodoParaGerarDescricaoReferencia() {
        gerador.criarMovimento(dr1);
        verify(gerador, times(1)).gerarDescricaoReferencia(any(LocalDate.class), any(Periodo.class));

    }

    @Test
    @DisplayName("Metodo criarMovimento")
    public void deveChamarMetodoParaGerarIntegridade() {
        gerador.criarMovimento(dr1);
        verify(gerador, times(1)).gerarIntegridade(any(Long.class), any(LocalDate.class));

    }

    @Test
    @DisplayName("Metodo salvar")
    public void deveAdicionaroMovimentoDentroDaListaDeMovimentosDaDespesa() {
        MovimentoPagamento movimento1 = gerador.criarMovimento(dr1);
        gerador.salvar(dr1, movimento1);
        assertTrue(movimento1.getDespesa() == dr1);
        assertTrue(!dr1.getParcelas().isEmpty());
        assertEquals(dr1.getParcelas().get(0).getReferenteParcela(), movimento1.getReferenteParcela());
    }

    @Test
    @DisplayName("Metodo salvar")
    public void deveChamarMetodoParaSalvarADespesa() {
        MovimentoPagamento movimento1 = gerador.criarMovimento(dr1);
        gerador.salvar(dr1, movimento1);
        verify(despesaRepository,times(1)).save(any(DespesaRecorrente.class));
    }

    @Test
    @DisplayName("Metodo salvar")
    public void deveChamarMetodoParaSalvarOMovimento() {
        MovimentoPagamento movimento1 = gerador.criarMovimento(dr1);
        gerador.salvar(dr1, movimento1);
        verify(movimentoRepository,times(1)).save(any(MovimentoPagamento.class));
    }

}