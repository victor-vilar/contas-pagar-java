package br.com.victorvilar.contaspagar.services.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victorvilar.contaspagar.entities.Despesa;
import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import javax.swing.table.DefaultTableModel;

@Service
public class DespesaServiceImpl implements DespesaService {

    private DespesaRepository repository;
    

    @Autowired
    public DespesaServiceImpl(DespesaRepository repository) {
        this.repository = repository;
    
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
        // TODO Auto-generated method stub
        return null;
    }


}
