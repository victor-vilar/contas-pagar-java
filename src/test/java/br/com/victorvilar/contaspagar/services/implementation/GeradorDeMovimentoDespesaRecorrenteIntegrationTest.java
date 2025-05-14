package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.ErpApplication;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.entities.interfaces.Despesa;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.exceptions.DespesaNotFoundException;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

//SpringBootTest, digo para iniciar o contexto do spring no teste
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class GeradorDeMovimentoDespesaRecorrenteIntegrationTest {

    @Autowired
    @InjectMocks
    private GeradorDeMovimentoDespesaRecorrente gerador;

    @Autowired
    private MovimentoPagamentoRepository movimentoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Mock
    private GeradorDeDatasDespesasRecorrentes geradorDatas;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withDatabaseName("teste")
            .withUsername("postgres")
            .withPassword("123456")
            .withReuse(true);


    //passa configurações dinamicas para o spring ao invez dele puxar as configurações do application.properties
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",postgres::getJdbcUrl);
        registry.add("spring.datasource.useraname",postgres::getUsername);
        registry.add("spring.datasource.password",postgres::getPassword);
    }

    static{
        System.setProperty("java.awt.headless","false");

    }



    @BeforeEach
    public void setUp(){
        movimentoRepository.deleteAll();
        despesaRepository.deleteAll();
    }


    public void saveDespesa(Periodo periodo, LocalDate dataUltimoLancamento){
        if(dataUltimoLancamento == null){
            dataUltimoLancamento = LocalDate.of(2020,1,1);
        }
        DespesaRecorrente dr1 = new DespesaRecorrente();
        dr1.setPeriocidade(periodo);
        dr1.setDiaPagamento(1);
        dr1.setMesPagamento(1);
        dr1.setDataUltimoLancamento(dataUltimoLancamento);
        dr1.setDescricao("teste");
        dr1.setNomeFornecedor("teste");
        dr1.setDataInicio(LocalDate.of(2020,1,1));
        dr1.setDataFim(LocalDate.of(9999,1,1));
        dr1.setValorTotal(new BigDecimal("1000"));
        despesaRepository.save(dr1);
    }


    @Test
    public void deveLancarOsMovimentosDeUmaDespesaAnualInclusiveMovimentosRetroativos(){
        saveDespesa(Periodo.ANUAL,null);
        when(geradorDatas.dataHoje()).thenReturn(LocalDate.of(2025,5,1));
        gerador.run();
        List<MovimentoPagamento> movimentos = movimentoRepository.findByDataPagamentoIsNull();
        MovimentoPagamento movimento = movimentos.get(0);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2021,01,01));
        movimento = movimentos.get(1);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2022,01,01));
        movimento = movimentos.get(2);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2023,01,01));
        movimento = movimentos.get(3);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2024,01,01));
        movimento = movimentos.get(4);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,01,01));



    }

    @Test
    public void deveTerSetadoADataDoProximoLancamentoDeUmaDespesaAnualParaOnzeMesesAposUltimaDataLancada(){
        saveDespesa(Periodo.ANUAL,null);
        when(geradorDatas.dataHoje()).thenReturn(LocalDate.of(2025,5,1));
        gerador.run();
        DespesaRecorrente d = (DespesaRecorrente) despesaRepository.findAll().get(0);
        assertEquals(d.getDataUltimoLancamento(),LocalDate.of(2025,1,1));
        assertEquals(d.getDataProximoLancamento(),LocalDate.of(2025,12,1));
    }

    @Test
    public void deveLancarOsMovimentosDeUmaDespesaMensalInclusiveMovimentosRetroativos(){
        saveDespesa(Periodo.MENSAL, LocalDate.of(2025,1,1));
        when(geradorDatas.dataHoje()).thenReturn(LocalDate.of(2025,5,1));
        gerador.run();
        List<MovimentoPagamento> movimentos = movimentoRepository.findByDataPagamentoIsNull();
        assertTrue(movimentos.size() == 4 );
        MovimentoPagamento movimento = movimentos.get(0);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,02,01));
        movimento = movimentos.get(1);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,03,01));
        movimento = movimentos.get(2);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,04,01));
        movimento = movimentos.get(3);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,05,01));



    }

    @Test
    public void deveSetarADataDoProximoLancamentoParaOProximoMesEmUmaDespesaMensal(){
        saveDespesa(Periodo.MENSAL, LocalDate.of(2025,1,1));
        when(geradorDatas.dataHoje()).thenReturn(LocalDate.of(2025,5,1));
        gerador.run();
        DespesaRecorrente d = (DespesaRecorrente) despesaRepository.findAll().get(0);
        assertEquals(d.getDataUltimoLancamento(),LocalDate.of(2025,5,1));
        assertEquals(d.getDataProximoLancamento(),LocalDate.of(2025,5,16));

    }

    @Test
    public void deveLancarOsMovimentosDeUmaDespesaQuinzenalInclusiveMovimentosRetroativos(){
        saveDespesa(Periodo.QUINZENAL, LocalDate.of(2025,1,1));
        when(geradorDatas.dataHoje()).thenReturn(LocalDate.of(2025,5,1));
        gerador.run();
        List<MovimentoPagamento> movimentos = movimentoRepository.findByDataPagamentoIsNull();
        assertEquals(movimentos.size(),9);
        MovimentoPagamento movimento = movimentos.get(0);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,01,15));
        movimento = movimentos.get(1);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,02,1));
        movimento = movimentos.get(2);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,02,15));
        movimento = movimentos.get(3);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,03,1));
        movimento = movimentos.get(4);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,03,15));
        movimento = movimentos.get(5);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,04,1));
        movimento = movimentos.get(6);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,04,15));
        movimento = movimentos.get(7);
        assertEquals(movimento.getDataVencimento(), LocalDate.of(2025,05,1));

    }













}
