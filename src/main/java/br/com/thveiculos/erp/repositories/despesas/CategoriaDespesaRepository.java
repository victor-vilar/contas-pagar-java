package br.com.thveiculos.erp.repositories.despesas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;

@Repository
public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa,Long>{

}
