/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.thveiculos.erp.controllers.despesas;

import br.com.thveiculos.erp.controllers.AppViewController;
import br.com.thveiculos.erp.entities.despesas.DespesaRecorrente;
import br.com.thveiculos.erp.services.despesas.interfaces.CategoriaDespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.DespesaService;
import br.com.thveiculos.erp.services.despesas.interfaces.FormaPagamentoService;
import br.com.thveiculos.erp.views.despesas.DespesaRecorrenteViewImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author victor
 */
@Controller
@Lazy
public class DespesaRecorrenteController extends DespesaAbstractController<DespesaRecorrenteViewImpl>{

    @Autowired
    public DespesaRecorrenteController(DespesaService service, CategoriaDespesaService categoriaDespesaService, FormaPagamentoService formaPagamentoService) {
        super(service, categoriaDespesaService, formaPagamentoService);
    }
    
    @Override
    public void salvar() {
      DespesaRecorrente despesa = new DespesaRecorrente();
    }


    
}
