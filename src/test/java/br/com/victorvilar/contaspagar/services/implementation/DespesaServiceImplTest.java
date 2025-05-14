package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.*;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.services.implementation.DespesaServiceImpl;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DespesaServiceImplTest {

    @InjectMocks
    @Spy
    DespesaServiceImpl service;

    @Mock
    DespesaRepository repository;

    @Mock
    MovimentoPagamentoService movimentoService;

    DespesaAvulsa avulsa;
    DespesaRecorrente recorrente;
    CategoriaDespesa categoria;
    FormaPagamento forma;
    List<MovimentoPagamento> movimentos;

    @BeforeEach
    void setUp() throws Exception {

        categoria = new CategoriaDespesa();
        categoria.setCategoria("Despesas Diversas");

        forma = new FormaPagamento();
        forma.setForma("PIX");

        NotaFiscal nota = new NotaFiscal();
        nota.setNumero("123");
        nota.setDataEmissao(LocalDate.now());

        avulsa = new DespesaAvulsa();
        recorrente = new DespesaRecorrente();

        avulsa.setId(1l);
        avulsa.setNomeFornecedor("TESTE1 TESTE1");
        avulsa.setDescricao("TESTEZZ");
        avulsa.setCategoria(categoria);
        avulsa.setNotaFiscal(nota);

        recorrente.setId(2l);
        recorrente.setNomeFornecedor("TESTE2 TESTE2");
        recorrente.setDescricao("TESTEZZ2");
        recorrente.setCategoria(categoria);
        recorrente.setPeriocidade(Periodo.MENSAL);
        recorrente.setDiaPagamento(10);
        recorrente.setFormaPagamentoPadrao(forma);

    }

    @Test
    @DisplayName("metodo salvar")
    public void deveSalvarDespesaSeNaoTiverId() {
        avulsa.setId(null);
        service.save(avulsa);
        verify(repository, times(1)).save(avulsa);
    }

    @Test
    @DisplayName("metodo salvar")
    public void deveChamarMetodoAtualizarSeTiverId() {
        when(repository.findById(1l)).thenReturn(Optional.of(avulsa));
        service.save(avulsa);
        verify(service, times(1)).update(avulsa);
    }

    @Test
    @DisplayName("metodo update deve chamar getByyId quando a lista de movimentos deletados no serviço for vazio")
    public void deveChamarGetByIdQuandoListaDeDeletadosForVazio(){
        when(repository.findById(1l)).thenReturn(Optional.of(avulsa));
        service.update(avulsa);
        verify(service,times(1)).getById(avulsa.getId());
    }

    @Test
    @DisplayName("metodo update deve chamar getByIdWithMovimentos quando a lista de movimentos deletados NÃO for vazio")
    public void deveChamarGetByIdQuandoListaDeDeletadosNaoForVazio() {
        when(movimentoService.getMovimentosDeletados()).thenReturn(List.of(new MovimentoPagamento()));
        when(repository.findByIdWithMovimentos(1l)).thenReturn(avulsa);
        service.update(avulsa);
        verify(service, times(1)).getByIdWithMovimentos(avulsa.getId());
    }

    @Test
    @DisplayName("metodo update")
    public void deveChamarMetodosetIdDespesaDoMovimentoService() {
        when(movimentoService.getMovimentosDeletados()).thenReturn(List.of(new MovimentoPagamento()));
        when(repository.findByIdWithMovimentos(1l)).thenReturn(avulsa);
        service.update(avulsa);
        verify(movimentoService, times(1)).setIdDespesa(avulsa.getId());
    }

    @Test
    @DisplayName("metodo update")
    public void deveChamarMetodoDeletarMovimentos() {
        when(movimentoService.getMovimentosDeletados()).thenReturn(List.of(new MovimentoPagamento()));
        when(repository.findByIdWithMovimentos(1l)).thenReturn(avulsa);
        service.update(avulsa);
        verify(service, times(1)).deletarMovimentos(avulsa);
    }


    @Test
    @DisplayName("metodo update")
    public void deveAtualizarCamposDaDespesaAbstrata() {
        when(repository.findById(any())).thenReturn(Optional.of(avulsa));
        when(repository.save(any())).thenReturn(avulsa);
        DespesaAvulsa desp = (DespesaAvulsa) service.update(avulsa);
        assertEquals(desp.getNomeFornecedor(), avulsa.getNomeFornecedor());
        assertEquals(desp.getDescricao(), avulsa.getDescricao());
        assertEquals(desp.getNotaFiscal(), avulsa.getNotaFiscal());
    }

    @Test
    @DisplayName("metodo update")
    public void deveChamarMetodoUpdateDespesaAvulsa() {
        when(repository.findById(1l)).thenReturn(Optional.of(avulsa));
        service.update(avulsa);
        verify(service, times(1)).updateDespesaAvulsa(any(DespesaAvulsa.class), any(DespesaAvulsa.class));
    }

    @Test
    @DisplayName("metodo update")
    public void deveChamarMetodoUpdateDespesaRecorrente() {
        when(repository.findById(2l)).thenReturn(Optional.of(recorrente));
        service.update(recorrente);
        verify(service, times(1)).updateDespesaRecorrente(any(DespesaRecorrente.class), any(DespesaRecorrente.class));
    }

    @Test
    @DisplayName("metodo update")
    public void deveChamarMetodoNoSincroninarDeMovimentoService() {
        when(repository.findById(any())).thenReturn(Optional.of(avulsa));
        service.update(avulsa);
        verify(movimentoService,times(1)).sincronizarMovimentos();
    }

    @Test
    @DisplayName("metodo updateCamposDoTipo")
    public void deveChamarMetodoUpdateDespesaAvulsaQuandoTipoForAvulsa() {
        DespesaAbstrata d1 = new DespesaAvulsa();
        DespesaAbstrata d2 = new DespesaAvulsa();
        service.updateCamposDoTipo(d1,d2);
        verify(service,times(1)).updateDespesaAvulsa(any(),any());
    }

    @Test
    @DisplayName("metodo updateCamposDoTipo")
    public void deveChamarMetodoUpdateDespesaAvulsaQuandoTipoForRecorrente() {
        DespesaAbstrata d1 = new DespesaRecorrente();
        DespesaAbstrata d2 = new DespesaRecorrente();
        service.updateCamposDoTipo(d1,d2);
        verify(service,times(1)).updateDespesaRecorrente(any(),any());
    }


    @Test
    @DisplayName("metodo udpate despesa avulsa")
    public void deveAtualizarDadosDespesaAvulsa() {
        DespesaAvulsa obj = new DespesaAvulsa();
        service.updateDespesaAvulsa(obj, avulsa);
        assertEquals(obj.getNotaFiscal(), avulsa.getNotaFiscal());

    }

    @Test
    @DisplayName("metodo udpate despesa recorrente")
    public void deveAtualizarDadosDespesaRecorrente() {
        DespesaRecorrente obj = new DespesaRecorrente();
        service.updateDespesaRecorrente(obj, recorrente);
        assertEquals(obj.getPeriocidade(),recorrente.getPeriocidade());
        assertEquals(obj.getDiaPagamento(),recorrente.getDiaPagamento());
        assertEquals(obj.getMesPagamento(),recorrente.getMesPagamento());
        assertEquals(obj.getFormaPagamentoPadrao(),recorrente.getFormaPagamentoPadrao());
    }

    @Test
    @DisplayName("metodo deletar movimentos")
    public void deveChamarMetodoSaveAllDoMovimentoService() {
        service.deletarMovimentos(new DespesaAvulsa());
        verify(movimentoService,times(1)).saveAll(any());
    }
}
