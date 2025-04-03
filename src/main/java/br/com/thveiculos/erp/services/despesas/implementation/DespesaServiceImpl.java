package br.com.thveiculos.erp.services.despesas.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thveiculos.erp.entities.despesas.Despesa;
import br.com.thveiculos.erp.entities.despesas.DespesaAbstrata;
import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.repositories.despesas.DespesaRepository;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import javax.swing.table.DefaultTableModel;

@Service
public class DespesaServiceImpl implements DespesaService {

    private DespesaRepository repository;
    private MovimentoPagamentoService gm ;

    @Autowired
    public DespesaServiceImpl(DespesaRepository repository) {
        this.repository = repository;
        gm = new MovimentoPagamentoService();
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

    @Override
    public List<MovimentoPagamento> gerarMovimentos(String parcelamento, int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento) {
        return gm.gerarMovimentos(parcelamento, qtdParcelas, dataInicial, valor, formaPagamento);

    }

    @Override
    public void atualizarMovimentos(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model) {    
        gm.atualizarMovimentos(movimentos, linha, model);
    }
    
    @Override
    public void deletarMovimentos(List<MovimentoPagamento> movimentos, int[] linhas){
        gm.deletarMovimentos(movimentos, linhas);
        
    }

}
