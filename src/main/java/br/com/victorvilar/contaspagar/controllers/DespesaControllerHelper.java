package br.com.victorvilar.contaspagar.controllers;

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


    @Autowired
    public DespesaControllerHelper(FormaPagamentoService formaPagamentoService){
        this.formaPagamentoService = formaPagamentoService;
    }

    /**
     * Gera uma lista de movimetnos de acordo com os dados passados
     * @param parcelamento tipo de parcelamento que a despesa possui {@link br.com.victorvilar.contaspagar.enums.Periodo}
     * @param qtdParcelas quantidade de parcelas que serão criadas
     * @param dataInicial data do vencimento da primeira parcela
     * @param valor valor de cada parcela
     * @param formaPagamento forma de pagamento de cada parcela
     * @return uma lista de movimentoPagamento
     * @throws QuantidadeDeParcelasException Lançada caso a quantidade de parcelas for igual ou menor que zero
     * @throws DateTimeParseException Lançada caso a data informada esteja incorreta ou não exista
     */
    public List<MovimentoPagamento> gerarMovimentos(String parcelamento, int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento)
            throws QuantidadeDeParcelasException, DateTimeParseException {

        if (qtdParcelas <= 0) {
            throw new QuantidadeDeParcelasException("A quantidade de parcelas não pode ser zero ou negativa");
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

        return movimentos;
    }


    /**
     * Cria uma data nova dependendo da forma de parcelamento passado
     * @param parcelamento tipo de parcelamento que a despesa possui
     * @param dataAtual data da ultima parcela registrada
     * @return nova data para a proxima parcela
     */
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

    /**
     * Atualiza um movimento na lista de movimento de acordo com os novos valores inseridos em uma linha de uma tabela
     * @param movimentos lista de movimentos
     * @param linha linha na tabela que sofreu alteração e que sera usado para buscar o indice
     * @param model model de uma tabela na view
     * @return movimento atualizado
     */
    public MovimentoPagamento atualizarMovimentosTabela(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model) {
        MovimentoPagamento mp = movimentos.get(linha);
        mp.setDataVencimento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 2))));
        mp.setValorPagamento(ConversorMoeda.paraBigDecimal(String.valueOf(model.getValueAt(linha, 3))));
        mp.setDataPagamento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 4))));
        mp.setFormaPagamento(formaPagamentoService.getByForma(String.valueOf(model.getValueAt(linha,5))));
        mp.setObservacao((String)model.getValueAt(linha, 6));
        return mp;
    }

    /**
     * Remove de uma lista de movimentos os itens que se encontram no mesmo indice de um elemento do array passado.
     * @param movimentos lista de movimentos
     * @param linhas indices que devem ser eliminados na lista.
     * @return lista de movimentos deletados.
     */
    public List<MovimentoPagamento> deletarMovimentosTabela(List<MovimentoPagamento> movimentos, int[] linhas) {

        List<MovimentoPagamento> movimentosDeletados = new ArrayList<>();
        for (int i = linhas.length - 1; i >= 0; i--) {
            movimentosDeletados.add(movimentos.remove(linhas[i]));
        }

        return movimentosDeletados;
    }
}
