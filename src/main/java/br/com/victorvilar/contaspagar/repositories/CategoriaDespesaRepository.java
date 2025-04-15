package br.com.victorvilar.contaspagar.repositories;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorvilar.contaspagar.entities.CategoriaDespesa;

@Repository
@Lazy
public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa,Long>{

    public CategoriaDespesa getByCategoria(String categoria);
}
