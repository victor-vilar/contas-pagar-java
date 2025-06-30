package br.com.victorvilar.contaspagar.services.implementation;

import java.time.LocalDate;
import java.util.List;

import br.com.victorvilar.contaspagar.enums.DespesaTipo;
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
        if(despesa.getTipo() == DespesaTipo.DESPESA_RECORRENTE){
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
        DespesaTipo tipo = obj.getTipo();
        switch (tipo) {
            case DESPESA_AVULSA:
                updateDespesaAvulsa((DespesaAvulsa) obj, (DespesaAvulsa) despesa);
                break;
            case DESPESA_RECORRENTE:
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

        //Se o objeto atualizado não tiver uma nota fiscal e o objeto salvo tiver, ira remover.
        if(obj.getNotaFiscal() == null && nota != null){
            despesa.removeNotaFiscal();
         
        }

        //Atualiza os dados da nota fiscal
        if(obj.getNotaFiscal() != null && nota != null){
            nota.setNumero(obj.getNotaFiscal().getNumero());
            nota.setDataEmissao(obj.getNotaFiscal().getDataEmissao());
        }

        //Adiciona uma nota fiscal ao objeto salvo
        if(obj.getNotaFiscal() != null && nota == null){
            nota = new NotaFiscal();
            nota.setNumero(obj.getNotaFiscal().getNumero());
            nota.setDataEmissao(obj.getNotaFiscal().getDataEmissao());
            despesa.setNotaFiscal(nota);
        }
        
        
        
        
    }

    /**
     * Atualiza os campos de {@link DespesaRecorrente}
     * @param obj Objeto 'detached' que possui os novos valores que serão atualizados.
     * @param despesa Objeto 'persisted' que possui valores que ainda precisam ser atualizados.
     */
    public void updateDespesaRecorrente(DespesaRecorrente obj, DespesaRecorrente despesa) {

        if((obj.getAtivo() && !despesa.getAtivo()) || (obj.getPeriocidade() != despesa.getPeriocidade())){
            reiniciarLancamentosDespesaRecorrente(despesa);
        }

        despesa.setDiaPagamento(obj.getDiaPagamento());
        despesa.setMesPagamento(obj.getMesPagamento());
        despesa.setFormaPagamentoPadrao(obj.getFormaPagamentoPadrao());
        despesa.setPeriocidade(obj.getPeriocidade());
        despesa.setAtivo(obj.getAtivo());
    }

    /**
     * <p>
     * O sistema realiza os lançamentos das {@link DespesaRecorrente} no momento da sua inicialização,
     * dessa forma caso o sistema fique muito tempo sem ser aberto(dias, semanas, meses) e tiver {@link DespesaRecorrente}
     * que deveriam ter tido movimentos lançados, ele irá realizar os lançamentos retroativos.
     * </p>
     * <p>
     * Caso alguma {@link DespesaRecorrente} tenha ficado algum tempo desativada e tenha sido ativada novamente,
     * é necessário reiniciar os seus campos de dataUltimoLancamento e dataProximoLancamento para evitar que
     * o sistema realize os lançamentos retroativos de datas em que a despesa estava inativa.
     * </p>
     * <p>
     * A partir do momento que uma despesa recorrente é ativada novamente o sistema
     * só deve se preocupar com os futuros lançamentos.
     * </p>
     * @param despesa objeto do tipo {@link DespesaRecorrente}
     */
    public void reiniciarLancamentosDespesaRecorrente(DespesaRecorrente despesa){
        despesa.setDataProximoLancamento(null);
        despesa.setDataUltimoLancamento(null);
    }



    @Override
    public DespesaAbstrata findByIdWithMovimentos(Long id) {
        return repository.findByIdWithMovimentos(id);
    }

    @Override
    public List<DespesaAbstrata> findByTipo(DespesaTipo tipo) {
        return repository.findByTipo(tipo);
    }




    


}
