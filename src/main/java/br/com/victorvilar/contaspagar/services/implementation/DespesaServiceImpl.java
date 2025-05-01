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

    /**
     * Busca uma despesa trazendo a sua lista de {@link br.com.victorvilar.contaspagar.entities.MovimentoPagamento}
     * @param id Id da despesa que esta buscando
     * @return despesa referente ao id passado.
     */
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
        return null;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);

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
            movimentoService.setIdDespesa(despesa.getId());
            deletarMovimentos(despesa);
        }else{
            despesa = getById(obj.getId());
        }
        despesa.setNomeFornecedor(obj.getNomeFornecedor());
        despesa.setDescricao(obj.getDescricao());
        despesa.setCategoria(obj.getCategoria());
        updateCamposDoTipo(obj,despesa);
        despesa = repository.save(despesa);
        movimentoService.sincronizarMovimentos();
        return despesa;
    }

    /**
     * As despesas podem ser tanto {@link DespesaAvulsa} ou {@link DespesaRecorrente} então deve
     * pegar a sua propriedade 'tipo' para conseguir atualizar os outros campos que pertencem especificamente
     * a essas classes.
     * @param obj Objeto 'detached' que possui os novos valores que serão atualizados.
     * @param despesa Objeto 'persisted' que possui valores que ainda precisam ser atualizados.
     */
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

    /**
     * Atualiza os campos de {@link DespesaAvulsa}
     * @param obj Objeto 'detached' que possui os novos valores que serão atualizados.
     * @param despesa Objeto 'persisted' que possui valores que ainda precisam ser atualizados.
     */
    public void updateDespesaAvulsa(DespesaAvulsa obj, DespesaAvulsa despesa) {
        despesa.setNotaFiscal(obj.getNotaFiscal());
    }

    /**
     * Atualia os campos de {@link DespesaRecorrente}
     * @param obj Objeto 'detached' que possui os novos valores que serão atualizados.
     * @param despesa Objeto 'persisted' que possui valores que ainda precisam ser atualizados.
     */
    public void updateDespesaRecorrente(DespesaRecorrente obj, DespesaRecorrente despesa) {
        despesa.setPeriocidade(obj.getPeriocidade());
        despesa.setDataInicio(obj.getDataInicio());
        despesa.setDataFim(obj.getDataFim());
        despesa.setDiaPagamento(obj.getDiaPagamento());
        despesa.setMesPagamento(obj.getMesPagamento());
        despesa.setFormaPagamentoPadrao(obj.getFormaPagamentoPadrao());
    }

    /**
     * Remove da lista de movimentos da despesa, todos os movimentos que foram excluídos e armazenados
     * temporatiamente na lista de movimentos deletados de {@link MovimentoPagamentoService}.
     * @param despesa Despesa que possui os movimentos que devem ser deletados.
     */
    public void deletarMovimentos(DespesaAbstrata despesa){
        despesa.removerParcela(movimentoService.getMovimentosDeletados());
        movimentoService.saveAll(movimentoService.getMovimentosDeletados());
    }

    @Override
    public DespesaAbstrata findByIdWithMovimentos(Long id) {
        return repository.findByIdWithMovimentos(id);
    }




    


}
