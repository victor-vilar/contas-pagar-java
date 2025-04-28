package br.com.victorvilar.contaspagar.services.implementation;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.exceptions.DespesaNotFoundException;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;

@Service
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository repository;
    private final MovimentoPagamentoService movimentoService;


    @Autowired
    public DespesaServiceImpl(DespesaRepository repository, MovimentoPagamentoService movimentoService) {
        this.repository = repository;
        this.movimentoService = movimentoService;
    
    }

    @Override
    public List<DespesaAbstrata> getTodos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DespesaAbstrata getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));
    }

    public DespesaAbstrata getByIdWithMovimentos(Long id){
        return repository.findByIdWithMovimentos(id);
    }

    @Override
    public DespesaAbstrata save(DespesaAbstrata obj) {
        
        if(obj.getId() !=null){
            return update(obj);
        }
        return this.repository.save(obj);
    }

    @Override
    public List<DespesaAbstrata> saveAll(List<DespesaAbstrata> objs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(List<DespesaAbstrata> objs) {
        // TODO Auto-generated method stub

    }

    @Override
    @Transactional
    public DespesaAbstrata update(DespesaAbstrata obj) {

        DespesaAbstrata despesa;

        if(!movimentoService.getMovimentosDeletados().isEmpty()){
            despesa = getByIdWithMovimentos(obj.getId());
            deletarMovimentos(despesa);
        }else{
            despesa = getById(obj.getId());
        }
        despesa.setNomeFornecedor(obj.getNomeFornecedor());
        despesa.setDescricao(obj.getDescricao());
        despesa.setCategoria(obj.getCategoria());
        updateCamposDoTipo(obj,despesa);
        return repository.save(despesa);
    }

    public void updateCamposDoTipo(DespesaAbstrata obj, DespesaAbstrata despesa){
        String tipo = obj.getTipo();
        switch (tipo) {
            case "AVULSA":
                updateDespesaAvulsa((DespesaAvulsa) obj, (DespesaAvulsa) despesa);
                break;
            case "RECORRENTE":
                updateDespesaRecorrente((DespesaRecorrente) obj, (DespesaRecorrente) despesa);
                break;
        }
    }
    
    public void updateDespesaAvulsa(DespesaAvulsa obj, DespesaAvulsa despesa) {
        despesa.setNotaFiscal(obj.getNotaFiscal());
    }

    public void updateDespesaRecorrente(DespesaRecorrente obj, DespesaRecorrente despesa) {
        despesa.setPeriocidade(obj.getPeriocidade());
        despesa.setDataInicio(obj.getDataInicio());
        despesa.setDataFim(obj.getDataFim());
        despesa.setDiaPagamento(obj.getDiaPagamento());
        despesa.setMesPagamento(obj.getMesPagamento());
        despesa.setFormaPagamentoPadrao(obj.getFormaPagamentoPadrao());
    }

    /**
     * Remove os movimentos da lista de movimentos da despesa, os movimentos que estão na lista de movimentos
     * deletados do service.
     * @param despesa
     */
    public void deletarMovimentos(DespesaAbstrata despesa){
        despesa.removerParcela(movimentoService.getMovimentosDeletados());
        movimentoService.saveAll(movimentoService.getMovimentosDeletados());
    }




    


}
