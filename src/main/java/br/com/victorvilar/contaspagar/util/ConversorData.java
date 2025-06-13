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
        }else{
            
        }
        
        
        return LocalDate.parse(data.trim(), formatter);
    }
    
    /**
     * A classe aceita as datas sejam passadas interamente por números "01022025" será transformada na data "01/02/2025", ou ainda, "0102" será transformada
     * na data de "01/02/(mais o ano atual). Pode acontecer do usuário por já ter costume de utilizar os formatos que são aceitos por exemplo no excel
     * (onde uma string "01-02" ou "01/02" acaba virando uma data completa "01/02/2025"), acabe digitando os carecteres separadores de data como "/" ou "-".
     * 
     * Esse método tem a função de encontrar esses caracteres especiais e tentar transforma-los em somente números para que ele consiga então
     * realizar a sua conversão de String para Data.
     * @param valor
     * @return 
     */
    private static void descobrirFormato(String valor){
        if(valor.matches("^(?:[1-9]|1[0-9]|2[0-9]|30)[\\/-](?:[1-9]|1[0-2])$")){
            String[] dados = valor.split("");
            
        }
        
    }
    
    /**
     * Converte para uma String de números que foram passadas no formato "01-02" ou "01/02" ou ainda "1-2" ou "1/2" onde os números que são
     * menores que 10 não possuem o zero a esquerda para uma String de quatro caracteres, que poderá ser convertida no método buildData.
     * @param valor um array de caracteres
     * @return Retorna uma String de números no formato "0102" por exemplo.
     */
    public static String formatarDatasSemAno(String[] valor){

        int tamanhoLista = valor.length;

        if (tamanhoLista == 5) {
            return formatarDatasSemAnoQuemContemTresCaracteres(valor, tamanhoLista);

        }

        if (tamanhoLista == 3) {
            return formataQuandoForPassadoTresCaracteres(valor, tamanhoLista);
        }

        return " ";
    }
       /**
        * converte uma string "01/02" ou "01-02" em "0102"
        * @param valor valores separados em uma lista
        * @param tamanhoLista tamanho da lista
        * @return String com 
        */
    public static String formatarDatasSemAnoQuemContemTresCaracteres(String[] valor, int tamanhoLista) {
        String data = "";
        for (int i = 0; i < tamanhoLista; i++) {
            if (valor[i] != "/" && valor[i] != "-") {
                data = data + valor[i];
            }
        }
        return data;
    }
    
          /**
        * converte uma string "1/2" ou "1-2" em "0102"
        * @param valor valores separados em uma lista
        * @param tamanhoLista tamanho da lista
        * @return String com 
        */
    public static String formataQuandoForPassadoTresCaracteres(String[] valor, int tamanhoLista) {
        String data = "";
        for (int i = 0; i < tamanhoLista; i++) {
            if (valor[i] != "/" && valor[i] != "-") {
                if ((i == 0 || i == 2) && Integer.valueOf(valor[i]) < 10) {
                    data = data + "0" + valor[i];
                } else {
                    data = data + valor[i];
                }
            }
        }
        return data;
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
