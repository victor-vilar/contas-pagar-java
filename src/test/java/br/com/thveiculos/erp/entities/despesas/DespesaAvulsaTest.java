package br.com.thveiculos.erp.entities.despesas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class DespesaAvulsaTest {
	
	public List<Parcela> parcelas = new ArrayList<>();
	Parcela p1;
	Parcela p2;
	Parcela p3;
	
	@BeforeEach
	public void setUp() {
		p1 = new Parcela();
		p2 = new Parcela();
		p3 = new Parcela();
		p1.setValorPagamento(new BigDecimal("1000"));
		p2.setValorPagamento(new BigDecimal("1000"));
		p3.setValorPagamento(new BigDecimal("1000"));
		parcelas.add(p1);
		parcelas.add(p2);
		parcelas.add(p3);
	}
	
	@Test
	@DisplayName("O método dessa classe tem que somar os valores das parcelas e trazer o total")
	public void getValorTotal() {
		DespesaAvulsa da = new DespesaAvulsa();
		da.setParcelas(parcelas);
		BigDecimal total = da.getValorTotal();
		assertEquals(total, BigDecimal.valueOf(3000));
		
	}
	
	@Test
	@DisplayName("O metodo não deve permitir que duas instancias iguais de uma parcela seja inserida na lista")
	public void naoPodeAdicionarMesmaInstancia() {
		DespesaAvulsa da = new DespesaAvulsa();
		da.addParcel(p1);
		da.addParcel(p1);
		assertTrue(da.getQuantidadeParcelas() == 1);
		
		da.addParcel(p2);
		assertTrue(da.getQuantidadeParcelas() == 2);
		
		da.addParcel(p2);
		assertTrue(da.getQuantidadeParcelas() != 3);
		
		da.addParcel(p3);
		assertTrue(da.getQuantidadeParcelas() == 3);
	}

}
