package br.com.victorvilar.contaspagar.services.implementation;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;
import br.com.victorvilar.contaspagar.entities.DespesaAvulsa;
import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.NotaFiscal;
import br.com.victorvilar.contaspagar.exceptions.DespesaNotFoundException;
import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.services.interfaces.DespesaService;
import br.com.victorvilar.contaspagar.services.interfaces.MovimentoPagamentoService;

@Service
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository repository;
    private final MovimentoPagamentoService movimentoService;
    private final GeradorDeMovimentoDespesaRecorrente gerador;

    @Autowired
    public DespesaServiceImpl(DespesaRepository repository,
                              MovimentoPagamentoService movimentoService,
                              GeradorDeMovimentoDespesaRecorrente gerador) {
        this.repository = repository;
        this.movimentoService = movimentoService;
        this.gerador = gerador;
    
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
        DespesaAbstrata despesa = this.repository.save(obj);
        if(despesa.getTipo().equals("RECORRENTE")){
            gerador.realizarLancamentos((DespesaRecorrente) despesa);
        }
        return despesa;
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


        movimentoService.setIdDespesa(obj.getId());
        movimentoService.sincronizarMovimentos();

        DespesaAbstrata despesa = getById(obj.getId());
        despesa.setNome(obj.getNome());
        despesa.setDescricao(obj.getDescricao());
        despesa.setCategoria(obj.getCategoria());
        updateCamposDoTipo(obj,despesa);
        despesa = repository.save(despesa);
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
        NotaFiscal nota = despesa.getNotaFiscal() ;
        
        if(obj.getNotaFiscal() == null && nota != null){
            despesa.removeNotaFiscal();
         
        }
        
        if(obj.getNotaFiscal() != null && nota != null){
            nota.setNumero(obj.getNotaFiscal().getNumero());
            nota.setDataEmissao(obj.getNotaFiscal().getDataEmissao());
        }
        
        if(obj.getNotaFiscal() != null && nota == null){
            nota = new NotaFiscal();
            nota.setNumero(obj.getNotaFiscal().getNumero());
            nota.setDataEmissao(obj.getNotaFiscal().getDataEmissao());
            despesa.setNotaFiscal(nota);
        }
        
        
        
        
    }

    /**
     * Atualia os campos de {@link DespesaRecorrente}
     * @param obj Objeto 'detached' que possui os novos valores que serão atualizados.
     * @param despesa Objeto 'persisted' que possui valores que ainda precisam ser atualizados.
     */
    public void updateDespesaRecorrente(DespesaRecorrente obj, DespesaRecorrente despesa) {
        despesa.setPeriocidade(obj.getPeriocidade());
        despesa.setAtivo(obj.getAtivo());
        despesa.setDiaPagamento(obj.getDiaPagamento());
        despesa.setMesPagamento(obj.getMesPagamento());
        despesa.setFormaPagamentoPadrao(obj.getFormaPagamentoPadrao());
    }

    @Override
    public DespesaAbstrata findByIdWithMovimentos(Long id) {
        return repository.findByIdWithMovimentos(id);
    }

    @Override
    public List<DespesaAbstrata> findByTipo(String tipo) {
        return repository.findByTipo(tipo);
    }




    


}
