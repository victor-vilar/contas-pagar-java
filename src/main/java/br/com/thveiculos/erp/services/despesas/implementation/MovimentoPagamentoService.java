/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.services.despesas.implementation;

import br.com.thveiculos.erp.util.ConversorMoeda;
import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.exceptions.despesas.QuantidadeDeParcelasException;
import br.com.thveiculos.erp.util.ConversorData;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author victor
 */
public class MovimentoPagamentoService {

    private List<MovimentoPagamento> movimentosDeletados = new ArrayList<>();

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
                novaData = dataAtual.plusWeeks(2);
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

    public void atualizarMovimentos(List<MovimentoPagamento> movimentos, int linha, DefaultTableModel model) {

        //Busca o movimento na lista. O movimento se encontra na mesma posição da linha
        MovimentoPagamento mp = movimentos.get(linha);

        //Atualiza a data de vencimento 
        mp.setDataVencimento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 2))));

        //Atualiza o valor de pagamento            
        mp.setValorPagamento(ConversorMoeda.paraBigDecimal(String.valueOf(model.getValueAt(linha, 3))));

        //Atualiza da data de pagmento
        mp.setDataPagamento(ConversorData.paraData(String.valueOf(model.getValueAt(linha, 4))));

    }

    /**
     * Deleta os movimentos da lista, de acordo com as linhas deletadas da
     * tabela. Atualiza a propriedade referenteParcela do movimento .
     */
    public void deletarMovimentos(List<MovimentoPagamento> movimentos, int[] linhas) {

        //deleta os movimentos da lista que estão nas mesma posiçaõ das linhas
        //adiciona movimentos deletados na lista para que possam ser excluidos
        //caso já tenham sido salvos no banco.
        //O código começa a eliminar a partir do fim da fila, pois os elementos
        //mudam de posição quando são removidos
        for (int i = linhas.length - 1; i >= 0; i--) {
            System.out.println(linhas[i]);
            movimentosDeletados.add(movimentos.remove(linhas[i]));
        }

        //reorganiza a propriedade referencia parcela dos movimentos.
        int tamanho = movimentos.size();
        for (int i = 0; i < tamanho; i++) {
            movimentos.get(i).setReferenteParcela(i + 1 + "/" + tamanho);
        }

    }

}
