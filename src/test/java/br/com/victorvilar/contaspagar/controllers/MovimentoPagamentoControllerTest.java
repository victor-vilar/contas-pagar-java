package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.AppMensagens;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.MovimentoPagamentoView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;

@ExtendWith(MockitoExtension.class)
class MovimentoPagamentoControllerTest {

    @InjectMocks
    @Spy
    MovimentoPagamentoController controller;

    @Mock
    DespesaService despesaService;

    @Mock
    MovimentoPagamentoService movimentoService;

    MovimentoPagamentoView view;

    @BeforeEach
    public void setUp(){

        view = new MovimentoPagamentoView();
        controller.setView(view);


    }

    @Test
    @DisplayName("metodo limpar")
    public void deveLimparTodosOsCamposDaView(){

        view.getFieldDespesa().setText("teste");
        view.getFieldDataInicio().setText("01/01/2025");
        view.getFieldDataFim().setText("01/01/2025");
        view.getCheckboxPagas().setSelected(true);

        controller.limparCampos();

        assertEquals(view.getFieldDespesa().getText(),"");
        assertEquals(view.getFieldDataInicio().getText(),"");
        assertEquals(view.getFieldDataFim().getText(),"");
        assertFalse(view.getCheckboxPagas().isSelected());

    }
    
    @Test
    public void metodoInicializarDeveChamarMetodoPesquisar(){
        controller.inicializarTabela();
        verify(controller,times(1)).pesquisar();
    }
    
    @Test
    public void metodoPreencherTabelaDeveAdicionarATabelaTodosOsMovimentosDaLista(){
        
        DespesaAvulsa d1 = new DespesaAvulsa();
        d1.setId(1l);
        d1.setNome("LOREM TEST");
        d1.setDescricao("LOREM IPSUM");
        d1.setQuitado(false);
        
        FormaPagamento fp2 = new FormaPagamento();
        fp2 = new FormaPagamento();
        fp2.setId(2L);
        fp2.setForma("BOLETO");
        
        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        m1.setFormaPagamento(fp2);

        MovimentoPagamento m2 = new MovimentoPagamento();
        m2.setId(2L);
        m2.setReferenteParcela("2/2");
        m2.setDataVencimento(LocalDate.of(2025, Month.APRIL, 1));
        m2.setValorPagamento(new BigDecimal("1000"));
        m2.setFormaPagamento(fp2);
        
        List<MovimentoPagamento> movimentos = new ArrayList<>();
        movimentos.add(m1);
        movimentos.add(m2);
        
        d1.addParcela(m1);
        d1.addParcela(m2);
        
        controller.preencherTabela(movimentos);
        
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        
        for(int i = 0; i < 1 ; i++){
          
            assertEquals(String.valueOf(model.getValueAt(i,0)),String.valueOf(movimentos.get(i).getId()));
            assertEquals(model.getValueAt(i, 1),ConversorData.paraString(movimentos.get(i).getDataVencimento()));
            assertEquals(model.getValueAt(i, 2),movimentos.get(i).getDespesa().getNome());
            assertEquals(model.getValueAt(i, 3),movimentos.get(i).getReferenteParcela());
            assertEquals(model.getValueAt(i, 4),ConversorMoeda.paraString(movimentos.get(i).getValorPagamento()));
            assertEquals(model.getValueAt(i, 5),movimentos.get(i).getFormaPagamento().getForma());
        }
        
        
    }
    
    @Test
    public void metodoBuscaDespesaDeveTrazerAsDepesasComOsSeusMovimentos(){
        
        FormaPagamento fp2 = new FormaPagamento();
        fp2 = new FormaPagamento();
        fp2.setId(2L);
        fp2.setForma("BOLETO");
        
        DespesaAvulsa d1 = new DespesaAvulsa();
        d1.setId(1l);
        d1.setNome("LOREM TEST");
        d1.setDescricao("LOREM IPSUM");
        d1.setQuitado(false);
        
        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        m1.setFormaPagamento(fp2);
        
        d1.addParcela(m1);
        when(movimentoService.getById(any(Long.class))).thenReturn(m1);
        when(despesaService.findByIdWithMovimentos(any(Long.class))).thenReturn(d1);
        
        controller.buscarDespesa(1L);
        
        verify(movimentoService,times(1)).getById(any(Long.class));
        verify(despesaService,times(1)).findByIdWithMovimentos(any(Long.class));
    }
    
