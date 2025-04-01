/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.services.despesas.implementation;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;
import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import br.com.thveiculos.erp.exceptions.despesas.QuantidadeDeParcelasException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author victor
 */

public class GeradorMovimentos {
    
    
   
    public  List<MovimentoPagamento> gerarMovimentos(String parcelamento,int qtdParcelas, String dataInicial, String valor, FormaPagamento formaPagamento)
            throws QuantidadeDeParcelasException, DateTimeParseException{
        
        if(qtdParcelas <= 0){
            throw new QuantidadeDeParcelasException();
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(dataInicial,formatter);
        List<MovimentoPagamento> movimentos = new ArrayList<>();
        
        //Se a data passada for dia 31, ira configurar para o dia 30
        if(data.getDayOfMonth() == 31){
            data = data.minusDays(1);
        }
        

        
        
        for(int i = 0; i < qtdParcelas; i++){
        
                MovimentoPagamento mp = new MovimentoPagamento();                
                mp.setValorPagamento(new BigDecimal(valor));
                mp.setFormaPagamento(formaPagamento);
                
                
                if(qtdParcelas == 1){
                    mp.setReferenteParcela("UNICA");
                }else{
                    mp.setReferenteParcela(i + 1 + "/" + qtdParcelas);
 
                }
                
                
                if(i != 0){
                    data = dataProximaParcela(parcelamento,data);
                }
                
                mp.setDataVencimento(data);
                movimentos.add(mp);
        }
        
        
        
        return movimentos;
    }
    
    private  LocalDate dataProximaParcela(String parcelamento, LocalDate dataAtual){
    
        LocalDate novaData;
        
        switch(parcelamento){
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
    
    
    public void atualizarMovimentos(List<MovimentoPagamento> movimentos, Set<Integer> linhas, DefaultTableModel model){
    
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(Integer i : linhas){
            
            //Busca o movimento na lista. O movimento se encontra na mesma posição da linha
            MovimentoPagamento mp = movimentos.get(i);
            
            //Atualiza a data de vencimento 
            mp.setDataVencimento(LocalDate.parse(model.getValueAt(i, 2).toString(),formatter));
            
            
            //Atualiza o valor de pagamento
            mp.setValorPagamento(new BigDecimal(String.valueOf(model.getValueAt(i,3))));
            
            
            //Se existir uma data de pagamento ela será atualizada
            if(!model.getValueAt(i,4).toString().equals("")){
                mp.setDataPagamento(LocalDate.parse(model.getValueAt(i,4).toString(),formatter));
            }
            
             
        }
    }
    
}
