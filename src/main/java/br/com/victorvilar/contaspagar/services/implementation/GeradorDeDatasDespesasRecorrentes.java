package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.entities.DespesaRecorrente;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.enums.Periodo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
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
        int diaPagamento = despesa.getDiaPagamento();
        int mesPagamento = despesa.getMesPagamento() != null ? despesa.getMesPagamento():0;
        LocalDate ultimoLancamento = despesa.getDataUltimoLancamento();
        Periodo periodo = despesa.getPeriocidade();
        return switch(periodo){
            case ANUAL -> gerarVencimentoAnual(diaPagamento,mesPagamento,ultimoLancamento);
            case MENSAL -> gerarVencimentoMensal(diaPagamento,ultimoLancamento);
            case QUINZENAL -> gerarVencimentoQuinzenal(diaPagamento,ultimoLancamento);
            case SEMANAL -> gerarVencimentoSemanal(diaPagamento,mesPagamento,ultimoLancamento);
            default -> null;
        };
    }

    /**
     * Cria data de vencimento para contas anuais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um mês e dia anteriores ao que é programado, irá criar uma nova data para o mesmo ano.
     * Caso exista uma data de 'ultimoLancamento' ele ira pegar essa data e adicionará 1 ano.
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoAnual(int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){

        int diaAtual = dataHoje().getDayOfMonth();
        int mesAtual = dataHoje().getMonthValue();
        int anoAtual = dataHoje().getYear();

        if(ultimoLancamento != null){
            return ultimoLancamento.plusYears(1);
        }

        if(mesAtual <= mesPagamento && diaAtual <= diaPagamento ){
            return LocalDate.of(anoAtual,mesPagamento,diaPagamento);
        }

        return LocalDate.of(anoAtual + 1,mesPagamento,diaPagamento);

    }
    /**
     * Cria data de vencimento para contas mensais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um dia anterior ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 mês.
     * @param diaPagamento dia de pagamento pre definido
     * @param ultimoLancamento  data do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoMensal(int diaPagamento, LocalDate ultimoLancamento){

        int diaAtual = dataHoje().getDayOfMonth();
        Month mesAtual = dataHoje().getMonth();
        int anoAtual = dataHoje().getYear();

        //se o ultimo lançamento não for nulo, ira lançar para a data do proximo mês
        if(ultimoLancamento != null){
            return ultimoLancamento.plusMonths(1);
        }

        //se o dia atual for menor que o dia programado para pagamento
        if(diaAtual < diaPagamento ){
            return LocalDate.of(anoAtual,mesAtual,diaPagamento);
        }

        //se o dia atual for maior que o dia programado para pagamento
        //se o mes for igual a dezembro, ira aumentar o ano.
        //se o ano aumentou, o mes agora é janeiro se não aumenta o mes
        int ano = mesAtual == Month.DECEMBER ? anoAtual + 1: anoAtual;
        Month month = ano > anoAtual ? Month.JANUARY : mesAtual.plus(1);
        return LocalDate.of(ano, month, diaPagamento);

    }


    /**
     * Cria data de vencimento para contas quinzenais. No app as despesas recorrentes que são quinzenais não podem
     * cadastrar os seus dias de pagamentos para depois do dia 14, garantindo assim que as duas parcelas vão estar
     * dentro do mesmo mês.
     * @param diaPagamento dia de pagamento pre programado
     * @param ultimoLancamento  data de vencimento do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoQuinzenal(int diaPagamento, LocalDate ultimoLancamento){
        int diaAtual = dataHoje().getDayOfMonth();
        Month mesAtual = dataHoje().getMonth();
        int anoAtual = dataHoje().getYear();

        //Dia do ultimo lançamento
        int diaDoUltimoLancamento = ultimoLancamento != null ? ultimoLancamento.getDayOfMonth():0;

        //Mês do último Lancamento
        Month mesUltimoLancamento = ultimoLancamento != null ? ultimoLancamento.getMonth():Month.JANUARY;

        //Dia do mês que marca o último dia da primeira quinzena.
        int diaLimitePrimeiraQuinzena = 14;

        //se o último lançamento não for nulo e a data foi antes do dia 14, ira criar uma data para duas semanas depois
        if(ultimoLancamento != null && diaDoUltimoLancamento <= diaLimitePrimeiraQuinzena){
            return ultimoLancamento.plusWeeks(2);
        }

        //Se o último lançamento não for de nulo e o dia do último pagamento for maior que 14(Data Limite Primeira Quinzena)
        //ira gerar a data para o mês seguinda ao mês do último lançamento. Se o mês seguinte for 'dezembro'
        //ira vi retornar com mês de 'janeiro'.
        if(ultimoLancamento != null && diaDoUltimoLancamento >= diaLimitePrimeiraQuinzena){
            return mesUltimoLancamento != Month.DECEMBER ?
                    LocalDate.of(anoAtual, mesUltimoLancamento.plus(1),diaPagamento):
                    LocalDate.of(anoAtual + 1, Month.JANUARY, diaPagamento);

        }

        //Se o último lançamento for nulo e o dia atual for maior que 14
        //ira gerar a data para o mês seguinte ao mês atual. Se o mês seguinte for 'dezembro'
        //ira retornar com o mês de 'janeiro'.
        if (ultimoLancamento == null && diaAtual >= diaLimitePrimeiraQuinzena) {
            return mesAtual != Month.DECEMBER ?
                    LocalDate.of(anoAtual, mesAtual.plus(1),diaPagamento):
                    LocalDate.of(anoAtual + 1, Month.JANUARY, diaPagamento);

        }

        //se o último lançamento for nulo e o dia de atual é maior que o dia programado e ainda é menor que a dataLimite
        // ira retornar uma data para duas semanas depois do dia de pagamento.
        if(diaAtual >= diaPagamento && diaAtual <= diaLimitePrimeiraQuinzena){
            return LocalDate.of(anoAtual,mesAtual,diaPagamento).plusWeeks(2);
        }

        //se o último lançamento for nulo, e dia atual for menor que o dia programado
        //ira criar uma data no mês atual no dia programado.
        return LocalDate.of(anoAtual,mesAtual,diaPagamento);



    }

    /**
     * Cria data de vencimento para contas semanais. Caso não exista uma data de 'ultimoLancamento' e ainda estivermos
     * em um dia anterior ao que é programado, irá criar uma nova data. Caso exista uma data de 'ultimoLancamento'
     * ele ira pegar essa data e adicionará 1 mês.
     * @param diaPagamento dia de pagamento pre definido
     * @param mesPagamento mes de pagamento pre definido
     * @param ultimoLancamento  data de vencimento do ultimo movimento
     * @return data do proximo lançamento.
     */
    public LocalDate gerarVencimentoSemanal(int diaPagamento, int mesPagamento, LocalDate ultimoLancamento){
        //TODO
        return null;
    }


    /**
     * Apos ter gerado uma data para o vencimento das despesas, tem que ser gerado uma data 'dataProximoLancamento' para
     * determinar quando o sistema deve novamente gera outro movimento.
     * Exemplo: Uma despesa recorrente 'MENSAL' que é paga todo dia 10, precisa estar no movimento de pagamento antes desse dia.
     * O que determina então quando esse movimento deve ser gerador é a propriedade 'dataProximoLancamento' de {@link DespesaRecorrente}.
     * Apos então ser determinada uma data de vencimento, é possivel então criar uma 'dataProxmoLancamento' para a despesa.
     */
    public LocalDate gerarDataDoProximoLancamento(DespesaRecorrente despesa){
        LocalDate dataUltimoLancamento = despesa.getDataUltimoLancamento();
        return switch(despesa.getPeriocidade()){
            case ANUAL -> gerarDataProximoLancamentoDespesaAnual(dataUltimoLancamento);
            case MENSAL -> gerarDataProximoLancamentoDespesaMensal(dataUltimoLancamento);
            case QUINZENAL -> gerarDataProximoLancamentoDespesaQuinzenal(dataUltimoLancamento);
            case SEMANAL -> gerarDataProximoLancamentoDespesaSemanal(dataUltimoLancamento);
            default -> null;
        };
    }

    /**
     * Gerar a proxima data de lancamento, sendo onze meses após a data do ultimo movimento lançado.
     * @param dataUltimoLancamento A data do ultimo movimento lançado para essa despesa recorrente.
     * @return data do proximo dia que o sistema deverá lançar um movimento para essa despesa recorrente
     */
    public LocalDate gerarDataProximoLancamentoDespesaAnual(LocalDate dataUltimoLancamento){
        return dataUltimoLancamento.plusMonths(11);
    }

    /**
     * Gerar a proxima data de lancamento, sendo 15 dias após a data do ultimo movimento lançado.
     * @param dataUltimoLancamento A data do ultimo movimento lançado para essa despesa recorrente.
     * @return data do proximo dia que o sistema deverá lançar um movimento para essa despesa recorrente
     */
    public LocalDate gerarDataProximoLancamentoDespesaMensal(LocalDate dataUltimoLancamento){
        return dataUltimoLancamento.plusDays(15);
    }

    /**
     * Gerar a proxima data de lancamento, sendo uma semana após a data do ultimo movimento lançado.
     * @param dataUltimoLancamento A data do ultimo movimento lançado para essa despesa recorrente.
     * @return data do proximo dia que o sistema deverá lançar um movimento para essa despesa recorrente
     */
    public LocalDate gerarDataProximoLancamentoDespesaQuinzenal(LocalDate dataUltimoLancamento){
        return dataUltimoLancamento.plusWeeks(1);
    }

    /**
     * Gerar a proxima data de lancamento, sendo 4 dias após a data do ultimo movimento lançado.
     * @param dataUltimoLancamento A data do ultimo movimento lançado para essa despesa recorrente.
     * @return data do proximo dia que o sistema deverá lançar um movimento para essa despesa recorrente
     */
    public LocalDate gerarDataProximoLancamentoDespesaSemanal(LocalDate dataUltimoLancamento){
        return dataUltimoLancamento.plusDays(4);
    }



}
