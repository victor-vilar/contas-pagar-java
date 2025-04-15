/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author victor
 */
public abstract class ConversorData {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate paraData(String data) {

        if (data == null || data.equals("") || data.equals("null")) {
            return null;
        }
        
        if(TudoNumero(data)){
           data = buildData(data);
        }

        return LocalDate.parse(data.trim(), formatter);
    }

    public static String paraString(LocalDate data) {

        if (data == null) {
            return null;
        }
        return data.format(formatter);
    }

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
