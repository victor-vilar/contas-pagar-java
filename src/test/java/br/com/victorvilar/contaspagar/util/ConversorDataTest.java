/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.util;

import br.com.victorvilar.contaspagar.util.ConversorData;
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

    
    @Test
    public void retornaDataCompletaSeForPassadoApenasOsNumeros(){
        String dataString = "01042025";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        String dataConvertida = ConversorData.paraString(dataLocal);
        assertEquals("01/04/2025", dataConvertida);
    
    }
    
    @Test
    public void retornaDataCompletaSeForPassadoApenasOsNumerosContendoSomenteDiaEMes(){
        String dataString = "3112";
        LocalDate dataLocal = LocalDate.of(2025, 12, 31);
        String dataConvertida = ConversorData.paraString(dataLocal);
        assertEquals("31/12/2025", dataConvertida);
    
    }
}
