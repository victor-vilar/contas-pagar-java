/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.services.implementation.MovimentoPagamentoServiceImpl;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class MovimentoPagamentoServiceTest {

    @Spy
    @InjectMocks
    MovimentoPagamentoServiceImpl service;
    
    @Mock
    MovimentoPagamentoRepository repository;

    List<MovimentoPagamento> movimentos = new ArrayList<>();
    MovimentoPagamento mp1;
    MovimentoPagamento mp2;
    MovimentoPagamento mp3;
    FormaPagamento fp1;
    FormaPagamento fp2;

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

        mp3.setId(3L);
        mp3.setReferenteParcela("1/5");
        mp3.setDataVencimento(ConversorData.paraData("0104"));
        mp3.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp3.setDataPagamento(ConversorData.paraData("0104"));

        fp1 = new FormaPagamento();
        fp1.setForma("BOLETO BANCÁRIO");
        fp2 = new FormaPagamento();
        fp2.setForma("CARTÃO");
    }



    @Test
    @DisplayName("metodo atulizar deve atualizar todos as propriedades do objeto passado")
    public void updateDeveAtualizarPropriedadesDoObjeto(){

        when(repository.findById(mp1.getId())).thenReturn(Optional.of(mp1));
        when(repository.save(any(MovimentoPagamento.class))).thenAnswer(c -> c.getArgument(0));
        MovimentoPagamento mp = service.update(mp1);

        assertEquals(mp.getId(),mp1.getId());
        assertEquals(mp.getDataPagamento(),mp1.getDataPagamento());
        assertEquals(mp.getDataVencimento(),mp1.getDataVencimento());
        assertEquals(mp.getFormaPagamento(),mp1.getFormaPagamento());
        assertEquals(mp.getReferenteParcela(),mp1.getReferenteParcela());
        assertEquals(mp.getValorPagamento(),mp1.getValorPagamento());
        assertEquals(mp.getValorPago(),mp1.getValorPago());
        assertEquals(mp.getObservacao(),mp1.getObservacao());


    }

    @Test
    @DisplayName("deve chamr o metodo update para cada item da lista")
    public void updateDeveAtualizarListaDeObjetos() {
        when(repository.findById(mp1.getId())).thenReturn(Optional.of(mp1));
        when(repository.findById(mp2.getId())).thenReturn(Optional.of(mp2));
        when(repository.findById(mp3.getId())).thenReturn(Optional.of(mp3));
        when(repository.save(any(MovimentoPagamento.class))).thenAnswer(c -> c.getArgument(0));
        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);

        List<MovimentoPagamento> movimentosAtualizados = service.update(movimentos);

        assertNotNull(movimentosAtualizados);
        assertFalse(movimentos.isEmpty());
        assertEquals(movimentos.size(),3);


    }

    @Test
    @DisplayName("metodo limpar deve limpar listas e variavel id")
    public void metodoLimparDeveLimparListasESetarValorIdDespesaNull(){
        service.addMovimentoAtualizado(new MovimentoPagamento());
        service.addMovimentoAtualizado(new MovimentoPagamento());
        service.setIdDespesa(1l);
        service.limpar();
        assertEquals(service.getMovimentosAtualizados().size(), 0);
        assertEquals(service.getMovimentosDeletados().size(), 0);
        //assertEquals();
    }

    @Test
    @DisplayName("metodo addMovimentoDeletado deve adcionar um novo movimento a lista")
    public void metodoAddMovimentoDeletadoAdicionaNovoMovimentoALista(){
        assertTrue(service.getMovimentosDeletados().isEmpty());
        service.addMovimentoDeletado(new MovimentoPagamento());
        assertEquals(service.getMovimentosDeletados().size(),1);

    }

    @Test
    @DisplayName("metodo addMovimentoDeletado não deve adicionar um movimento caso ele já exista na lista")
    public void metodoAddMovimentoDeletadoNaoAdicionaMovimentoCasoJaExistaNaLista(){
        assertTrue(service.getMovimentosDeletados().isEmpty());
        service.addMovimentoDeletado(mp1);
        assertEquals(service.getMovimentosDeletados().size(),1);
        service.addMovimentoDeletado(mp1);
        assertEquals(service.getMovimentosDeletados().size(),1);
        service.addMovimentoDeletado(mp2);
        assertEquals(service.getMovimentosDeletados().size(),2);
    }

    @Test
    @DisplayName("metodo addMovimentoAtualizado deve adicionar um movimento")
    public void metodoAddMovimentoAtualizadoDeveAdicionarNovoMovimentoNaLista(){
        assertTrue(service.getMovimentosAtualizados().isEmpty());
        service.addMovimentoAtualizado(mp1);
        assertEquals(service.getMovimentosAtualizados().size(),1);
    }

    @Test
    @DisplayName("metodo addMovimentoAtualizado deve remover um objeto com dados desatualizados da lista e adicionar" +
                 " o novo")
    public void  metodoAddMovimentoAtualizadoDeveRemoverObjetoComDadosAntigosDaListaEAdicionarUmNovo(){

        service.addMovimentoAtualizado(mp1);
        assertEquals(service.getMovimentosAtualizados().size(),1);
        MovimentoPagamento novo = new MovimentoPagamento();
        novo.setId(1l);
        novo.setReferenteParcela("teste");
        service.addMovimentoAtualizado(novo);
        assertEquals(service.getMovimentosAtualizados().size(),1);
        assertEquals(service.getMovimentosAtualizados().get(0).getReferenteParcela(),novo.getReferenteParcela());
        service.addMovimentoAtualizado(mp2);
        assertEquals(service.getMovimentosAtualizados().size(),2);
    }

    @Test
    @DisplayName("metodo sincronizarMovimentos não deve chamar metodos se a lista tiver vazia")
    public void metodoNaoDeveChamarMetodosSeListaDeMovimentosDeletadosEstiverVazia(){
        service.sincronizarMovimentos();
        verify(service,times(0)).deleteAll(any());
        verify(service,times(0)).adicionarOuAtualizarReferenteParcela(any());
        verify(service,times(0)).saveAll(any());
    }

    @Test
    @DisplayName(" metodo sincronizar movimentos deve chamar deleteAll se a lista de movimentos nao estiver vazia")
    public void  metodoSincronizarMovimentosDeveChamarDeleteAllSeLIstaDeDeletadosNaoEstiverVazia() {
        service.addMovimentoDeletado(mp1);
        service.sincronizarMovimentos();
        verify(service,times(1)).deleteAll(any());
    }

    @Test
    @DisplayName(" metodo sincronizar movimentos deve chamar AdicionarOuAtualizarReferenteParcela se a lista de movimentos nao estiver vazia")
    public void  metodoSincronizarMovimentosDeveChamarAdicionarOuAtualizarReferenteParcelaSeLIstaDeDeletadosNaoEstiverVazia() {
        service.addMovimentoDeletado(mp1);
        service.sincronizarMovimentos();
        verify(service,times(1)).adicionarOuAtualizarReferenteParcela(any());
    }

    @Test
    @DisplayName(" metodo sincronizar movimentos deve chamar deleteAll se a lista de movimentos nao estiver vazia")
    public void metodoSincronizarMovimentosDeveChamarSaveAllSeLIstaDeDeletadosNaoEstiverVazia() {
        service.addMovimentoDeletado(mp1);
        service.sincronizarMovimentos();
        verify(service,times(1)).saveAll(any());
    }

    @Test
    @DisplayName("metodo sincronizar não deve chamar metodo se lista de atualizados estiver vazia")
    public void metodoSincronizarNaoDeveChamarMetodoUpdateSeListaDeAtualizadosEstiverVazia(){
        service.sincronizarMovimentos();
        verify(service,times(0)).update(anyList());
    }
    @Test
    @DisplayName("metodo sincronizar deve chamar metodo se lista de atualizados estiver vazia")
    public void metodoSincronizarDeveChamarmetodoUpdateSeListaDeAtualizadosEstiverVazia(){
        when(repository.findById(mp1.getId())).thenReturn(Optional.of(mp1));
        service.addMovimentoAtualizado(mp1);
        service.sincronizarMovimentos();
        verify(service,times(1)).update(anyList());
    }

    @Test
    @DisplayName("metodo sincronizar deve chamar metodo limpar")
    public void metodoSincronizarDeveChamarmetodoLimpar(){
        service.sincronizarMovimentos();
        verify(service,times(1)).limpar();
    }

    @Test
    @DisplayName("metood adicionar gera parcela 'UNICA' quando há somente um item")
    public void metodoadicionarOuAtualizarReferentePagamentoDeveGerarParcelaUnicaQuandoHaSomenteUmMovimento(){
        List<MovimentoPagamento> lista = new ArrayList<>();
        mp1.setReferenteParcela("");
        lista.add(mp1);
        service.adicionarOuAtualizarReferenteParcela(lista);
        assertEquals(mp1.getReferenteParcela(),"UNICA");
        assertEquals(lista.get(0).getReferenteParcela(),"UNICA");

    }

    @Test
    @DisplayName("metood adicionar gerar parcelas de acordo com a quantidade de movimentos")
    public void metodoadicionarOuAtualizarReferentePagamentoDeveGerarParcelaDeAcordoComAQuantidadeDeMovimentos(){
        List<MovimentoPagamento> lista = new ArrayList<>();
        mp1.setReferenteParcela("");
        mp2.setReferenteParcela("");
        mp3.setReferenteParcela("");
        lista.add(mp1);
        lista.add(mp2);
        lista.add(mp3);
        service.adicionarOuAtualizarReferenteParcela(lista);
        assertEquals(lista.get(0).getReferenteParcela(),"1/3");
        assertEquals(lista.get(1).getReferenteParcela(),"2/3");
        assertEquals(lista.get(2).getReferenteParcela(),"3/3");
        assertEquals(mp1.getReferenteParcela(),"1/3");
        assertEquals(mp2.getReferenteParcela(),"2/3");
        assertEquals(mp3.getReferenteParcela(),"3/3");

    }
}


