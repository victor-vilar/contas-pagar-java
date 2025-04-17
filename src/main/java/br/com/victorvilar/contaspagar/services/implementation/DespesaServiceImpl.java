package br.com.victorvilar.contaspagar.services.implementation;

import java.util.List;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DespesaAbstrata save(DespesaAbstrata obj) {
        
        if(obj.getId() !=null){
            return update(obj);
        }
        return this.repository.save(obj);
    }

    @Override
    public DespesaAbstrata saveAll(List<DespesaAbstrata> objs) {
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
    public DespesaAbstrata update(DespesaAbstrata obj) {
        DespesaAbstrata despesa = repository.findById(obj.getId()).orElseThrow(() -> new DespesaNotFoundException("Despesa não encontrada"));
        despesa.setNomeFornecedor(obj.getNomeFornecedor());
        despesa.setDescricao(obj.getDescricao());
        despesa.setCategoria(obj.getCategoria());
        
        if(obj.getTipo().equals("AVULSA")){
            updateDespesaAvulsa((DespesaAvulsa) obj,(DespesaAvulsa) despesa);
        }
        
        if(obj.getTipo().equals("RECORRENTE")){
            updateDespesaRecorrente((DespesaRecorrente) obj, (DespesaRecorrente) despesa);
        }
        
        movimentoService.update(obj.getParcelas());
        
        return repository.save(despesa);
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



    


}
