/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.MovimentoPagamentoNotFoundException;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author victor
 */
@Service
public class MovimentoPagamentoServiceImpl implements MovimentoPagamentoService {

    private final MovimentoPagamentoRepository repository;
    private List<MovimentoPagamento> movimentosDeletados = new ArrayList<>();
    private List<MovimentoPagamento> movimentosAtualizados = new ArrayList<>();
    private Long idDespesa;

    public void setIdDespesa(Long id){
        idDespesa = id;
    }


    @Autowired
    public MovimentoPagamentoServiceImpl(MovimentoPagamentoRepository repository){
        this.repository = repository;
    }

    @Override
    public void limpar(){
        movimentosDeletados.clear();
        movimentosAtualizados.clear();
        idDespesa = null;
    }
    @Override
    public List<MovimentoPagamento> getMovimentosDeletados(){
        return movimentosDeletados;
    }
    @Override
    public List<MovimentoPagamento> getMovimentosAtualizados(){
        return movimentosAtualizados;
    }
    @Override
    public void addMovimentoDeletado(MovimentoPagamento movimento){
        if(!movimentosDeletados.contains(movimento)){
            movimentosDeletados.add(movimento);
        }
    }
    @Override
    public void addMovimentoAtualizado(MovimentoPagamento movimento){
        // se o movimento existir dentro da lista e estou tentando coloca-lo novamente
        // antes eu tenho que remover o que já exite e depois adicionar o novo.
        // o metodo contains usa o equals do objeto para saber se ele existe dentro da lista
        // então aqui ele so vai comparar nesse caso o id dos movimentos.
        if(movimentosAtualizados.contains(movimento)){
           movimentosAtualizados.remove(movimento);
        }
        movimentosAtualizados.add(movimento);
    }

    @Override
    public void sincronizarMovimentos(){
        //remove os movimentos deletados e atualiza a propriedade referencia dos movimentos que ainda estao no banco.
        if(!movimentosDeletados.isEmpty()){
            deleteAll(movimentosDeletados);
            List<MovimentoPagamento> movimentos = getAllByDespesaId(idDespesa);
            adicionarOuAtualizarReferenteParcela(movimentos);
            saveAll(movimentos);
        }

        //atualiza as propriedades dos movimetnos que forma atualizado
        if(!movimentosAtualizados.isEmpty()){
            update(movimentosAtualizados);
        }

        limpar();
    }



    @Override
    public List<MovimentoPagamento> getTodos() {
        return repository.findAll();
    }

    @Override
    public MovimentoPagamento getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new MovimentoPagamentoNotFoundException("Não encontrado"));
    }

    @Override
    public MovimentoPagamento save(MovimentoPagamento obj) {
        return repository.save(obj);
    }

    @Override
    public List<MovimentoPagamento> saveAll(List<MovimentoPagamento> objs) {
        return repository.saveAll(objs);
    }

    @Override
    public MovimentoPagamento update(MovimentoPagamento obj) {
        MovimentoPagamento movimento = getById(obj.getId());
        movimento.setDataVencimento(obj.getDataVencimento());
        movimento.setDataPagamento(obj.getDataPagamento());
        movimento.setValorPagamento(obj.getValorPagamento());
        movimento.setValorPago(obj.getValorPago());
        movimento.setObservacao(obj.getObservacao());
        movimento.setFormaPagamento(obj.getFormaPagamento());
        return repository.save(movimento);
    }
    
    @Override
    public List<MovimentoPagamento> update(List<MovimentoPagamento> movimentos) {
        return movimentos.stream().map(m -> update(m)).toList();
    }


    @Override
    public List<MovimentoPagamento> getAllByDespesaId(Long id) {
        return repository.getAllByDespesaId(id);
    }


    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public void deleteAll(List<MovimentoPagamento> objs) {
        repository.deleteAll(objs);
    }

    @Override
    public List<MovimentoPagamento> getAllNaoPagos() {
        return repository.findByDataPagamentoIsNull();
    }

    @Override
    public List<MovimentoPagamento> getAllPagos() {
        return repository.findByDataPagamentoIsNotNull();
    }
    
    @Override
    public List<MovimentoPagamento> getBetweenDatesAndDespesaName(LocalDate dataInicio, LocalDate dataFim, String despesa, boolean pago){
        if(!pago){
            return repository.getBetweenDatesAndDespesaNameNaoPago(dataInicio, dataFim, despesa);
        }else
            return repository.getBetweenDatesAndDespesaNamePago(dataInicio, dataFim, despesa);

    }

    /**
     * Cria os valores da propriedade referente parcela, ou atualiza os valores, caso já existam, de uma lista
     * de movimentos passados.
     * @param movimentos
     */
    public void adicionarOuAtualizarReferenteParcela(List<MovimentoPagamento> movimentos){

        if(movimentos.size() == 0){
            return;
        }

        DespesaAbstrata despesa = movimentos.get(0).getDespesa();
        
        if(despesa != null && despesa.getTipo().equals("RECORRENTE")){
            return;
        }
        
        int quantidade = movimentos.size();
        for(int i = 0; i < quantidade ; i++){
            if(quantidade == 1){
                movimentos.get(0).setReferenteParcela("UNICA");
                break;
            }
            movimentos.get(i).setReferenteParcela(i+1 +"/" + quantidade);
        }
    }

}
