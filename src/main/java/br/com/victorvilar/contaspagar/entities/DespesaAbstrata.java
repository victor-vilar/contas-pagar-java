package br.com.victorvilar.contaspagar.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="despesa")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DespesaAbstrata implements Despesa{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String nomeFornecedor;
	@Column(nullable=false)
	private String descricao;
	private boolean quitado = false;
	private BigDecimal valorTotal;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="despesa")
	private List<MovimentoPagamento> movimentos = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name="categoria_fk",foreignKeyDefinition="FOREIGN KEY (categoria_id) REFERENCES categorias_despesas(id) ON DELETE SET NULL"))
	private CategoriaDespesa categoria;
	

	public Long getId() {
		return id;
	}
	
	@Override
	public CategoriaDespesa getCategoria() {
		return this.categoria;
	}
	
	
	@Override
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	
	@Override
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public boolean isQuitado() {
		return this.quitado;
	}
	

	public void setId(Long id) {
		this.id = id;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setCategoria(CategoriaDespesa categoria) {
		this.categoria = categoria;
	}

	public void setParcelas(List<MovimentoPagamento> movimentos) {
             this.movimentos.clear();
             movimentos.stream().forEach(m ->addParcela(m));
            
	}
        
        public List<MovimentoPagamento> getParcelas(){
            return this.movimentos;
        }
	
	public void addParcela(MovimentoPagamento parcela) {
		
		if(!this.movimentos.contains(parcela)) {
			movimentos.add(parcela);
                        parcela.setDespesa(this);
		}
                
                getValorTotal();
	}
	
	public int getQuantidadeParcelas() {
		return this.movimentos.size();
	}
	
	@Override
	public BigDecimal getValorTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(MovimentoPagamento p : movimentos) {
		 total = total.add(p.getValorPagamento());
		}
		this.valorTotal = total;
		return total;
	}
	


	
	
	
	
}
