package br.com.victorvilar.contaspagar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;

public interface DespesaRepository extends JpaRepository<DespesaAbstrata, Long> {

}
