package br.com.victorvilar.contaspagar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victorvilar.contaspagar.entities.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento,Long>{
    
    public FormaPagamento getByForma(String forma);

}
