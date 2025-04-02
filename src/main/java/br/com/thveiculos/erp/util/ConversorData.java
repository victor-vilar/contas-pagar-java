/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author victor
 */
public abstract class ConversorData {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate paraData(String data) {
        
        if(data == null || data.equals("") || data.equals("null")){
            return null;
        }
       
        return LocalDate.parse(data.trim(), formatter);
    }

    public static String paraString(LocalDate data) {
        
        if(data == null){
            return null;
        }    
        return data.format(formatter);
    }
}
