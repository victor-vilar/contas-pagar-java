/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.views.FinalizarMovimentoPagamentoView;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;


/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class FinalizarMovimentoControllerTest {
    
    @InjectMocks
    @Spy
    private FinalizarMovimentoController controller;
   
    @Mock
    private MovimentoPagamentoService service;
    
    private FinalizarMovimentoPagamentoView view;

    @Mock
    private FormaPagamentoService formaPagamentoService;

    MovimentoPagamento movimento;
    public FinalizarMovimentoControllerTest() {
    }
    
    @BeforeEach
    public void setUpClass() {
        FormaPagamento f1 = new FormaPagamento();
        FormaPagamento f2 = new FormaPagamento();
        f1.setId(1L);
        f1.setForma("forma 1");
        f2.setId(2L);
        f2.setForma("forma 2");
        when(formaPagamentoService.getTodos()).thenReturn(List.of(f1,f2));

        view = new FinalizarMovimentoPagamentoView();
        controller.setView(view);
        controller.inicializarComboBox();


        DespesaAvulsa despesa = new DespesaAvulsa();
        despesa.setNome("CONTA TESTE");
        movimento = new MovimentoPagamento();
        movimento.setId(1l);
        movimento.setDataVencimento(LocalDate.now());
        movimento.setDespesa(despesa);
        movimento.setFormaPagamento(new FormaPagamento());
        movimento.setReferenteParcela("unica");
        movimento.setValorPagamento(new BigDecimal("1000"));
        
    }
    
    @Test
    @DisplayName("Metodo buscar pega o movimento no banco e adiciona em uma variavel no controller")
    public void metodoBuscarBuscaMovimento() {
        when(service.getById(1l)).thenReturn(movimento);
        controller.buscar(1l);
        verify(service, times(1)).getById(1l);
        Assertions.assertNotNull(controller.getMovimento());
    }
    
    
    @Test
    @DisplayName("Metodo salvar seta novos valores para o MovimentoPagmanto")
    public void metodoSalvarAtualizaMovimento(){
       
        when(service.getById(1l)).thenReturn(movimento);
       controller.buscar(1l);
       MovimentoPagamento movimento = controller.getMovimento();
       
       view.getFieldDataPagamento().setText(ConversorData.paraString(LocalDate.now()));
       view.getFieldValorPago().setText(ConversorMoeda.paraString(new BigDecimal("1000.00")));
       view.getFieldObservacao().setText("Observacao");
       view.getComboFormaPagamento().setSelectedIndex(1);
       
       
       
       controller.salvar();
       assertEquals(movimento.getDataPagamento(),LocalDate.now());
       assertEquals(movimento.getValorPago(), new BigDecimal("1000.00"));
       assertEquals(movimento.getObservacao(),"Observacao");
       verify(service,times(1)).save(movimento);
    
    }
    
    @Test
    @DisplayName("metodo deve preencher view com os dados do movimento")
    public void metodoPreencherCampos(){
    
        controller.preencherCampos(movimento);
        
        assertEquals(view.getFieldCodigo().getText(),String.valueOf(movimento.getId()));
        assertEquals(view.getFieldParcela().getText(),movimento.getReferenteParcela());
        assertEquals(view.getFieldDescricao().getText(),movimento.getDespesa().getNome());
        assertEquals(view.getFieldVencimento().getText(),ConversorData.paraString(movimento.getDataVencimento()));
        assertEquals(view.getFieldValor().getText(),ConversorMoeda.paraString(movimento.getValorPagamento()));
        

        
    }
    
    
    @Test
    @DisplayName("metodo prencher os campos do formulário ao abrir a view")
    public void metodoAoAbrirFormulario(){
        
        view.getFieldVencimento().setText("teste");
        view.getFieldValor().setText("teste");

        view.getFieldDataPagamento().setText("");
        view.getFieldValorPago().setText("");
        
        controller.aoAbrirFormulario();
        
        assertEquals(view.getFieldDataPagamento().getText(),view.getFieldVencimento().getText());
        assertEquals(view.getFieldValorPago().getText(),view.getFieldValor().getText());
       
    }
    
    
    @Test
    @DisplayName("Metodo limpar campos reseta valores e limpar referencia de movimento pagamento")
    public void metodoLimparCampos(){
        
                when(service.getById(1l)).thenReturn(movimento);
       controller.buscar(1l);
        
        view.getFieldCodigo().setText("teste");
        view.getFieldParcela().setText("teste");
        view.getFieldDescricao().setText("teste");
        view.getFieldVencimento().setText("teste");
        view.getFieldValor().setText("teste");
        
        view.getFieldDataPagamento().setText("teste");
        view.getFieldValorPago().setText("teste");
        view.getFieldObservacao().setText("teste");
        
        controller.limparCampos();
        
        assertEquals(view.getFieldCodigo().getText(),"");
        assertEquals(view.getFieldParcela().getText(),"");
        assertEquals(view.getFieldDescricao().getText(),"");
        assertEquals(view.getFieldVencimento().getText(),"");
        assertEquals(view.getFieldValor().getText(),"");
        
        assertEquals(view.getFieldDataPagamento().getText(),"");
        assertEquals(view.getFieldValorPago().getText(),"");
        assertEquals(view.getFieldObservacao().getText(),"");
        assertEquals(controller.getMovimento(),null);
    
    }
    
    
    
    
}
