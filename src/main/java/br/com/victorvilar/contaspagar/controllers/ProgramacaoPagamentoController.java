/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers;

import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamentoParaRelatorio;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ReportUtil;
import br.com.victorvilar.contaspagar.views.ProgramacaoPagamentoView;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author victor
 */
public class ProgramacaoPagamentoController {
 
    private MovimentoPagamentoService movimentoService;
    private ProgramacaoPagamentoView view;
    private static final String PARAMETRO_DATA_INICIAL ="periodoIni";
    private static final String PARAMETRO_DATA_FINAL ="periodoFim";
    private static final String JASPER_FILE = "contas-em-aberto";
    
    public ProgramacaoPagamentoController(MovimentoPagamentoService movimentoService, ProgramacaoPagamentoView view){
        this.movimentoService = movimentoService;
        this.view = view;
    }
    
    
    public void emitirProgramacaoDePagamento(){
        LocalDate dataInicial = ConversorData.paraData(view.getFieldDataInicial().getText());
        LocalDate dataFinal = ConversorData.paraData(view.getFieldDataFinal().getText());
        List<MovimentoPagamentoParaRelatorio> movimentos = buscarMovimentosParaRelatorio(dataInicial,dataFinal);
        Map<String,Object> parametros = gerarParametrosParaRelatorio(dataInicial, dataFinal);
        String nomeArquivoSaida = gerarNomeArquivoSaida(dataInicial, dataFinal);
        ReportUtil util = new ReportUtil();
        util.generate(movimentos,JASPER_FILE, nomeArquivoSaida,parametros);
    
    }
    
    public List<MovimentoPagamentoParaRelatorio> buscarMovimentosParaRelatorio(LocalDate dataInicial, LocalDate dataFinal){
        List<MovimentoPagamento> movimentos = this.movimentoService.getBetweenDatesAndDespesaName(dataInicial, dataFinal, "", false);
        return movimentos.stream().map(m -> new MovimentoPagamentoParaRelatorio(m)).toList();
    }
    
    public Map<String,Object> gerarParametrosParaRelatorio(LocalDate dataInicial, LocalDate dataFinal){
        Map<String,Object> parametros = new HashMap<>();
        parametros.put(PARAMETRO_DATA_INICIAL,ConversorData.paraString(dataInicial));
        parametros.put(PARAMETRO_DATA_FINAL,ConversorData.paraString(dataFinal));
        return parametros;
    }
    
    public String gerarNomeArquivoSaida(LocalDate dataInicial, LocalDate dataFinal){
        StringBuilder builder = new StringBuilder();
        builder.append("programação: de ");
        builder.append(dataInicial.toString());
        builder.append(" à ");
        builder.append(dataFinal.toString());
        return builder.toString();
    }
    
}
