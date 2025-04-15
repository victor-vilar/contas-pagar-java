package br.com.victorvilar.contaspagar.services.interfaces;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;

public interface FormaPagamentoService extends AppService<FormaPagamento>{

    public FormaPagamento getByForma(String forma);

}