    @Test
    public void metodoPesquisarProcuraComDatasPreDefinidasSeCamposNaViewEstiverVazio(){
    
        controller.pesquisar();
        verify(movimentoService,times(1)).getBetweenDatesAndDespesaName(LocalDate.of(2000, Month.MARCH, 1), LocalDate.of(9999, Month.MARCH, 1), "", false);
    }
    
    @Test
    public void metodoPesquisarProcuraComDadosDaView(){
    
        view.getFieldDespesa().setText("teste");
        view.getFieldDataInicio().setText("01/01/2025");
        view.getFieldDataFim().setText("31/01/2025");
        view.getCheckboxPagas().setSelected(true);
        controller.pesquisar();
        verify(movimentoService,times(1)).getBetweenDatesAndDespesaName(LocalDate.of(2025, Month.JANUARY, 1), LocalDate.of(2025, Month.JANUARY, 31), "teste", true);
        
        
    }
    
    @Test
    public void metodoBuscarCodigoMovimentosTabelaRetornaOsCodigosDosMovimentosDasLinhasPassadas() {
        
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        model.addRow(new Object[]{1L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{10L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{100L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{2L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{3L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{5L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        
        int linhasSelecionadas[] = {0,3,4};
        List<Long> codigos = controller.buscarCodigoMovimentosTabela(linhasSelecionadas);
        
        assertEquals(codigos.get(0), 1L);
        assertEquals(codigos.get(1), 2L);
        assertEquals(codigos.get(2), 3L);
        
    }
    
    @Test
    public void metodoBuscarCodigoMovimentosTabelaDeveLançarExceptionSeOMovimentoNaTabelaForParcelaUnica(){
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        model.addRow(new Object[]{1L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{10L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{100L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{2L,"01/01/2025","teste",AppMensagens.PARCELA_UNICA,"R$ 400,00","PIX"});
        model.addRow(new Object[]{3L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{5L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        
        int linhasSelecionadas[] = {0,3,4};
        
        Assertions.assertThrows(QuantidadeDeParcelasException.class, ()
                -> controller.buscarCodigoMovimentosTabela(linhasSelecionadas));
    }
    
    @Test
    public void metodoDeletarDeveChamarMetodoBuscarCodigoMovimentosTabela(){
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        model.addRow(new Object[]{1L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{10L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{100L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{3L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{5L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        
        int linhasSelecionadas[] = {0,3,4};
        
        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        
        DespesaAvulsa d1 = new DespesaAvulsa();
        d1.setId(1l);
        d1.setNome("LOREM TEST");
        d1.setDescricao("LOREM IPSUM");
        d1.setQuitado(false);
        d1.addParcela(m1);
        
        
        
        when(movimentoService.getById(any(Long.class))).thenReturn(m1);
        
        controller.deletar(linhasSelecionadas);
        verify(controller,times(1)).buscarCodigoMovimentosTabela(linhasSelecionadas);
        
    }
            

    @Test
    public void metodoDeletarDeveChamarMetodoDoMovimentoService(){
        DefaultTableModel model = (DefaultTableModel) view.getTableMovimentos().getModel();
        model.addRow(new Object[]{1L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{10L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{100L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{3L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        model.addRow(new Object[]{5L,"01/01/2025","teste","1/3","R$ 400,00","PIX"});
        
        int linhasSelecionadas[] = {0,3,4};
        
        MovimentoPagamento m1 = new MovimentoPagamento();
        m1.setId(1L);
        m1.setReferenteParcela("1/2");
        m1.setDataVencimento(LocalDate.of(2025, Month.MARCH, 1));
        m1.setValorPagamento(new BigDecimal("1000"));
        
        DespesaAvulsa d1 = new DespesaAvulsa();
        d1.setId(1l);
        d1.setNome("LOREM TEST");
        d1.setDescricao("LOREM IPSUM");
        d1.setQuitado(false);
        d1.addParcela(m1);
        
        
        
        when(movimentoService.getById(any(Long.class))).thenReturn(m1);
        controller.deletar(linhasSelecionadas);
        verify(movimentoService,times(3)).setIdDespesa(any(Long.class));
        verify(movimentoService,times(3)).addMovimentoDeletado(any(MovimentoPagamento.class));
        
        
        
        
    }
}