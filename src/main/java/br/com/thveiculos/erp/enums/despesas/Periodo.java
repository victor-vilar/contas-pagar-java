package br.com.thveiculos.erp.enums.despesas;

public enum Periodo {

	ANUAL("anual"),
	MENSAL("mensal"),
	SEMANAL("semanal");
	
	private String descricao;
	
	private Periodo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getPeriodo() {
		return descricao;
	}
}
