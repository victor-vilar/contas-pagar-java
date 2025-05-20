package br.com.victorvilar.contaspagar.entities;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="despesas_avulsas")
public class DespesaAvulsa extends DespesaAbstrata implements Serializable {



	@Override
	public BigDecimal getValorTotal() {
		if (getParcelas().isEmpty()) {
			return BigDecimal.ZERO;

		}

		BigDecimal total = BigDecimal.ZERO;
		for (MovimentoPagamento m : getParcelas()){
			total = total.add(m.getValorPagamento());
		}
		return total;

	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="nota_id")
	private NotaFiscal notaFiscal;
	
	public DespesaAvulsa(){
		super();
		this.setTipo("AVULSA");
	}
	

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}
	


	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}




	
	




	
	
	
	
	
}
