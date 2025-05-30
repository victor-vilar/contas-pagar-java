/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe que converte um data do tipo {@link java.time.LocalDate} para em String
 * e de String para LocalDate.
 * Devido a diferença de zona as datas ficam no formato yyyy-MM-dd quando vem 
 * diretamente do LocalDate, essa classe ajuda a transformar a data em uma string
 * e alterar o seu formato para dd/MM/yyyy.
 * @author victor
 */
public abstract class ConversorData {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    
    /**
     * Converte uma data de String para LocalDate.
     * @param data string no formato dd/MM/yyyy
     * @return retorna nulo caso a String passada seja nula ou vazia. Retorna
     * um objeto do tipo LocalDate.
     */
    public static LocalDate paraData(String data) {

        if (data == null || data.equals("") || data.equals("null")) {
            return null;
        }
        
        if(TudoNumero(data)){
           data = buildData(data);
        }

        return LocalDate.parse(data.trim(), formatter);
    }
    
    
    /**
     * Converte uma data do tipo LocalDate para String.
     * @param data data do tipo LocalDate
     * @return nulo caso a data seja nula, retorna uma String no formato
     * dd/MM/yyyy.
     */
    public static String paraString(LocalDate data) {

        if (data == null) {
            return null;
        }
        return data.format(formatter);
    }

    
    /**
     * Confere se a String passada consegue ser convertida para número.
     * @param valor uma string com numeros
     * @return verdadeiro se o parametro conter somente números e falso
     * se houver outros caracteres.
     */
    private static boolean TudoNumero(String valor) {

        try {
            Double.valueOf(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static String buildData(String valor) {
        StringBuilder novaData = new StringBuilder();
        
        if (valor.length() == 8) {
            
            novaData.append(valor.charAt(0));
            novaData.append(valor.charAt(1));
            novaData.append("/");
            novaData.append(valor.charAt(2));
            novaData.append(valor.charAt(3));
            novaData.append("/");
            novaData.append(valor.charAt(4));
            novaData.append(valor.charAt(5));
            novaData.append(valor.charAt(6));
            novaData.append(valor.charAt(7));
            return novaData.toString();
        }
        

        if (valor.length() == 4) {
            novaData.append(valor.charAt(0));
            novaData.append(valor.charAt(1));
            novaData.append("/");
            novaData.append(valor.charAt(2));
            novaData.append(valor.charAt(3));
            novaData.append("/");
            novaData.append(LocalDate.now().getYear());
            return novaData.toString();
        }
        
        return valor;
    }

}
