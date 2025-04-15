/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *
 * @author victor
 */
public abstract class ConversorMoeda {

    private static NumberFormat formater = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    /**
     * Converte o texto que vai estar no formato 'R$1.000,00' para um bigDecimal.
     * @param moeda Representação da moeda em string;
     * @return BigDecimal do valor passado
     * @throws ParseException 
     */
    public static BigDecimal paraBigDecimal(String moeda) {

           moeda = moeda.replaceAll("[^0-9,]", "");
           moeda = moeda.replace(",",".");
           return new BigDecimal(moeda);
    }

    
    /**
     * Converte um bigDecimal para uma String no formato 'R$1.000,00'.
     * @param moeda
     * @return
     * @throws ParseException 
     */
    public static String paraString(BigDecimal moeda) {
        
        if(moeda == null){
            return null;
        }
        String texto = formater.format(moeda);
        return texto;
    }

}
