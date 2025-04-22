package br.com.victorvilar.contaspagar.entities;

import jakarta.persistence.CascadeType;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="despesas_avulsas")
public class DespesaAvulsa extends DespesaAbstrata implements Serializable {

        public final String tipo = "AVULSA";
        public String getTipo(){
            return tipo;
        }
    
    
	@OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name="despesa_id")
	private NotaFiscal notaFiscal;
	
	public DespesaAvulsa(){
		super();
	}
	

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}
	


	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}


	
	




	
	
	
	
	
}
