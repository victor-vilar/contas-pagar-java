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
        return LocalDate.parse(data, formatter);
    }

    public static String paraString(LocalDate data) {
        return data.format(formatter);
    }
}
