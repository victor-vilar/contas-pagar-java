/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.entities.interfaces.Despesa;
import br.com.victorvilar.contaspagar.enums.Periodo;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
    private GeradorDeDatasDespesasRecorrentes gerador;
    
    @Autowired
    public GeradorDeMovimentoDespesaRecorrente(MovimentoPagamentoRepository movimentoRepository, DespesaRepository despesaRepository ){
        this.movimentoRepository = movimentoRepository;
        this.despesaRepository = despesaRepository;
    }

    @Override
    public void run() {
        List<DespesaAbstrata> despesas = despesaRepository.findByTipo("RECORRENTE");
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
    public void realizarLancamentos(DespesaRecorrente despesa){
        gerador = new GeradorDeDatasDespesasRecorrentes();
        if(!verificarSeDeveRealizarLancamento(despesa)){
            return;
        }

        MovimentoPagamento movimento = criarMovimento(despesa);
        try {
            salvar(despesa, movimento);
        }catch(DataIntegrityViolationException e){
            System.out.println("Já existe uma forma de pagamento com essa integridade");
        }
    }


    /**
     * Verifica a necessidade de realizar o lançamento do próximo movimento da despesa.
     * @param despesa Objeto do tipo {@link DespesaRecorrente}
     * @return True se a data atual é igual ou maior que a propriedade 'dataProximoLançamento' de {@link DespesaRecorrente}.
     * True se a propriedade 'dataProximoLancamento' de {@link DespesaRecorrente} for nula.
     * False se a data atual for menor que 'dataProximoLancamento' de {@link DespesaRecorrente}.
     */
    public boolean verificarSeDeveRealizarLancamento(DespesaRecorrente despesa){
        LocalDate dataHoje = dataHoje();
        LocalDate dataProximoLancamento = despesa.getDataProximoLancamento();
        if(dataProximoLancamento == null){
            return true;
        }
        if(!dataHoje.isAfter(dataProximoLancamento) || !dataHoje.isEqual(dataProximoLancamento)) {
            return false;
        }
        return true;
    }

    /**
     * Cria um novo {@link MovimentoPagamento} com os dados gerados.
     * @param despesa {@link DespesaRecorrente} 'dona' do movimento
     * @return Objeto do Tipo {@link MovimentoPagamento} com os dados preenchidos.
     */
    public MovimentoPagamento criarMovimento(DespesaRecorrente despesa){

        LocalDate dataVencimento = gerador.criarDataVencimento(despesa);
        MovimentoPagamento movimento = new MovimentoPagamento();
        movimento.setDataVencimento(dataVencimento);
        movimento.setValorPagamento(despesa.getValorTotal());
        movimento.setReferenteParcela(setarReferenciaMovimento(dataVencimento,despesa.getPeriocidade()));
        movimento.setIntegridade(criarIntegridade(despesa.getId(),dataVencimento));
        return movimento;
    }

    /**
     * Cria uma descrição para a propriedade 'referenteParcela' de {@link MovimentoPagamento}
     * @param data Data de vencimento do Movimento
     * @param periodo Periodo de geração de movimento de {@link DespesaRecorrente}
     * @return String com o valor de 'referenteParcela'
     */
    public String setarReferenciaMovimento(LocalDate data , Periodo periodo){
        Locale local = new Locale("pt","BR");
        DateFormat dateFormat = new SimpleDateFormat("MMMM",local);

        return switch(periodo){
            case ANUAL -> "ANUIDADE " + data.getYear();
            case MENSAL -> dateFormat.format(data) + " DE " + data.getYear() ;
            case QUINZENAL -> "";
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
    public String criarIntegridade(long id, LocalDate dataVencimento){
        return id + "/" + dataVencimento.toString();
    }

    /**
     * Adiciona o movimento na lista de movimentos da despesa, salva a despesa e os movimentos.
     * @param despesa Objeto do tipo {@link DespesaRecorrente}
     * @param movimento Objeto do tipo {@link MovimentoPagamento}
     */
    public void salvar(DespesaRecorrente despesa, MovimentoPagamento movimento){
        despesa.addParcela(movimento);
        despesaRepository.save(despesa);
        movimentoRepository.save(movimento);
    }

}
