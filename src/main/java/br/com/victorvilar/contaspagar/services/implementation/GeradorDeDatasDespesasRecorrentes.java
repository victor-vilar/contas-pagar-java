package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;

import java.time.LocalDate;

public class GeradorDeDatasDespesasRecorrentes {

    /**
     * Método criado para facilitar a criação de testes;
     * @return Data de hoje;
     */
    public LocalDate dataHoje(){
        return LocalDate.now();
    }

    /**
     * Cria a data de vencimento do proximo {@link MovimentoPagamento} de uma {@link DespesaRecorrente} de acordo
     * com a sua periocidade.
     * @param despesa
     * @return
     */
    public LocalDate criarDataVencimento(DespesaRecorrente despesa){
        int diaAtual = dataHoje().getDayOfMonth();
        int mesAtual = dataHoje().getMonthValue();
        int diaPagamento = despesa.getDiaPagamento();
        int mesPagamento = despesa.getMesPagamento();
        LocalDate ultimoLancamento = despesa.getDataUltimoLancamento();
        Periodo periodo = despesa.getPeriocidade();
        return switch(periodo){
            case ANUAL -> gerarVencimentoAnual(diaAtual,mesAtual,diaPagamento,mesPagamento,ultimoLancamento);
            case MENSAL -> gerarVencimentoMensal(diaAtual,mesAtual,diaPagamento,mesPagamento,ultimoLancamento);
            case QUINZENAL -> gerarVencimentoQuinzenal(diaAtual,mesAtual,diaPagamento,mesPagamento,ultimoLancamento);
            case SEMANAL -> gerarVencimentoQuinzenal(diaAtual,mesAtual,diaPagamento,mesPagamento,ultimoLancamento);
            default -> null;
        };
    }

    /**
     * Cria data de vencimento para contas anuais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um mês e dia anteriores ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 ano.
     * @param diaAtual dia atual
     * @param mesAtual mês atual
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoAnual(int diaAtual, int mesAtual, int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){
        if((ultimoLancamento == null) && mesAtual < mesPagamento && diaAtual < diaPagamento ){
            return LocalDate.of(diaPagamento,mesPagamento,dataHoje().getYear());
        }
        return ultimoLancamento.plusYears(1);
    }
    /**
     * Cria data de vencimento para contas mensais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um dia anterior ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 mês.
     * @param diaAtual dia atual
     * @param mesAtual mês atual
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoMensal(int diaAtual, int mesAtual, int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){
        if((ultimoLancamento == null) &&  diaAtual < diaPagamento ){
            return LocalDate.of(diaPagamento,dataHoje().getMonthValue(),dataHoje().getYear());
        }
        return ultimoLancamento.plusMonths(1);
    }


    /**
     * Cria data de vencimento para contas mensais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um dia anterior ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 mês.
     * @param diaAtual dia atual
     * @param mesAtual mês atual
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoQuinzenal(int diaAtual, int mesAtual, int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){
        //TODO
    }

    /**
     * Cria data de vencimento para contas mensais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um dia anterior ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 mês.
     * @param diaAtual dia atual
     * @param mesAtual mês atual
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoSemanal(int diaAtual, int mesAtual, int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){
        //TODO
    }

}
