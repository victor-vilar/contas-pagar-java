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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author victor
 */
@Service
public class MovimentoPagamentoServiceImpl implements MovimentoPagamentoService {

    private final MovimentoPagamentoRepository repository;
    private final FormaPagamentoService formaPagamentoService;
    private List<MovimentoPagamento> movimentosDeletados = new ArrayList<>();
    

    @Autowired
    public MovimentoPagamentoServiceImpl(MovimentoPagamentoRepository repository, FormaPagamentoService formaPagamentoService){
        this.repository = repository;
        this.formaPagamentoService = formaPagamentoService;
    }
    
    public List<MovimentoPagamento> getMovimentosDeletados() {
        return movimentosDeletados;
    }

    public List<MovimentoPagamento> gerarMovimentos(String parcelamento, int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento)
            throws QuantidadeDeParcelasException, DateTimeParseException {

        if (qtdParcelas <= 0) {
            throw new QuantidadeDeParcelasException();
        }

        LocalDate data = ConversorData.paraData(dataInicial);
        List<MovimentoPagamento> movimentos = new ArrayList<>();

        //Se a data passada for dia 31, ira configurar para o dia 30
        if (data.getDayOfMonth() == 31) {
            data = data.minusDays(1);
        }

        for (int i = 0; i < qtdParcelas; i++) {

            MovimentoPagamento mp = new MovimentoPagamento();
            mp.setValorPagamento(ConversorMoeda.paraBigDecimal(valor));
            mp.setFormaPagamento(formaPagamento);

            if (qtdParcelas == 1) {
                mp.setReferenteParcela("UNICA");
            } else {
                mp.setReferenteParcela(i + 1 + "/" + qtdParcelas);

            }

            if (i != 0) {
                data = dataProximaParcela(parcelamento, data);
            }

            mp.setDataVencimento(data);
            movimentos.add(mp);
        }

        return movimentos;
    }

    private LocalDate dataProximaParcela(String parcelamento, LocalDate dataAtual) {

        LocalDate novaData;

        switch (parcelamento) {
            case "ANUAL":
                novaData = dataAtual.plusYears(1);
                break;
            case "MENSAL":
                novaData = dataAtual.plusMonths(1);
                break;
            case "QUINZENAL":
                novaData = dataAtual.plusDays(15);
                break;
            case "SEMANAL":
                novaData = dataAtual.plusWeeks(1);
                break;
            default:
                novaData = dataAtual;
                break;
        }

        return novaData;
    }

    public void atualizarMovimentosTabela(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model) {

        //Busca o movimento na lista. O movimento se encontra na mesma posição da linha
        MovimentoPagamento mp = movimentos.get(linha);
        
        //Atualiza a data de vencimento 
        mp.setDataVencimento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 2))));

        //Atualiza o valor de pagamento            
        mp.setValorPagamento(ConversorMoeda.paraBigDecimal(String.valueOf(model.getValueAt(linha, 3))));

        //Atualiza da data de pagmento
        mp.setDataPagamento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 4))));
        
        //Atualiza a forma de pagamento
        mp.setFormaPagamento(formaPagamentoService.getByForma(String.valueOf(model.getValueAt(linha,5))));
        
        //Atualiza observaçao
        mp.setObservacao((String)model.getValueAt(linha, 6));
        

    }

    /**
     * Deleta os movimentos da lista, de acordo com as linhas deletadas da
     * tabela. Atualiza a propriedade referenteParcela do movimento .
     */
    public void deletarMovimentosTabela(List<MovimentoPagamento> movimentos, int[] linhas) {

        //deleta os movimentos da lista que estão nas mesma posiçaõ das linhas
        //adiciona movimentos deletados na lista para que possam ser excluidos
        //caso já tenham sido salvos no banco.
        //O código começa a eliminar a partir do fim da fila, pois os elementos
        //mudam de posição quando são removidos
        for (int i = linhas.length - 1; i >= 0; i--) {
            movimentosDeletados.add(movimentos.remove(linhas[i]));
        }

        //reorganiza a propriedade referencia parcela dos movimentos.
        int tamanho = movimentos.size();
        for (int i = 0; i < tamanho; i++) {
            
            if(tamanho == 1){
                movimentos.get(i).setReferenteParcela("UNICA");
                break;
            }
            
            movimentos.get(i).setReferenteParcela(i + 1 + "/" + tamanho);
        }

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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public MovimentoPagamento saveAll(List<MovimentoPagamento> objs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll(List<MovimentoPagamento> objs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
