package br.com.victorvilar.contaspagar.services;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.services.interfaces.FormaPagamentoService;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DespesaControllerHelper {

    private final FormaPagamentoService formaPagamentoService;
    private List<MovimentoPagamento> movimentosAtualizados = new ArrayList<>();

    @Autowired
    public DespesaControllerHelper(FormaPagamentoService formaPagamentoService){
        this.formaPagamentoService = formaPagamentoService;
    }

    public List<MovimentoPagamento> getMovimentosAtualizados(){
        return movimentosAtualizados;
    }

    public void clearList(){
        movimentosAtualizados.clear();
    }

    public List<MovimentoPagamento> gerarMovimentos(String parcelamento, int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento)
            throws QuantidadeDeParcelasException, DateTimeParseException {

        if (qtdParcelas <= 0) {
            throw new QuantidadeDeParcelasException();
        }

        LocalDate data = ConversorData.paraData(dataInicial);
        List<MovimentoPagamento> movimentos = new ArrayList<>();


        if (data.getDayOfMonth() == 31) {
            data = data.minusDays(1);
        }

        for (int i = 0; i < qtdParcelas; i++) {

            MovimentoPagamento mp = new MovimentoPagamento();
            mp.setValorPagamento(ConversorMoeda.paraBigDecimal(valor));
            mp.setFormaPagamento(formaPagamento);

            if (i != 0) {
                data = dataProximaParcela(parcelamento, data);
            }

            mp.setDataVencimento(data);
            movimentos.add(mp);
        }
        adicionarOuAtualizarReferenteParcela(movimentos);
        return movimentos;
    }

    public void adicionarOuAtualizarReferenteParcela(List<MovimentoPagamento> movimentos){
        int quantidade = movimentos.size();
        for(int i = 0; i < quantidade ; i++){
            if(quantidade == 1){
                movimentos.get(0).setReferenteParcela("UNICA");
                break;
            }
            movimentos.get(i).setReferenteParcela(i+1 +"/" + quantidade);
        }
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
        MovimentoPagamento mp = movimentos.get(linha);
        mp.setDataVencimento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 2))));
        mp.setValorPagamento(ConversorMoeda.paraBigDecimal(String.valueOf(model.getValueAt(linha, 3))));
        mp.setDataPagamento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 4))));
        mp.setFormaPagamento(formaPagamentoService.getByForma(String.valueOf(model.getValueAt(linha,5))));
        mp.setObservacao((String)model.getValueAt(linha, 6));
        movimentosAtualizados.add(mp);
    }

    public List<MovimentoPagamento> deletarMovimentosTabela(List<MovimentoPagamento> movimentos, int[] linhas) {

        List<MovimentoPagamento> movimentosDeletados = new ArrayList<>();
        for (int i = linhas.length - 1; i >= 0; i--) {
            movimentosDeletados.add(movimentos.remove(linhas[i]));
        }

        adicionarOuAtualizarReferenteParcela(movimentos);
        return movimentosDeletados;
    }
}
