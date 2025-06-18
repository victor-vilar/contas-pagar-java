/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.util;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

        dataString = "10/06/2025";
        dataLocal = LocalDate.of(2025, 06, 10);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "20/06/2025";
        dataLocal = LocalDate.of(2025, 06, 20);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSimplesBarra(){
        String dataString = "01/04";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "20/04";
        dataLocal = LocalDate.of(2025, 04, 20);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30/04";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSuperSimplesBarra(){
        String dataString = "1/4";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "20/4";
        dataLocal = LocalDate.of(2025, 04, 20);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30/4";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSimplesTraco(){
        String dataString = "01-04";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "10-04";
        dataLocal = LocalDate.of(2025, 04, 10);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "25-04";
        dataLocal = LocalDate.of(2025, 04, 25);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30-04";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSuperSimplesTraco(){
        String dataString = "1-4";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "10-4";
        dataLocal = LocalDate.of(2025, 04, 10);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "25-4";
        dataLocal = LocalDate.of(2025, 04, 25);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30-4";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSimplesPonto(){
        String dataString = "01.04";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "10.04";
        dataLocal = LocalDate.of(2025, 04, 10);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "25.04";
        dataLocal = LocalDate.of(2025, 04, 25);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30.04";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);
    }
    
    @Test
    public void paraDataQuandoNaoEPassadoOAnoComDataSuperSimplesPonto(){
        String dataString = "1.4";
        LocalDate dataLocal = LocalDate.of(2025, 04, 01);
        LocalDate dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "10.4";
        dataLocal = LocalDate.of(2025, 04, 10);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "25.4";
        dataLocal = LocalDate.of(2025, 04, 25);
        dataConvertida = ConversorData.paraData(dataString);
        assertEquals(dataLocal, dataConvertida);

        dataString = "30.4";
        dataLocal = LocalDate.of(2025, 04, 30);
        dataConvertida = ConversorData.paraData(dataString);
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

    @Test
    public void transformaDataSemZeroAEsquerda(){
    
        String dia1 = "1";
        String dia2 = "2";
        String dia3 = "3";
        String dia4 = "4";
        String dia5 = "5";
        
        String diaTransormado1 = ConversorData.adicionarZeroAEsquerda(dia1);
        String diaTransormado2 = ConversorData.adicionarZeroAEsquerda(dia2);
        String diaTransormado3 = ConversorData.adicionarZeroAEsquerda(dia3);
        String diaTransormado4 = ConversorData.adicionarZeroAEsquerda(dia4);
        String diaTransormado5 = ConversorData.adicionarZeroAEsquerda(dia5);
        
        assertEquals(diaTransormado1,"01");
        assertEquals(diaTransormado2,"02");
        assertEquals(diaTransormado3,"03");
        assertEquals(diaTransormado4,"04");
        assertEquals(diaTransormado5,"05");
        
        dia1 = "01";
        dia2 = "02";
        dia3 = "03";
        dia4 = "04";
        dia5 = "05";

        diaTransormado1 = ConversorData.adicionarZeroAEsquerda(dia1);
        diaTransormado2 = ConversorData.adicionarZeroAEsquerda(dia2);
        diaTransormado3 = ConversorData.adicionarZeroAEsquerda(dia3);
        diaTransormado4 = ConversorData.adicionarZeroAEsquerda(dia4);
        diaTransormado5 = ConversorData.adicionarZeroAEsquerda(dia5);

        assertEquals(diaTransormado1, "01");
        assertEquals(diaTransormado2, "02");
        assertEquals(diaTransormado3, "03");
        assertEquals(diaTransormado4, "04");
        assertEquals(diaTransormado5, "05");

    
    }
    
    @Test
    public void configurarAnoTeste(){
    
        String ano1 = "20";
        String ano2 = "21";
        String ano3 = "22";
        String ano4 = "23";
        String ano5 = "24";
        String ano6 = "25";
        
        String anoTransormado1 = ConversorData.configurarAno(ano1);
        String anoTransormado2 = ConversorData.configurarAno(ano2);
        String anoTransormado3 = ConversorData.configurarAno(ano3);
        String anoTransormado4 = ConversorData.configurarAno(ano4);
        String anoTransormado5 = ConversorData.configurarAno(ano5);
        String anoTransormado6 = ConversorData.configurarAno(ano6);
        
        assertEquals(anoTransormado1, "2020");
        assertEquals(anoTransormado2, "2021");
        assertEquals(anoTransormado3, "2022");
        assertEquals(anoTransormado4, "2023");
        assertEquals(anoTransormado5, "2024");
        assertEquals(anoTransormado6, "2025");
        
    }

    
}
