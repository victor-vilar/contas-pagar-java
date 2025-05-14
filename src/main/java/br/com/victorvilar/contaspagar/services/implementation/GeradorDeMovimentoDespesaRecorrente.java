/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import jakarta.transaction.Transactional;
import jxl.write.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

/**
 * Classe que sera usa para gerar os movimentos das despesas recorrentes.
 * @author victor
 */
@Component
public class GeradorDeMovimentoDespesaRecorrente implements Runnable{
    
    private final MovimentoPagamentoRepository movimentoRepository;
    private final DespesaRepository despesaRepository;
    private GeradorDeDatasDespesasRecorrentes gerador ;


    @Autowired
    public GeradorDeMovimentoDespesaRecorrente(MovimentoPagamentoRepository movimentoRepository, DespesaRepository despesaRepository ){
        this.movimentoRepository = movimentoRepository;
        this.despesaRepository = despesaRepository;

    }


    @Override
    @Transactional
    public void run() {
        //Busca todas as depesas recorrentes que possuem a propriedade 'dataProximoLancamento' anterior ao dia atual.
        List<DespesaAbstrata> despesas = despesaRepository.findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(LocalDate.now());
        for(DespesaAbstrata despesa: despesas){
            var despesaRecorrente = (DespesaRecorrente) despesa;
            realizarLancamentos(despesaRecorrente);
        }
    }



    /**
     * Método criado para facilitar a criação de testes;
     * @return Data de hoje;
     */
    public LocalDate dataHoje(){
        return LocalDate.now();
    }

    /**
     * Metodo principal que será chamado dentro desse runnable.
     * @param despesa
     */
    @Transactional
    public void realizarLancamentos(DespesaRecorrente despesa){

        //Caso o sistema fique muitos dias sem ser aberto, pode acontecer de algumas despesas que possuem um período
        //de pagamento mais frequente, necessite que sejam geradas todas as parcelas retroativas que ficaram pendentes.
        //Então, todo o processo de gerar um novo movimento deve ser repetido, até a data do próximo lançamento ser
        //maior que a data atual.
        LocalDate proximoLancamento = despesa.getDataProximoLancamento();
        while(proximoLancamento == null || proximoLancamento.isBefore(LocalDate.now()) ){
            MovimentoPagamento movimento = criarMovimento(despesa);
            try {
                salvar(despesa, movimento);
                proximoLancamento = despesa.getDataProximoLancamento();
            }catch(DataIntegrityViolationException e){
                System.out.println(e.getMessage());
            }

        }


    }

    /**
     * Cria um novo {@link MovimentoPagamento} com os dados gerados.
     * @param despesa {@link DespesaRecorrente} 'dona' do movimento
     * @return Objeto do Tipo {@link MovimentoPagamento} com os dados preenchidos.
     */
    public MovimentoPagamento criarMovimento(DespesaRecorrente despesa){
        gerador = new GeradorDeDatasDespesasRecorrentes();
        LocalDate dataVencimento = gerador.criarDataVencimento(despesa);
        String referencia = gerarDescricaoReferencia(dataVencimento,despesa.getPeriocidade());
        String integridade = gerarIntegridade(despesa.getId(),dataVencimento);

        MovimentoPagamento movimento = new MovimentoPagamento();
        movimento.setDataVencimento(dataVencimento);
        movimento.setValorPagamento(despesa.getValorTotal());
        movimento.setReferenteParcela(referencia);
        movimento.setIntegridade(integridade);
        movimento.setFormaPagamento(despesa.getFormaPagamentoPadrao());
        return movimento;
    }

    /**
     * Cria uma descrição para a propriedade 'referenteParcela' de {@link MovimentoPagamento}
     * @param data Data de vencimento do Movimento
     * @param periodo Periodo de geração de movimento de {@link DespesaRecorrente}
     * @return String com o valor de 'referenteParcela'
     */
    public String gerarDescricaoReferencia(LocalDate data , Periodo periodo){
        Locale local = new Locale("pt","BR");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMMM",local);

        return switch(periodo){
            case ANUAL -> "ANUIDADE " + data.getYear();
            case MENSAL -> data.format(formato).toUpperCase() + " DE " + data.getYear() ;
            case QUINZENAL -> data.getDayOfMonth() < 14 ? "1ª QUINZENA DE " + data.format(formato).toUpperCase() + " DE " + data.getYear() : "2ª QUINZENA DE " + data.format(formato).toUpperCase() + " DE " + data.getYear();
            case SEMANAL -> "";
            default -> "";
        };
    }

    /**
     * Cria o valor da propriedade 'integridade' de {@link MovimentoPagamento} para garantir que não haja mais de uma
     * entidade que pertença a essa despesa com os mesmos dados no banco de dados.
     * @param id Id da despesa
     * @param dataVencimento data de vencimento do proximo movimento a ser lançado
     * @return String com o valor que será salvo na propriedade 'integridade' de {@link MovimentoPagamento}
     */
    public String gerarIntegridade(long id, LocalDate dataVencimento){
        return id + "/" + dataVencimento.toString();
    }

    /**
     * Adiciona o movimento na lista de movimentos da despesa, salva a despesa e os movimentos.
     * @param despesa Objeto do tipo {@link DespesaRecorrente}
     * @param movimento Objeto do tipo {@link MovimentoPagamento}
     */

    public void salvar(DespesaRecorrente despesa, MovimentoPagamento movimento){
        despesa.addParcela(movimento);
        movimentoRepository.save(movimento);

        despesa.setDataUltimoLancamento(movimento.getDataVencimento());
        despesa.setDataProximoLancamento(gerador.gerarDataDoProximoLancamento(despesa));
        despesaRepository.save(despesa);

    }


}
