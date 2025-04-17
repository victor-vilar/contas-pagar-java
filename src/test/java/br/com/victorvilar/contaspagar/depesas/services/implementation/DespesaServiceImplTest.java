package br.com.victorvilar.contaspagar.depesas.services.implementation;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.entities.NotaFiscal;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        recorrente.setDataInicio(LocalDate.now());
        recorrente.setDataFim(LocalDate.of(9999, Month.MARCH, 1));
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
    @DisplayName("metodo update")
    public void deveAtualizarCamposDaDespesaAbstrata() {
        when(repository.findById(1l)).thenReturn(Optional.of(avulsa));
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
    @DisplayName("metodo udpate ")
    public void deveChamarMetodoUpdateDeMovimentoPagamentoService() {
        when(repository.findById(1l)).thenReturn(Optional.of(avulsa));
        service.update(avulsa);
        verify(movimentoService, times(1)).update(anyList());
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
        assertEquals(obj.getDataInicio(),recorrente.getDataInicio());
        assertEquals(obj.getDataFim(),recorrente.getDataFim());
        assertEquals(obj.getDiaPagamento(),recorrente.getDiaPagamento());
        assertEquals(obj.getMesPagamento(),recorrente.getMesPagamento());
        assertEquals(obj.getFormaPagamentoPadrao(),recorrente.getFormaPagamentoPadrao());
    }

}
