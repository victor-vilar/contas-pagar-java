package br.com.thveiculos.erp.entities;

import java.math.BigInteger;
import java.util.List;

public interface Veiculo {
	
	//tabela fipe
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
