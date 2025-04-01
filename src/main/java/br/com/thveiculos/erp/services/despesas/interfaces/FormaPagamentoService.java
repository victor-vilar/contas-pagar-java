package br.com.thveiculos.erp.services.despesas.interfaces;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;

public interface FormaPagamentoService extends AppService<FormaPagamento>{

    public FormaPagamento getByForma(String forma);

}
