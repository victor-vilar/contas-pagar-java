package br.com.thveiculos.erp.repositories.veiculos;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thveiculos.erp.entities.veiculos.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long>{

}
