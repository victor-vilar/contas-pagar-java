package br.com.thveiculos.erp.repositories.despesas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thveiculos.erp.entities.despesas.MovimentoPagamento;
import java.util.List;

public interface MovimentoPagamentoRepository extends JpaRepository<MovimentoPagamento, Long> {

    List<MovimentoPagamento> findByDataPagamentoIsNull();
    List<MovimentoPagamento> findByDataPagamentoIsNotNull();
}
