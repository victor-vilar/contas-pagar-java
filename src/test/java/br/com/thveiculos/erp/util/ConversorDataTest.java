/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.thveiculos.erp.util;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author victor
 */
public class ConversorDataTest {

    /**
     * Test of paraData method, of class ConversorData.
     */
    @Test
    public void testParaData() {
        String dataString = "01/04/2025";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }

    @Test
    public void retornaNullQuandoADataForNula() {

        LocalDate dataConvertida = ConversorData.paraData(null);
        assertEquals(dataConvertida, null);
    }

    @Test
    public void retornaNullQuandoADataForStringVazia() {

        LocalDate dataConvertida = ConversorData.paraData("");
        assertEquals(dataConvertida, null);
    }

    /**
     * Test of paraString method, of class ConversorData.
     */
    @Test
    public void testParaString() {
        String dataString = "01/04/2025";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        String dataConvertida = ConversorData.paraString(dataLocal);
        assertEquals(dataString, dataConvertida);
    }

}
