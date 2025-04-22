/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.controllers.interfaces;
import br.com.victorvilar.contaspagar.controllers.FinalizarMovimentoController;
import br.com.victorvilar.contaspagar.views.DespesaAvulsaViewImpl;
import br.com.victorvilar.contaspagar.views.DespesaRecorrenteViewImpl;
import br.com.victorvilar.contaspagar.views.MovimentoPagamentoView;
import br.com.victorvilar.contaspagar.views.FinalizarMovimentoPagamentoView;
/**
 * Interface para todos os controladores que manipulam formulários que realizam
 * operações CRUD.
 * 
 * Há controladores na aplicação que controlam formulários que realizam operações
 * CRUD no sistema, como os formulários {@link DespesaAvulsaViewImpl} e
 * {@link DespesaRecorrenteViewImpl}. Esses controladores devem implementar a interface
 * CrudViewController para que todos os métodos necessários sejam devidamente definidos.
 * 
 * Já outros formulários como {@link MovimentoPagamentoView} e {@link FinalizarMovimentoPagamentoView}
 * Não possuem todos os métodos necessários, sendo desnecessário que seus controladores
 * implementem todos os métodos de CrudViewController. Nesse caso esses controladores
 * devem implementar a interface {@link AppViewController} diretamente apenas para 
 * poder definir o seu formulário de manipulação e os metodos padrões de todo formulário.
 * Qualquer outro método que esse formulários precisarem, deverão ser definidos diretamente
 * no controller.
 * @author victor
 */
public interface CrudViewController<T> extends AppViewController<T> {
    void novo();
    void salvar();
    void editar();
    void deletar();
}
