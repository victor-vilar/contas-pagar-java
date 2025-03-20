package br.com.thveiculos.erp.entities.despesas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Table(name="despesas_avulsas")
public class DespesaAvulsa extends DespesaAbstrata implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private List<Parcela> parcelas = new ArrayList<>();
	private NotaFiscal notaFiscal;
	
	public DespesaAvulsa(){
		
	}
	
	public Long getId() {
		return id;
	}
	
	public List<Parcela> getParcelas() {
		return parcelas;
	}
	
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}
	
	@Override
	public BigDecimal getValorTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(Parcela p : parcelas) {
		 total = total.add(p.getValorPagamento());
		}
		return total;
	}
	
	public int getQuantidadeParcelas() {
		return this.parcelas.size();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParcelas(List<Parcela> parcelas) {
		this.parcelas = parcelas;
	}
	
	public void addParcela(Parcela parcela) {
		
		if(!this.parcelas.contains(parcela)) {
			parcelas.add(parcela);
		}
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	




	
	
	
	
	
}
