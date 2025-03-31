package br.com.thveiculos.erp.enums.despesas;

public enum Periodo {

	ANUAL("ANUAL"),
	MENSAL("MENSAL"),
        QUINZENAL("QUINZENAL"),
	SEMANAL("SEMANAL"),
        DIARIO("DIARIO");
	
	private String descricao;
	
	private Periodo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getPeriodo() {
		return descricao;
	}
}
