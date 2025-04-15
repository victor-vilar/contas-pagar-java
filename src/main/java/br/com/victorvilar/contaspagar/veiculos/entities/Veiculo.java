package br.com.victorvilar.contaspagar.veiculos.entities;

import java.math.BigInteger;
import java.util.List;


/** 
 * Interface para representar os veículos que estão envolvidos nos processos de compra e venda
 * */
public interface Veiculo {
	
	//tabela fipe
	public Long getId();
	public String getCodigoFipe();
	public String getNome();
	public String getMarca();
	public String getAno();
	public String getTipoCombustivel();
	public BigInteger getPreco();
	//outros
	public String getPlaca();
	public String getChassi();
	public String getTipo();
	public List<Manutencao> getManutencoes();
	
	

}
