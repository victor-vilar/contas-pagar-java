package br.com.thveiculos.erp.repositories.despesas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thveiculos.erp.entities.despesas.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento,Long>{
    
    public FormaPagamento getByForma(String forma);

}
