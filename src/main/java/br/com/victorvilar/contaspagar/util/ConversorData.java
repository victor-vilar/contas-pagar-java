/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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
        else{
           data = criarData(data);
           if(data == null){
               return null;
           }
           
        }
        
        
        return LocalDate.parse(buildData(data), formatter);
    }
    
 
    
    public static String criarData(String data){
        
        //separa os valores quando encontra uma barra '/' traço '-' ou ponto '.' 
        String[] partes = data.split("[/\\-.]");
        
        //aceita somente valores que são números
        partes = Arrays.stream(partes)
                       .filter(s -> s.matches("\\d+"))
                       .toArray(String[]::new);
        
        //se partes tiver vazio retorna nulo;
        if(partes.length == 0){
            return null;
        }
        //verifica se os valores estão de acordo
        String dia = adicionarZeroAEsquerda(partes[0]);
        String mes = adicionarZeroAEsquerda(partes[1]);
        String ano = "";
        
        //se o ano for passado
        if(partes.length == 3){
            ano = configurarAno(partes[2]);
        }
        
        //retorna a data completa
        return dia + mes + ano;
    }
    
    /**
     * Verifica se o valor passado é menor que 10. Esse metodo prepara a forma como o metodo buildData espera receber os valores para
     * formar uma data. Se um valor for passado como '5' ele irá transformar em '05'. Valores que já possuem o primeiro caracter zero ou maior que zero
     * não sofrem alterações. 
     * @param dia String de número
     * @return Retorna o proprio valor passado caso tenha sido passado com o zero antes do número ou transforma o valor para que ele fique
     * apropriado 
     */
    public static String adicionarZeroAEsquerda(String dia) throws NumberFormatException{
        int diaInteiro = Integer.parseInt(dia);
        if((diaInteiro < 10 && diaInteiro > 0) && !dia.substring(0,1).equals("0")){
            return "0" + dia;
        }
        return dia;
    }
    
    /**
     * Verifica se o valor do ano passado possui o prefixo '20', se o ano for passado apenas com dois caracteres por exemplo '25', esse método
     * ira transforma-lo em um ano 'completo' exemplo '2025'
     * @param ano
     * @return 
     */
    public static String configurarAno(String ano){
        
        if(ano.length() == 2){
            return LocalDate.now().toString().substring(0, 2) + ano;
        }
        return ano;
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
            Integer.valueOf(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }


    /**
     * Transforma a string passada que são somente números em uma data no padrão dd/MM/yyyy. Esse método recebe
     * os valores nos padrões "01012025" e o transforma em 01/01/2025 e caso o valor seja "0101" ele então
     * pegará o ano atual para completar a data.
     * @param valor String de números
     * @return retorna uma string no formato de data dd/MM/yyyy
     */
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
