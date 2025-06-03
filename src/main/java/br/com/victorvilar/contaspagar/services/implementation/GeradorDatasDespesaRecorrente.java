package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GeradorDatasDespesaRecorrente implements Runnable{

    private final DespesaRepository despesaRepository;
    private final GeradorDeMovimentoDespesaRecorrente gerador;


    @Autowired
    public GeradorDatasDespesaRecorrente(DespesaRepository despesaRepository ,GeradorDeMovimentoDespesaRecorrente gerador){
        this.despesaRepository = despesaRepository;
        this.gerador = gerador;
    }

    @Override
    public void run() {
        List<DespesaAbstrata> despesas = despesaRepository.findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(LocalDate.now());
        for(DespesaAbstrata despesa: despesas){
            var despesaRecorrente = (DespesaRecorrente) despesa;
            gerador.realizarLancamentos(despesaRecorrente);
        }
    }
}
