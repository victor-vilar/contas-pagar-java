package br.com.thveiculos.erp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thveiculos.erp.entities.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long>{

}
