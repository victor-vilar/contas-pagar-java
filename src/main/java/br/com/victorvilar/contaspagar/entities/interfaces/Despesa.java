package br.com.victorvilar.contaspagar.entities.interfaces;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;
import java.math.BigDecimal;

public interface Despesa {

	
	public CategoriaDespesa getCategoria();
	public String getNomeFornecedor();
	public String getDescricao();
	public boolean isQuitado();
	public BigDecimal getValorTotal();
	public int getQuantidadeParcelas();
	public String getTipo();
}
