/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.services.implementation.MovimentoPagamentoServiceImpl;
import br.com.victorvilar.contaspagar.entities.FormaPagamento;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import br.com.victorvilar.contaspagar.exceptions.QuantidadeDeParcelasException;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import br.com.victorvilar.contaspagar.util.ConversorData;
import br.com.victorvilar.contaspagar.util.ConversorMoeda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author victor
 */
@ExtendWith(MockitoExtension.class)
public class MovimentoPagamentoServiceTest {

    @Spy
    @InjectMocks
    MovimentoPagamentoServiceImpl service;
    
    @Mock
    MovimentoPagamentoRepository repository;

    List<MovimentoPagamento> movimentos = new ArrayList<>();
    MovimentoPagamento mp1;
    MovimentoPagamento mp2;
    MovimentoPagamento mp3;
    FormaPagamento fp1;
    FormaPagamento fp2;

    @BeforeEach
    public void setUp() {
        movimentos.clear();
        mp1 = new MovimentoPagamento();
        mp2 = new MovimentoPagamento();
        mp3 = new MovimentoPagamento();

        mp1.setId(1L);
        mp1.setReferenteParcela("1/5");
        mp1.setDataVencimento(ConversorData.paraData("0104"));
        mp1.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp1.setDataPagamento(ConversorData.paraData("0104"));

        mp2.setId(2L);
        mp2.setReferenteParcela("1/5");
        mp2.setDataVencimento(ConversorData.paraData("0104"));
        mp2.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp2.setDataPagamento(ConversorData.paraData("0104"));

        mp3.setId(2L);
        mp3.setReferenteParcela("1/5");
        mp3.setDataVencimento(ConversorData.paraData("0104"));
        mp3.setValorPagamento(ConversorMoeda.paraBigDecimal("1000"));
        mp3.setDataPagamento(ConversorData.paraData("0104"));

        fp1 = new FormaPagamento();
        fp1.setForma("BOLETO BANCÁRIO");
        fp2 = new FormaPagamento();
        fp2.setForma("CARTÃO");
    }



    @Test
    @DisplayName("metodo atulizar deve atualizar todos as propriedades do objeto passado")
    public void updateDeveAtualizarPropriedadesDoObjeto(){

        when(repository.findById(mp1.getId())).thenReturn(Optional.of(mp1));
        when(repository.save(any(MovimentoPagamento.class))).thenAnswer(c -> c.getArgument(0));
        MovimentoPagamento mp = service.update(mp1);

        assertEquals(mp.getId(),mp1.getId());
        assertEquals(mp.getDataPagamento(),mp1.getDataPagamento());
        assertEquals(mp.getDataVencimento(),mp1.getDataVencimento());
        assertEquals(mp.getFormaPagamento(),mp1.getFormaPagamento());
        assertEquals(mp.getReferenteParcela(),mp1.getReferenteParcela());
        assertEquals(mp.getValorPagamento(),mp1.getValorPagamento());
        assertEquals(mp.getValorPago(),mp1.getValorPago());
        assertEquals(mp.getObservacao(),mp1.getObservacao());


    }

    @Test
    @DisplayName("deve chamr o metodo update para cada item da lista")
    public void updateDeveAtualizarListaDeObjetos() {
        when(repository.findById(mp1.getId())).thenReturn(Optional.of(mp1));
        when(repository.findById(mp2.getId())).thenReturn(Optional.of(mp2));
        when(repository.findById(mp3.getId())).thenReturn(Optional.of(mp3));
        when(repository.save(any(MovimentoPagamento.class))).thenAnswer(c -> c.getArgument(0));
        movimentos.add(mp1);
        movimentos.add(mp2);
        movimentos.add(mp3);

        List<MovimentoPagamento> movimentosAtualizados = service.update(movimentos);

        assertNotNull(movimentosAtualizados);
        assertFalse(movimentos.isEmpty());
        assertEquals(movimentos.size(),3);


    }



}
