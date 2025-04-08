package br.com.thveiculos.erp.repositories.despesas;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.thveiculos.erp.entities.despesas.CategoriaDespesa;

@Repository
@Lazy
public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa,Long>{

    public CategoriaDespesa getByCategoria(String categoria);
}
