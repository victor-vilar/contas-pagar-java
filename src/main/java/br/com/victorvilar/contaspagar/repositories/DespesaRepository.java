package br.com.victorvilar.contaspagar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victorvilar.contaspagar.entities.DespesaAbstrata;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespesaRepository extends JpaRepository<DespesaAbstrata, Long> {

    @Query("SELECT d FROM DespesaAbstrata d LEFT JOIN FETCH d.movimentos WHERE d.id = :id")
    DespesaAbstrata findByIdWithMovimentos(@Param("id")Long id);
    List<DespesaAbstrata> findByTipo(String tipo);

    @Query("SELECT d FROM DespesaAbstrata d WHERE d.tipo = 'RECORRENTE' AND d.ativo=true AND  (d.dataProximoLancamento <= :dataAtual OR d.dataProximoLancamento IS NULL )")
    List<DespesaAbstrata> findDespesaRecorrenteWhereDataProximoLancamentoLowerThanNow(LocalDate dataAtual);
}
