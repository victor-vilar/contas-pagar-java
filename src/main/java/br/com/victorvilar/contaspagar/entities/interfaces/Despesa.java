package br.com.victorvilar.contaspagar.entities.interfaces;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import br.com.victorvilar.contaspagar.enums.DespesaTipo;

import java.math.BigDecimal;

public interface Despesa {

	
	public CategoriaDespesa getCategoria();
	public String getNome();
	public String getDescricao();
	public boolean isQuitado();
	public BigDecimal getValorTotal();
	public int getQuantidadeParcelas();
	public DespesaTipo getTipo();
}
