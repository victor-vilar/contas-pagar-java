/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.exceptions.DespesaNotFoundException;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Classe que sera usa para gerar os movimentos das despesas recorrentes.
 * @author victor
 */
@Component
public class GeradorDeMovimentoDespesaRecorrente implements Runnable{
    
    private final MovimentoPagamentoRepository movimentoRepository;
    private final DespesaRepository despesaRepository;
    private final GeradorDeDatasDespesasRecorrentes gerador ;


    @Autowired
    public GeradorDeMovimentoDespesaRecorrente(MovimentoPagamentoRepository movimentoRepository, DespesaRepository despesaRepository ,GeradorDeDatasDespesasRecorrentes gerador){
        this.movimentoRepository = movimentoRepository;
        this.despesaRepository = despesaRepository;
        this.gerador = gerador;
    }

    @Override
    public void run() {
        List<DespesaAbstrata> despesas = despesaRepository.findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(LocalDate.now());
        for(DespesaAbstrata despesa: despesas){
            var despesaRecorrente = (DespesaRecorrente) despesa;
            realizarLancamentos(despesaRecorrente);
        }
    }

    /**<p>
     * Metodo que ira realizar lançamento de movimentos para uma {@link DespesaRecorrente}. O metodo possui um loop
     * para gerar lançamentos retroativos para despesas já cadastradas. O loop só ira parar quando a data do próximo
     * lançamento (propriedade dataProximoLancamento de {@link DespesaRecorrente}) for maior que a data atual.
     *</p>
     * <p>
     * As {@link DespesaRecorrente} possuem duas propriedades principais para que esse método funcione de maneira
     * adequada, as propriedades 'dataUltimoLancamento' que marca qual foi a data de vencimento do ultimo movimento
     * ({@link MovimentoPagamento}) gerado para essa despesa e a propriedade 'dataProximoLancamento' que é uma data
     * que marca qual será o proximo dia em que um novo {@link MovimentoPagamento} será lançado para essa despesa.
     *</p>
     *
     *<p>
     * {@link DespesaRecorrente} novas ou inativas que ficaram ativas, possuem as propriedades
     * 'dataUltimoLancamento' e 'dataProximoLancamento' nulos, o metodo então irá apenas realizar os proximos lançamentos
     * ignorando datas anteriores.
     * </p>
     */
    public void realizarLancamentos(DespesaRecorrente despesa){
        despesa = (DespesaRecorrente) despesaRepository.findByIdWithMovimentos(despesa.getId());
        LocalDate proximoLancamento = despesa.getDataProximoLancamento();
        while(proximoLancamento == null || proximoLancamento.isBefore(gerador.dataHoje()) ){
            MovimentoPagamento movimento = criarMovimento(despesa);
            Optional<MovimentoPagamento> movimentoComIntegridade;
            movimentoComIntegridade = movimentoRepository.findByIntegridade(movimento.getIntegridade());
            if(movimentoComIntegridade.isEmpty()){
                salvar(despesa, movimento);
            }
            despesa.setDataUltimoLancamento(movimento.getDataVencimento());
            despesa.setDataProximoLancamento(gerador.gerarDataDoProximoLancamento(despesa));
            proximoLancamento = despesa.getDataProximoLancamento();
        }

        despesaRepository.save(despesa);
    }

    /**
     * Cria um novo {@link MovimentoPagamento} com os dados gerados.
     * @param despesa {@link DespesaRecorrente} 'dona' do movimento
     * @return Objeto do Tipo {@link MovimentoPagamento} com os dados preenchidos.
     */
    public MovimentoPagamento criarMovimento(DespesaRecorrente despesa){

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
     * Adiciona o movimento na lista de movimentos da despesa, salva  os movimentos.
     * @param despesa Objeto do tipo {@link DespesaRecorrente}
     * @param movimento Objeto do tipo {@link MovimentoPagamento}
     */
    @Transactional
    public void salvar(DespesaRecorrente despesa, MovimentoPagamento movimento){
        despesa.addParcela(movimento);
        movimentoRepository.save(movimento);
        despesa.setDataUltimoLancamento(movimento.getDataVencimento());
        despesa.setDataProximoLancamento(gerador.gerarDataDoProximoLancamento(despesa));
        despesaRepository.save(despesa);
    }



}
