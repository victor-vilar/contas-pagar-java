package br.com.thveiculos.erp.repositories.despesas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thveiculos.erp.entities.despesas.DespesaAbstrata;

public interface DespesaRepository extends JpaRepository<DespesaAbstrata, Long> {

}
