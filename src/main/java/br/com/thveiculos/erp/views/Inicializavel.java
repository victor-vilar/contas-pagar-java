package br.com.thveiculos.erp.views;

/** 
 * Os formulários da aplicação estarão no contexto do spring, dessa forma para que eu possa iniciar os formulários da maneira que eu gostaria
 * eu tenho que ter um metodo que sera chamado para que quando o formulário executar o seu método setVible(true), ele já esteja no estado que 
 * eu gostaria que ele estivesse.
 * 
 * Esse estado pode ser, uma tabela preenchida com dados do banco de dados por exemplo por exemplo. Qualquer formulário que se encontra no contexto,
 * deverá implementar essa interface e definir o que será executado antes dele.
 * 
 * */
public interface Inicializavel {
	
	public void refresh();

}
