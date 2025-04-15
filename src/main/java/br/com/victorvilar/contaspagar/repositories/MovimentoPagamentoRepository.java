package br.com.victorvilar.contaspagar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import java.util.List;

public interface MovimentoPagamentoRepository extends JpaRepository<MovimentoPagamento, Long> {

    List<MovimentoPagamento> findByDataPagamentoIsNull();
    List<MovimentoPagamento> findByDataPagamentoIsNotNull();
}
