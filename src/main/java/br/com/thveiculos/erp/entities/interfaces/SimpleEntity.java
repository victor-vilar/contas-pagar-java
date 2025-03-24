package br.com.thveiculos.erp.entities.interfaces;

/**
 * SimpleEntity são entidades que só possuem Id e Nome. Essas entidades são tratadas como ENUMS na aplicação.
 * Os formulários que manipulam essas entidades possuem as mesmas quantidade de campos, e funcionam da mesma maneira
 * sendo então desnecessário criar um código repetido para manipular os dados. As views dessas entidades são chamadas
 * {@link SimpleView}.
 * 
 * Tambem foi criada a classe {@link SimpleViewController} para controler a view desses formulários considerados
 * {@link SimpleView}.
 * 
 * Exemplos desse tipo são as classes {@link FormaPagamento} e {@link CategoriaDespesa}.
 */
public interface SimpleEntity {

	Long getId();
	String getName();
}
