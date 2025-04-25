package br.com.victorvilar.contaspagar.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DespesaAvulsaTest {

    public List<MovimentoPagamento> parcelas = new ArrayList<>();
    MovimentoPagamento p1;
    MovimentoPagamento p2;
    MovimentoPagamento p3;

    @BeforeEach
    public void setUp() {
        p1 = new MovimentoPagamento();
        p2 = new MovimentoPagamento();
        p3 = new MovimentoPagamento();
        p1.setValorPagamento(new BigDecimal("1000"));
        p2.setValorPagamento(new BigDecimal("1000"));
        p3.setValorPagamento(new BigDecimal("1000"));
        parcelas.add(p1);
        parcelas.add(p2);
        parcelas.add(p3);
    }

    @Test
    void getNotaFiscal() {
        DespesaAvulsa da = new DespesaAvulsa();
        da.setParcelas(parcelas);
        BigDecimal total = da.getValorTotal();
        assertEquals(total, BigDecimal.valueOf(3000));
    }
}