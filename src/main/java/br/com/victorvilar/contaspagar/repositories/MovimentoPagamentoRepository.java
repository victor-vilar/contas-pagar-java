package br.com.victorvilar.contaspagar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.victorvilar.contaspagar.entities.MovimentoPagamento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

public interface MovimentoPagamentoRepository extends JpaRepository<MovimentoPagamento, Long> {

    List<MovimentoPagamento> findByDataPagamentoIsNull();
    List<MovimentoPagamento> findByDataPagamentoIsNotNull();

    public List<MovimentoPagamento> getAllByDespesaId(Long id);
    
    @Query("SELECT m FROM MovimentoPagamento m WHERE LOWER(m.despesa.nome) LIKE LOWER(CONCAT('%', :despesaName, '%')) AND dataVencimento >= :dataInicio AND dataVencimento <= :dataFim AND dataPagamento IS NULL ORDER BY dataVencimento ASC")
    List<MovimentoPagamento> getBetweenDatesAndDespesaNameNaoPago(LocalDate dataInicio, LocalDate dataFim, String despesaName);
    
    @Query("SELECT m FROM MovimentoPagamento m WHERE LOWER(m.despesa.nome) LIKE LOWER(CONCAT('%', :despesaName, '%')) AND dataVencimento >= :dataInicio AND dataVencimento <= :dataFim AND dataPagamento IS NOT NULL ORDER BY dataVencimento ASC")
    List<MovimentoPagamento> getBetweenDatesAndDespesaNamePago(LocalDate dataInicio, LocalDate dataFim, String despesaName);

    Optional<MovimentoPagamento> findByIntegridade(String integridade);
}
