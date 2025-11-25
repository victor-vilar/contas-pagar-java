/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamentoParaRelatorio;
import br.com.victorvilar.contaspagar.enums.DespesaTipo;
import br.com.victorvilar.contaspagar.enums.TipoDeExport;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ReportUtil;
import br.com.victorvilar.contaspagar.views.ProgramacaoPagamentoView;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class ProgramacaoPagamentoControllerTest {
    
    @InjectMocks
    @Spy
    private ProgramacaoPagamentoController controller;
    
    @Mock
    private ReportUtil util;
    
    @Mock
    private MovimentoPagamentoService service;
    
    private ProgramacaoPagamentoView view;
    
    private MovimentoPagamento mov1;
    private MovimentoPagamento mov2;
    
    @BeforeEach
    public void setUp() {
        view = new ProgramacaoPagamentoView(controller);
        DespesaAvulsa avulsa = (DespesaAvulsa) getDespesa();
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setForma("boleto Bancário");
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setCategoria("Energia");
        mov1 = new MovimentoPagamento();
        mov1.setDataPagamento(LocalDate.now());
        mov1.setDataVencimento(LocalDate.now());
        mov1.setDespesa(getDespesa());
        mov1.setFormaPagamento(formaPagamento);
        
        mov2 = new MovimentoPagamento();
    }
    
    public DespesaAbstrata getDespesa(){
    
        DespesaAvulsa avulsa = new DespesaAvulsa();
        avulsa.setId(1l);
        avulsa.setDescricao("despesa 1");
        avulsa.setNome("despesa 1");
        avulsa.setTipo(DespesaTipo.DESPESA_AVULSA);
        return avulsa;
    }
    
    @Test
    public void emitirProgramaçãoDeveChamarOMetodoDeChecagemDeErro(){
        
        view.getFieldDataInicial().setText("10/10/2025");
        view.getFieldDataFinal().setText("11/10/2025");
        
        when(service.getBetweenDatesAndDespesaName(
                    ConversorData.paraData(view.getFieldDataInicial().getText()),
                    ConversorData.paraData(view.getFieldDataFinal().getText()),
                    "",
                    false)).thenReturn(List.of(mov1,mov2));
        controller.emitirProgramacaoDePagamento();
        verify(controller,times(1)).checarErros();
    }
    
    
    
    
 
    
}
