/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.MovimentoPagamentoNotFoundException;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import jakarta.transaction.Transactional;
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



    @Autowired
    public MovimentoPagamentoServiceImpl(MovimentoPagamentoRepository repository){
        this.repository = repository;
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
        movimento.setReferenteParcela(obj.getReferenteParcela());
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
    
    
    
    /**
     * Atualiza o banco de dados de acordo com as alterações realizadas nos movimentos.
     * Se houve movimentos deletados, eles serão removidos do banco.
     * Se houve movimentos atualizados, eles serão atualizados no banco.
     */
    @Transactional
    public void update(){

        if(!movimentosAtualizados.isEmpty()){
           update(movimentosAtualizados); 
        }

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

}
